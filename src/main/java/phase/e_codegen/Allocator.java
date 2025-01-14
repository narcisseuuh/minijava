package phase.e_codegen;

import java.util.HashMap;
import java.util.Map;

import compil.util.CompilerException;
import compil.util.Debug;
import phase.c_semantic.symtab.InfoKlass;
import phase.c_semantic.symtab.InfoMethod;
import phase.d_intermediate.IntermediateRepresentation;
import phase.d_intermediate.ir.IRConst;
import phase.d_intermediate.ir.IRTempVar;
import phase.d_intermediate.ir.IRVariable;
import phase.e_codegen.access.Access;
import phase.e_codegen.access.AccessConst;
import phase.e_codegen.access.AccessOff;
import phase.e_codegen.access.AccessReg;

/**
 * Allocation mémoire : champs d'objet, trames des méthodes, variables,
 * registres...
 * 
 * @author Pascal Hennequin
 */
public class Allocator {
    /**
     * La taille mémoire d'une variable. Tous les types utilisent 32 bits !
     */
    static final int SIZEOF = 4;
    /**
     * Le Nombre d'arguments passés dans les registres.
     */
    private static final int NBARGS = 4;
    /**
     * Le Nombre de registres sauvegardés dans la trame (partie fixe).
     */
    static final int NBREGS = 9; // ra + s0...s7
    /**
     * La forme intermédiaire en entrée.
     */
    private final IntermediateRepresentation ir;
    /**
     * L'allocation des variables (en sortie).
     */
    private final Map<IRVariable, Access> access;
    /**
     * La taille allouée pour les variables globales (en sortie).
     */
    private int globalSize;
    /**
     * La taille des instances de classes (en sortie).
     */
    private final Map<String, Integer> classSize;
    /**
     * La taille des trames (frames) de méthodes (en sortie).
     */
    private final Map<String, Integer> frameSize;

    /**
     * Construit les allocations mémoires.
     * 
     * @param ir la forme intermédiaire.
     */
    public Allocator(final IntermediateRepresentation ir) {
        this.globalSize = 0;
        this.classSize = new HashMap<>();
        this.frameSize = new HashMap<>();
        this.access = new HashMap<>();
        this.ir = ir;
    }

    /**
     * Calcule les allocations mémoires.
     * 
     * @return L'allocateur courant.
     * @throws CompilerException si erreur dans la table de symbole.
     */
    public Allocator execute() {
        // Allocation des instances d'objets et de leurs champs.
        for (InfoKlass kl : ir.rootScope().getKlasses()) {
            classAlloc(kl);
        }
        // Allocation des variables locales et construction des trames de méthode
        for (InfoKlass kl : ir.rootScope().getKlasses()) {
            for (InfoMethod m : kl.getMethods()) {
                methodAlloc(m);
            }
        }
        // Allocation des variables temporaires IR. Ajout dans les trames de méthode.
        for (IRTempVar v : ir.tempos()) {
            tempoAlloc(v);
        }
        // "Allocation" des constantes IR.
        for (IRConst v : ir.consts()) {
            this.access.put(v, new AccessConst(v.value()));
        }

        // Les résultats
        if (Debug.ALLOCATOR) {
            Debug.log(" globalSize (main) " + this.globalSize());
            Debug.log(" classSize " + this.classSize);
            Debug.log(" frameSizeMin " + this.frameSizeMin());
            Debug.log(" frameSize " + this.frameSize);
            Debug.log(" Access " + this.access);
        }
        return this;
    }

    /**
     * Donne La taille de la mémoire pour les variables globales.
     * 
     * @return la taille des variables globales.
     */
    public Integer globalSize() {
        return globalSize;
    }

    /**
     * Donne la taille mémoire d'une instance de la classe.
     * 
     * @param klassName le nom de la classe.
     * @return la taille d'une instance de classe.
     */
    public Integer classSize(final String klassName) {
        return classSize.get(klassName);
    }

    /**
     * Donne la taille de la partie fixe de la trame des méthodes.
     * 
     * @return la taille minimum de la trame.
     */
    public Integer frameSizeMin() {
        return NBREGS * SIZEOF;
    }

    /**
     * Donne la taille de la trame (<i>frame</i>) d'une méthode.
     * 
     * @param methodName le nom de la méthode.
     * @return la taille de la trame de la méthode.
     */
    public Integer frameSize(final String methodName) {
        return frameSize.get(methodName);
    }

