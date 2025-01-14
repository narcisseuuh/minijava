package phase.e_codegen;

import phase.d_intermediate.IntermediateRepresentation;
import phase.d_intermediate.ir.IRQuadruple;
import phase.d_intermediate.ir.IRVariable;
import phase.d_intermediate.ir.IRvisitorDefault;
import phase.e_codegen.access.Access;

/**
 * La traduction de la forme intermédiaire vers MIPS.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class ToMips extends IRvisitorDefault {
    /**
     * L'exit status pour l'exception "on dépasse les bornes".
     */
    protected static final int OUTBAND = 42;
    /**
     * L'exit status pour l'exception "tableau de taille négative.
     */
    protected static final int OUTBANDNEW = 43;
    /**
     * La taille mémoire d'un registre variable. 32 bits !
     */
    protected static final int SIZEOF = 4;
    /**
     * Le Nombre d'arguments passés dans les registres.
     */
    protected static final int NBARGS = 4;
    /**
     * Les 4 registres pour les arguments de la fonction.
     */
    protected static final MIPSRegister[] AREGS = { MIPSRegister.A0, MIPSRegister.A1, MIPSRegister.A2,
            MIPSRegister.A3 };
    /**
     * Les registres "s" à sauvegarder. (non utilisé)
     */
    protected static final MIPSRegister[] S_REG_USED_LIST = {};
    // {Reg.S0, Reg.S1, Reg.S2, ...
    /**
     * Les registres "t" à sauvegarder. (non utilisé)
     */
    protected static final MIPSRegister[] T_REG_USED_LIST = {};
    // {Reg.T0, Reg.T1, Reg.T2, ...
    /**
     * L'allocateur.
     */
    protected final Allocator allocator;
    /**
     * La forme intermédiaire.
     */
    protected final IntermediateRepresentation ir;
    /**
     * Le Writer MIPS.
     */
    protected final MipsWriter mw;
    /**
     * Une table pour les paramètres d'appel de fonction. Empile les {@code QParam}
     * pour traitement par {@code QCall*}.
     */
    protected final java.util.ArrayList<IRVariable> params = new java.util.ArrayList<>();

    /**
     * Construit la traduction de la forme intermédiaire vers MIPS.
     * 
     * @param ir         la forme intermédiaire.
     * @param allocator  l'allocateur.
     * @param mipsWriter le "Writer" pour l'impression MIPS.
     */
    public ToMips(final phase.d_intermediate.IntermediateRepresentation ir, final Allocator allocator,
            final MipsWriter mipsWriter) {
        this.allocator = allocator;
        this.ir = ir;
        this.mw = mipsWriter;
    }

    /**
     * Exécute la traduction de la forme intermédiaire vers MIPS.
     */
    public void execute() {
        mw.println(".text");
        for (IRQuadruple q : this.ir.program()) {
            mw.com(q.toString()); // instruction IR en commentaire
            q.accept(this);
        }
    }

    // Helpers : save/load dans les registres
    /**
     * Charge une variable IR dans un registre.
     * 
     * @param reg le registre à charger.
     * @param v   la variable IR.
     */
    protected void regLoad(final MIPSRegister reg, final IRVariable v) {
        Access a = allocator.access(v);
        String aName = a.getClass().getName();
        mw.com("regload " + aName.substring(aName.lastIndexOf('.') + 1) + " -> var " + v.name() + " in reg "
                + reg.name());
        mw.inst(a.load(reg));
    }

    /**
     * Enregistre un registre dans une variable IR.
     * 
     * @param reg le registre à sauver.
     * @param v   la variable IR.
     */
    protected void regStore(final MIPSRegister reg, final IRVariable v) {
        Access a = allocator.access(v);
        String aName = a.getClass().getName();
        mw.com("regstore " + aName.substring(aName.lastIndexOf('.') + 1) + " -> reg " + reg.name() + " in var "
                + v.name());
        mw.inst(a.store(reg));
    }

    // Helpers : save/restore dans la pile
    /**
     * Empile le contenu de N registres (sauvegarde).
     * 
     * @param regs        les registres à empiler.
     * @param commentaire le commentaire mis à la fin de la ligne.
     */
    protected void push(final MIPSRegister... regs) {
        final int size = regs.length;
        mw.com("push : " + regs.length + " octet(s) à sauvegarder");
        mw.plus(MIPSRegister.SP, -SIZEOF * size);
        for (int i = 0; i < size; i++) {
            mw.com("push -> store " + regs[i].name() + " at (" + SIZEOF * (size - i - 1) + ")" + MIPSRegister.SP);
            mw.storeOffset(regs[i], SIZEOF * (size - i - 1), MIPSRegister.SP);
        }
    }

    /**
     * Récupère le contenu de N registres (restauration).
     * 
     * @param regs les registres à récupérer.
     */
    protected void pull(final MIPSRegister... regs) {
        final int size = regs.length;
        mw.com("pull : " + regs.length + " octet(s) à récupérer");
        for (int i = 0; i < size; i++) {
            mw.com("pull -> load " + regs[i].name() + " from (" + SIZEOF * (size - i - 1) + ")" + MIPSRegister.SP);
            mw.loadOffset(regs[i], SIZEOF * (size - i - 1), MIPSRegister.SP);
        }
        mw.plus(MIPSRegister.SP, SIZEOF * size);
    }

    // Helpers : Convention d'appel

    /**
     * Sauvegarde de l'appelant. (avant l'appel)
     */
    protected void callerSave() {
        mw.com("--sauvegarde par l'appelant des registers (push T0-9 puis FP, A0-3)");
        push(T_REG_USED_LIST);
        push(MIPSRegister.FP, MIPSRegister.A3, MIPSRegister.A2, MIPSRegister.A1, MIPSRegister.A0);
        mw.com("--fin de la sauvegarde par l'appelant des registres");
    }

    /**
     * Démarrage de l'appelé.
     */
    protected void calleeSave() {
        int offset = -SIZEOF;
        mw.com("--sauvegarde par l'appelant des registres (store RA puis S0-7)");
        mw.com("storeOffset -> RA à sauvegarder");
        mw.storeOffset(MIPSRegister.RA, offset, MIPSRegister.FP);
        mw.com("storeOffset -> S0-7 : " + S_REG_USED_LIST.length + " à sauvegarder");
        for (MIPSRegister reg : S_REG_USED_LIST) {
            offset -= SIZEOF;
            mw.storeOffset(reg, offset, MIPSRegister.FP);
        }
        mw.com("--fin de la sauvegarde par l'appelant des registres");
    }

    /**
     * Terminaison de l'appelé.
     */
    protected void calleeRestore() {
        int offset = -SIZEOF;
        mw.com("--restauration par l'appelé (RA, S0-7)");
        mw.com("loadOffset -> RA à récupérer");
        mw.loadOffset(MIPSRegister.RA, offset, MIPSRegister.FP);
        mw.com("loadOffset -> S0-7 : " + S_REG_USED_LIST.length + " à récupérer");
        for (MIPSRegister reg : S_REG_USED_LIST) {
            offset -= SIZEOF;
            mw.loadOffset(reg, offset, MIPSRegister.FP);
        }
        mw.com("--fin de la restauration par l'appelé");
    }

    /**
     * Restauration de l'appelant. (retour d'appel)
     */
    protected void callerRestore() {
        mw.com("--restauration par l'appelant des registers (T0-9, FP, A0-3)");
        pull(MIPSRegister.FP, MIPSRegister.A3, MIPSRegister.A2, MIPSRegister.A1, MIPSRegister.A0);
        pull(T_REG_USED_LIST);
        mw.com("--fin de la restauration par l'appelant");
    }

    // ////////////// VISIT ///////////////
}
