package phase.e_codegen;

import phase.d_intermediate.IntermediateRepresentation;
import phase.d_intermediate.ir.*;
import phase.e_codegen.access.Access;
import phase.e_codegen.access.AccessReg;

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
    @Override
    public void visit(final QLabel q) {
        mw.label(q.arg1().name());
    }

    @Override
    public void visit(final QParam q) {
        this.params.add(q.arg1());
    }
    
    @Override
    public void visit(final QCallStatic q) {
        final String function = q.arg1().name();
        final int nbArg = q.arg2().value();
        if (nbArg != this.params.size()) {
            throw new compil.util.CompilerException("ToMips : Params error");
        }
        if (nbArg > NBARGS) {
            throw new compil.util.CompilerException("ToMips : too many args " + function);
        }
        switch (function) {
        case "_exit":
            push(MIPSRegister.A0);
            regLoad(MIPSRegister.A0, this.params.get(0));
            mw.jumpIn("_system_exit");
            pull(MIPSRegister.A0);
            break;
        case "_println":
            push(MIPSRegister.A0);
            regLoad(MIPSRegister.A0, this.params.get(0));
            mw.jumpIn("_system_out_println");
            pull(MIPSRegister.A0);
            break;
        case "main":
            throw new compil.util.CompilerException("ToMips : recurse main forbidden");
        default:
            throw new compil.util.CompilerException("ToMips : wrong special " + function);
        }
        this.params.clear();
    }

        @Override
    public void visit(final QJump q) {
        mw.jump(q.arg1().name());
    }

    @Override
    public void visit(final QJumpCond q) {
        regLoad(MIPSRegister.V0, q.arg2());
        mw.jumpIfNot(MIPSRegister.V0, q.arg1().name());
    }

    @Override
    public void visit(final QCopy q) {
        // optimisation 1
        if (this.allocator.access(q.arg1()) instanceof AccessReg regArg1) {
            regStore(regArg1.getRegister(), q.result());
            return;
        }
        // optimisation 2
        if (this.allocator.access(q.result()) instanceof AccessReg regResult) {
            regLoad(regResult.getRegister(), q.arg1());
            return;
        }
        // cas général
        regLoad(MIPSRegister.V0, q.arg1());
        regStore(MIPSRegister.V0, q.result());
    }

    @Override
    public void visit(final QNew q) {
        final String klassName = q.arg1().name();
        final Integer size = this.allocator.classSize(klassName);
        if (size == null) {
            throw new compil.util.CompilerException("ToMips.QNew : unknown size for class " + klassName);
        }
        if (size < 0) {
            throw new compil.util.CompilerException("ToMips.QNew : negative size malloc for class " + klassName);
        }
        push(MIPSRegister.A0);
        mw.load(MIPSRegister.A0, size);
        mw.jumpIn("_new_object");
        regStore(MIPSRegister.V0, q.result());
        pull(MIPSRegister.A0);
    }

    @Override
    public void visit(final QAssign q) {
        regLoad(MIPSRegister.V0, q.arg1());
        regLoad(MIPSRegister.V1, q.arg2());
        switch (q.op()) {
        case PLUS -> mw.plus(MIPSRegister.V0, MIPSRegister.V1);
        case MINUS -> mw.moins(MIPSRegister.V0, MIPSRegister.V1);
        case TIMES -> mw.fois(MIPSRegister.V0, MIPSRegister.V1);
        case AND -> mw.et(MIPSRegister.V0, MIPSRegister.V1);
        case LESS -> mw.inferieur(MIPSRegister.V0, MIPSRegister.V1);
        default -> throw new compil.util.CompilerException("Invalid binary Operator " + q.op());
        }
        regStore(MIPSRegister.V0, q.result());
    }

    @Override
    public void visit(final QNewArray q) {
        push(MIPSRegister.A0);
        regLoad(MIPSRegister.A0, q.arg2());

        // compute size
        mw.plus(MIPSRegister.A0, 1);
        mw.fois4(MIPSRegister.A0);

        mw.jumpIn("_new_object");
        regLoad(MIPSRegister.A0, q.arg2()); // useless ?? is the poly wrong ?

        mw.storeOffset(MIPSRegister.A0, 0, MIPSRegister.V0);
        regStore(MIPSRegister.A0, q.result());

        pull(MIPSRegister.A0);
    }

    @Override
    public void visit(final QAssignArrayFrom q) {
        regLoad(MIPSRegister.V0, q.arg1());
        regLoad(MIPSRegister.V1, q.arg2());

        // calcul de l'adresse
        mw.fois4(MIPSRegister.V0);
        mw.plus(MIPSRegister.V0, MIPSRegister.V1);

        // load offset, using 4 as a constant offset
        // instead of adding it to save an instruction
        mw.loadOffset(MIPSRegister.V0, 4, MIPSRegister.V0);

        regStore(MIPSRegister.V0, q.result());
    }

    @Override
    public void visit(final QAssignArrayTo q) {
        regLoad(MIPSRegister.V0, q.arg2());
        regLoad(MIPSRegister.V1, q.result());

        // calcul de l'adresse
        mw.fois4(MIPSRegister.V0);
        mw.plus(MIPSRegister.V0, MIPSRegister.V1);
        mw.plus(MIPSRegister.V0, 4);
        
        // load offset, using 4 as a constant offset
        // instead of adding it to save an instruction
        regLoad(MIPSRegister.V0, q.arg1());
        mw.storeOffset(MIPSRegister.V0, 0, MIPSRegister.V0);
    }

    @Override
    public void visit(final QLength q) {
        regLoad(MIPSRegister.V0, q.arg1());
        mw.loadOffset(MIPSRegister.V0, 0, MIPSRegister.V0);

        regStore(MIPSRegister.V0, q.result());
    }

    @Override
    public void visit(final QAssignUnary q) {
        regLoad(MIPSRegister.V0, q.arg1());
        if (q.op() == compil.EnumOper.NOT) {
            mw.not(MIPSRegister.V0);
        } else {
            throw new compil.util.CompilerException("Invalid unary Operator " + q.op());
        }
        regStore(MIPSRegister.V0, q.result());
    }

    @Override
    public void visit(final QLabelMeth q) {
        final String function = q.arg1().name();
        mw.label(function);
        mw.com("--création de la trame dans l'appelé");
        mw.com("positionne la trame $fp (et sauvegarde $sp)");
        mw.move(MIPSRegister.FP, MIPSRegister.SP); // positionne la trame $fp (et sauvegarde $sp)
        mw.com("alloue la trame : " + MIPSRegister.SP + ", " + -allocator.frameSize(function));
        mw.plus(MIPSRegister.SP, -allocator.frameSize(function)); // alloue la trame
        calleeSave();
        // A1, A2, A3 recopiés comme variables locales
        final int local0 = -allocator.frameSizeMin() - SIZEOF;
        mw.com("recopie de A1, A2, A3 comme variables locales : " + local0);
        final int nbArg = q.arg2().value();
        if (nbArg > 1) {
            mw.storeOffset(MIPSRegister.A1, local0, MIPSRegister.FP);
        }
        if (nbArg > 2) {
            mw.storeOffset(MIPSRegister.A2, local0 - SIZEOF, MIPSRegister.FP);
        }
        if (nbArg > 3) {
            mw.storeOffset(MIPSRegister.A3, local0 - 2 * SIZEOF, MIPSRegister.FP);
        }
        // NB : l'execution des 3 copies sans tester le nombre d'arguments
        // donne aussi un code correct
        // Initialisation des variables locales
        // on devrait le faire ici mais on a oublié !!!
        mw.com("--fin de la création de la trame dans l'appelé");
        mw.com("--code de l'appelé " + q.arg1());
    }

    @Override
    public void visit(final QReturn q) {
        calleeRestore();
        regLoad(MIPSRegister.V0, q.arg1());
        mw.com("--déallocation de la trame en restaurant SP à partir de FP");
        mw.move(MIPSRegister.SP, MIPSRegister.FP);
        mw.com("--retour à l'appelant en utilisant RA qui vient d'être récupéré");
        mw.jumpOut();
    }

    @Override
    public void visit(final QCall q) {
        final String function = q.arg1().name();
        final int nbArg = q.arg2().value();
        // check nombre d'arguments
        if (nbArg != this.params.size()) {
            throw new compil.util.CompilerException("ToMips : Params error");
        }
        // Sauvegarde registres non préservés par un appel de fonction
        callerSave();
        // Passage des Arguments dans $A0-$A3 puis dans la pile
        // N.B. : Danger d'écrasement sur les registres $A_i :
        // si les valeurs des arguments de l'appelé dépendent
        // des arguments de l'appelant, il ne faut pas prendre
        // ces valeurs dans les registres $A_i mais dans la sauvegarde des $A_i
        // que l'on vient de faire sur la pile !
        // Exemple : rec.f(tutu,titi) avec tutu = champs d'objet
        // this = $A0
        // tutu == this.tutu == Offset_tutu ( $A0 )
        // si on affecte d'abord $A0 (this=rec), on a perdu (rec.tutu??) !!!
        // Seul $A0 reste alloué dans un registre
        // les parametres $A1, $A2, $A3 sont obligatoirement
        // recopiées dans des variables locales allouées dans la frame.
        // => Seule contrainte contre l'écrasement :
        // l'argument A0 est chargé (et écrasé) le dernier.
        // arguments 1, 2 ,3 dans A1, A2, A3
        mw.com("--arguments 1, 2, 3 dans A1, A2, A3");
        for (int i = 1; i < NBARGS && i < nbArg; i++) {
            regLoad(AREGS[i], this.params.get(i));
        }
        // argument 0 == this, obligatoirement le dernier, dans $A0
        mw.com("--argument 0 == this, obligatoirement le dernier, dans $A0");
        regLoad(MIPSRegister.A0, this.params.get(0));
        // Jump
        mw.com("--saut dans la fonction " + function);
        mw.jumpIn(function);
        // restauration registre
        callerRestore();
        // resultat dans V0
        regStore(MIPSRegister.V0, q.result());
        // clear des QParams
        this.params.clear();
    }
}