    /**
     * Donne l'allocation mémoire d'une variable IR.
     * 
     * @param v la variable de la forme intermédiaire.
     * @return l'accès MIPS à la variable.
     */
    public Access access(final IRVariable v) {
        return access.get(v);
    }

    // --------------------- Les Allocations -----------------------

    /**
     * Allocation des instances de classe.
     * 
     * <p>
     * Calcul de la taille d'un objet comme somme des champs de la classe et des
     * classes ancêtres.
     * <p>
     * Chaque champs est à un offset fixe par rapport à l'adresse de l'objet.
     * <p>
     * Pour MiniJAVA, seuls les champs de {@code this} sont accessibles. L'adresse
     * de l'objet est toujours dans le registre {@code $A0}.
     * 
     * @param kl La déclaration de la classe d'objet.
     */
    private void classAlloc(final InfoKlass kl) {
        if (kl == null) {
            throw new CompilerException("Allocator : class==null");
        }
        if (classSize.get(kl.getName()) != null) {
            return; // déja alloué !
        }
        Integer offset = SIZEOF * kl.getFields().size();
        final InfoKlass parent = ir.rootScope().lookupKlass(kl.getParent());
        if (parent != null) { // kl != "Object"
            classAlloc(parent);
            offset += classSize.get(parent.getName());
        }
        classSize.put(kl.getName(), offset);
        for (IRVariable v : kl.getFields()) {
            offset -= SIZEOF;
            access.put(v, new AccessOff(MIPSRegister.A0, offset));
        }
    }

    /**
     * Allocation de la trame d'une méthode (variables locales, paramètres formel).
     * 
     * <p>
     * {@code Arg n, Arg n-1, ..., Arg 4 | $ra $s0-$s7 local0, 1, 2, 3, ...}
     * 
     * <p>
     * NB : Arg1, Arg2. Arg3 recopiés dans local0, local1, local2
     * 
     * @param m la déclaration de la méthode.
     */
    private void methodAlloc(final InfoMethod m) {
        // "trame" statique globale pour "main" :
        if ("main".equals(m.getName())) {
            for (IRVariable v : m.getScope().getAllVariables()) {
                access.put(v, new AccessOff(MIPSRegister.GP, globalSize));
                globalSize += SIZEOF;
            }
            return;
        }

        // trame dynamique de méthode d'instance
        /// partie fixe : sauvegarde de $ra, $s0-$s7
        int frameIndex = frameSizeMin();
        /// paramètres formels
        int i = 0;
        for (IRVariable v : m.getArgs()) {
            switch (i) {
            case 0 -> // arg0 = this, toujours dans le registre $A0
                access.put(v, new AccessReg(MIPSRegister.A0));
            case 1, 2, 3 -> { // arg1, 2, 3 comme variable local0, 1, 2 dans la trame
                frameIndex += SIZEOF;
                access.put(v, new AccessOff(MIPSRegister.FP, -frameIndex));
            }
            default -> // arg 4, 5, ... dans la pile avant $FP
                access.put(v, new AccessOff(MIPSRegister.FP, SIZEOF * (i - NBARGS)));
            }
            i++;
        }
        /// variables locales
        for (IRVariable v : m.getScope().getAllVariables()) {
            frameIndex += SIZEOF;
            access.put(v, new AccessOff(MIPSRegister.FP, -frameIndex));
        }
        // mise à jour de la taille de trame.
        frameSize.put(m.getName(), frameIndex);
    }

    /**
     * Allocation des variables temporaires de la forme intermédiaire.
     * 
     * <p>
     * Ajout dans les trames de méthode.
     *
     * @param v la déclaration de variable temporaire.
     */
    private void tempoAlloc(final IRTempVar v) {
        final String methName = v.getScope();
        // "trame" statique globale pour "main" :
        if ("main".equals(methName)) {
            access.put(v, new AccessOff(MIPSRegister.GP, this.globalSize));
            globalSize += SIZEOF;
            return;
        }
        // trame dynamique de méthode d'instance
        int frameIndex = frameSize.get(methName);
        frameIndex += SIZEOF;
        access.put(v, new AccessOff(MIPSRegister.FP, -frameIndex));
        frameSize.put(methName, frameIndex);
    }
}
