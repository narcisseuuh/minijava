package phase.d_intermediate;

import compil.util.Debug;
import phase.b_syntax.ast.AstNode;
import phase.b_syntax.ast.AstVisitorDefault;
import phase.b_syntax.ast.Axiom;
import phase.c_semantic.SemanticAttribute;
import phase.c_semantic.SemanticTree;
import phase.d_intermediate.ir.IRConst;
import phase.d_intermediate.ir.IRLabel;
import phase.d_intermediate.ir.IRQuadruple;
import phase.d_intermediate.ir.IRTempVar;
import phase.d_intermediate.ir.IRVariable;

/**
 * Génération de la forme intermédiaire (Code à 3 adresses).
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class Intermediate extends AstVisitorDefault {
    /**
     * l'axiome ou nœud racine de l'AST.
     */
    protected final Axiom axiom;
    /**
     * l'arbre sémantique.
     */
    protected final SemanticTree semanticTree;
    /**
     * La représentation intermédiaire en sortie de la génération.
     */
    protected final IntermediateRepresentation ir;
    /**
     * {@code true} si des erreurs dans la forme intermédiaire.
     */
    protected boolean error;
    /**
     * L'attribut synthétisé Var. Variable IR pour les résultats des expressions.
     */
    protected final SemanticAttribute<IRVariable> varAttr;
    /**
     * L'attribut synthétisé currentMethod. Utilisé comme portée pour les variables
     * IR temporaires.
     */
    protected String currentMethod;

    /**
     * Construit une génération de la forme intermédiaire.
     * 
     * @param axiom l'axiome ou nœud racine de l'AST.
     */
    public Intermediate(final Axiom axiom, final SemanticTree semanticTree) {
        this.axiom = axiom;
        this.semanticTree = semanticTree;
        this.ir = new IntermediateRepresentation(semanticTree);
        this.varAttr = new SemanticAttribute<>();
        this.currentMethod = null;
        this.error = false;
    }

    /**
     * Exécute la génération de la forme intermédiaire.
     * 
     * @return la forme intermédiaire.
     * @throws compil.util.CompilerException si programme Intermédiaire invalide.
     */
    public IntermediateRepresentation execute() {
        axiom.accept(this);
        if (Debug.INTERMED) {
            Debug.log(this.ir);
        }
        if (this.error) {
            throw new compil.util.CompilerException("Programme Intermédiaire invalide");
        }
        return this.ir;
    }

    // Helpers
    
    /**
     * Génération erreur sur variable null.
     * 
     * @param irv une variable.
     * @return la variable si non null, une variable "undef" sinon.
     */
    protected IRVariable nonNull(final IRVariable irv) {
        if (irv != null) {
            return irv;
        }
        this.error = true;
        return new IRVariable() { /* une variable indéfinie */
            public String name() {
                return "_UNDEF_";
            }

            public String type() {
                return "void";
            }

            public String toString() {
                return "_UNDEF_";
            }
        };
    }

    /**
     * Retourne l'attribut Var.
     * 
     * @param n le nœud de l'AST.
     * @return la valeur de l'attribut Var.
     */
    protected IRVariable getVar(final AstNode n) {
        return nonNull(this.varAttr.get(n));
    }

    /**
     * Positionne l'attribut Var.
     * 
     * @param n   le nœud de l'AST.
     * @param irv la valeur de l'attribut Var.
     */
    protected void setVar(final AstNode n, final IRVariable irv) {
        this.varAttr.set(n, irv);
    }

    /**
     * Ajoute une instruction au programme IR.
     * 
     * @param irq l'instruction ajoutée en fin de programme.
     */
    protected void add(final IRQuadruple irq) {
        this.ir.program().add(irq);
    }

    /**
     * Crée un label unique.
     * 
     * @return le label.
     */
    protected IRLabel newLabel() {
        return this.ir.createLabel();
    }

    /**
     * Crée un label avec nom.
     * 
     * @param name le nom de Label.
     * @return le label.
     */
    protected IRLabel newLabel(final String name) {
        return this.ir.createLabel(name);
    }

    /**
     * Crée une constante.
     * 
     * @param value la valeur de la constante.
     * @return la constante.
     */
    protected IRConst newConst(final int value) {
        return this.ir.createConst(value);
    }

    /**
     * Crée une variable temporaire dans la portée de la méthode.
     * 
     * @return la variable temporaire.
     */
    protected IRTempVar newTemp() {
        return this.ir.createTemp(this.currentMethod);
    }

    /**
     * Recherche d'une variable de l'AST dans la table des symboles.
     * 
     * @param name le nom de la variable.
     * @param n    le nœud de l'AST (-> portée courante).
     * @return la variable IR ou {@code null} si indéfinie.
     */
    protected IRVariable lookupVar(final String name, final AstNode n) {
        return semanticTree.attrScope().get(n).lookupVariable(name);
    }

    /////////////////// Visit ////////////////////
}
