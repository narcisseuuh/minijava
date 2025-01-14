package phase.b_syntax.ast;

/**
 * Interface Visiteur pour l'AST Minijava.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public interface AstVisitor {
    /**
     * Visite une liste de nœuds homogènes.
     * 
     * @param <T> le type commun des enfants.
     * @param n   le nœud visité.
     */
    <T extends AstNode> void visit(AstList<T> n);

    /**
     * Visite l'Axiome.
     * 
     * @param n le nœud visité.
     */
    void visit(Axiom n);

    /**
     * Visite une Déclaration de classe.
     * 
     * @param n le nœud visité.
     */
    void visit(Klass n);

    /**
     * Visite une Déclaration de la classe "Main".
     * 
     * @param n le nœud visité.
     */
    void visit(KlassMain n);

    /**
     * Visite le corps d'une classe.
     * 
     * @param n le nœud visité.
     */
    void visit(KlassBody klassBody);

    /**
     * Visite une Déclaration de Méthode.
     * 
     * @param n le nœud visité.
     */
    void visit(Method n);

    /**
     * Visite le corps d'une méthode.
     * 
     * @param n le nœud visité.
     */
    void visit(MethodBody methodBody);

    /**
     * Visite une Déclaration de paramètre formel.
     * 
     * @param n le nœud visité.
     */
    void visit(Formal n);

    /**
     * Visite un Identificateur (classe, méthode, champs, variable).
     * 
     * @param n le nœud visité.
     */
    void visit(Ident n);

    /**
     * Visite un Nom de type.
     * 
     * @param n le nœud visité.
     */
    void visit(Type n);

    /**
     * Visite une Déclaration de variable/champs.
     * 
     * @param n le nœud visité.
     */
    void visit(Variable n);

    /**
     * Visite une Expression longueur de tableau.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprArrayLength n);

    /**
     * Visite une Expression lecture dans un tableau.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprArrayLookup n);

    /**
     * Visite une Expression instanciation de tableau.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprArrayNew n);

    /**
     * Visite une Expression appel de fonction.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprCall n);

    /**
     * Visite une Expression valeur de variable.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprIdent n);

    /**
     * Visite une Expression constante booléenne.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprLiteralBool n);

    /**
     * Visite une Expression constante entière.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprLiteralInt n);

    /**
     * Visite une Expression instanciation d'objet.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprNew n);

    /**
     * Visite une Expression opérateur binaire.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprOpBin n);

    /**
     * Visite une Expression opérateur unaire.
     * 
     * @param n le nœud visité.
     */
    void visit(ExprOpUn n);

    /**
     * Visite une Instruction affectation dans un tableau.
     * 
     * @param n le nœud visité.
     */
    void visit(StmtArrayAssign n);

    /**
     * Visite une Instruction affectation.
     * 
     * @param n le nœud visité.
     */
    void visit(StmtAssign n);

    /**
     * Visite une Instruction bloc d'instructions.
     * 
     * @param n le nœud visité.
     */
    void visit(StmtBlock n);

    /**
     * Visite une Instruction if.
     * 
     * @param n le nœud visité.
     */
    void visit(StmtIf n);

    /**
     * Visite une Instruction impression valeur entière.
     * 
     * @param n le nœud visité.
     */
    void visit(StmtPrint n);

    /**
     * Visite une Instruction while.
     * 
     * @param n le nœud visité.
     */
    void visit(StmtWhile n);
}
