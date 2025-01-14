package phase.b_syntax.ast;

/**
 * Visiteur générique de l'AST avec parcours en profondeur.
 * 
 * @author Pascal Hennequin
 */
public abstract class AstVisitorDefault implements AstVisitor {
    /**
     * Parcours récursif en profondeur de l'arbre de syntaxe.
     * 
     * @param node le nœud à visiter.
     */
    public void defaultVisit(final AstNode node) {
        for (AstNode f : node) {
            f.accept(this);
        }
    }

    // Liste homogène
    @Override
    public <T extends AstNode> void visit(final AstList<T> n) {
        defaultVisit(n);
    }

    // Productions de base
    @Override
    public void visit(final Axiom n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final Klass n) {
        defaultVisit(n);
    }

    @Override
    public void visit(KlassBody n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final KlassMain n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final Method n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final MethodBody n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final Formal n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final Ident n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final Type n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final Variable n) {
        defaultVisit(n);
    }

    // Expressions
    @Override
    public void visit(final ExprArrayLength n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprArrayLookup n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprArrayNew n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprCall n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprIdent n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprLiteralBool n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprLiteralInt n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprNew n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprOpBin n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprOpUn n) {
        defaultVisit(n);
    }

    // Instructions
    @Override
    public void visit(final StmtArrayAssign n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtAssign n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtBlock n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtIf n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtPrint n) {
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtWhile n) {
        defaultVisit(n);
    }
}
