package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Déclaration de méthode.
 * <ul>
 * <li>{@link #returnType}
 * <li>{@link #methodId}
 * <li>{@link #fargs}
 * <li>{@link #vars}
 * <li>{@link #stmts}
 * <li>{@link #returnType}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label      le label du nœud.
 * @param enfants    les enfants du nœud.
 * @param locations  les positions de début et de fin du nœud dans le fichier
 *                   source.
 * @param returnType le type de retour.
 * @param methodId   le nom de la méthode.
 * @param fargs      la liste des paramètres formels.
 * @param vars       la liste des variables.
 * @param stmts      liste des instructions.
 * @param returnExp  l'argument de l'instruction {@code return}.
 */
public record Method(String label, List<AstNode> enfants, AstLocations locations, Type returnType, Ident methodId,
        AstList<Formal> fargs, AstList<Variable> vars, AstList<Stmt> stmts, Expr returnExp) implements AstNode {
    @Override
    public void accept(final AstVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return print();
    }

    /**
     * construit un enregistrement avec des valeurs par défaut pour les premiers
     * attributs (label, etc.). Cette méthode est utilisée dans CUP.
     * 
     * @param returnType le type de retour.
     * @param methodId   le nom de la méthode.
     * @param fargs      la liste des paramètres formels.
     * @param vars       la liste des variables.
     * @param stmts      liste des instructions.
     * @param returnExp  l'argument de l'instruction {@code return}.
     * @return le nouveau nœud.
     */
    public static Method create(final Type returnType, final Ident methodId, final AstList<Formal> fargs,
            final AstList<Variable> vars, final AstList<Stmt> stmts, final Expr returnExp) {
        return new Method(Method.class.getSimpleName(),
                Arrays.asList(returnType, methodId, fargs, vars, stmts, returnExp), new AstLocations(), returnType,
                methodId, fargs, vars, stmts, returnExp);
    }
}
