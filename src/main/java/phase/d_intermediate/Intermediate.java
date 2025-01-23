package phase.d_intermediate;

import compil.util.Debug;
import phase.b_syntax.ast.*;
import phase.c_semantic.SemanticAttribute;
import phase.c_semantic.SemanticTree;
import phase.d_intermediate.ir.*;

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
    @Override
    public void visit(final KlassMain n) {
        final String mainMeth = "main";
        add(new QLabel(newLabel(mainMeth)));
        String saved = this.currentMethod;
        this.currentMethod = mainMeth;
        defaultVisit(n);
        this.currentMethod = saved;
        add(new QParam(newConst(0)));
        add(new QCallStatic(newLabel("_exit"), newConst(1)));
    }

    @Override
    public void visit(final Method n) {
        add(new QLabelMeth(newLabel(n.methodId().name()), newConst(1 + n.fargs().nbChildren())));
        String saved = this.currentMethod;
        this.currentMethod = n.methodId().name();
        defaultVisit(n);
        this.currentMethod = saved; //
        add(new QReturn(getVar(n.returnExp())));
    }

    // Expressions
    @Override
    public void visit(final ExprOpBin n) {
        defaultVisit(n);
        setVar(n, newTemp());
        add(new QAssign(n.op(), getVar(n.expr1()), getVar(n.expr2()), getVar(n)));
    }

    @Override
    public void visit(final ExprOpUn n) {
        defaultVisit(n);
        setVar(n, newTemp());
        add(new QAssignUnary(n.op(), getVar(n.expr()), getVar(n)));
    }

    @Override
    public void visit(final ExprLiteralInt n) {
        setVar(n, newConst(n.value()));
    }

    @Override
    public void visit(final ExprLiteralBool n) {
        setVar(n, newConst(Boolean.TRUE.equals(n.value()) ? 1 : 0));
    }

    @Override
    public void visit(final ExprCall n) {
        defaultVisit(n);
        add(new QParam(getVar(n.receiver()))); // this
        for (AstNode f : n.args()) {
            add(new QParam(getVar(f)));
        }
        setVar(n, newTemp());
        add(new QCall(newLabel(n.methodId().name()), newConst(n.args().nbChildren() + 1), getVar(n)));
    }

    @Override
    public void visit(final ExprIdent n) {
        setVar(n, lookupVar(n.varId().name(), n));
    }

    @Override
    public void visit(final ExprNew n) {
        defaultVisit(n);
        setVar(n, newTemp());
        add(new QNew(newLabel(n.klassId().name()), getVar(n)));
    }

    // Instructions
    
    @Override
    public void visit(final StmtPrint n) {
        defaultVisit(n);
        add(new QParam(getVar(n.expr())));
        add(new QCallStatic(newLabel("_println"), newConst(1)));
    }

    @Override
    public void visit(final StmtAssign n) {
        defaultVisit(n);
        // setVar(n, lookupVar(n.varId().name(), n)); // non nécessaire
        add(new QCopy(getVar(n.value()), lookupVar(n.varId().name(), n)));
    }

    @Override
    public void visit(final StmtBlock n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtIf n) {
        final IRLabel l1 = newLabel();
        final IRLabel l2 = newLabel();
        n.test().accept(this);
        add(new QJumpCond(l1, getVar(n.test())));
        n.ifTrue().accept(this);
        add(new QJump(l2));
        add(new QLabel(l1));
        n.ifFalse().accept(this);
        add(new QLabel(l2));
    }

    @Override
    public void visit(final StmtWhile n) {
        final IRLabel l1 = newLabel();
        final IRLabel l2 = newLabel();
        add(new QLabel(l1));
        n.test().accept(this);
        add(new QJumpCond(l2, getVar(n.test())));
        n.body().accept(this);
        add(new QJump(l1));
        add(new QLabel(l2));
    }
}
