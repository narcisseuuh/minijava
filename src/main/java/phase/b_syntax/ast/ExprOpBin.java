package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.EnumOper;
import compil.util.AstLocations;

/**
 * Opérateur Binaire.
 * <ul>
 * <li>{@link #expr1}
 * <li>{@link #op}
 * <li>{@link #expr2}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param expr1     l'opérande gauche.
 * @param op        l'opérateur.
 * @param expr2     l'opérande droite.
 */
public record ExprOpBin(String label, List<AstNode> enfants, AstLocations locations, Expr expr1, EnumOper op,
        Expr expr2) implements Expr {
    @Override
    public String toString() {
        return print() + " " + this.op.name();
    }

    @Override
    public void accept(final AstVisitor v) {
        v.visit(this);
    }

    /**
     * construit un enregistrement avec des valeurs par défaut pour les premiers
     * attributs (label, etc.). Cette méthode est utilisée dans CUP.
     * 
     * @param expr1 l'opérande gauche.
     * @param op    l'opérateur.
     * @param expr2 l'opérande droite.
     * @return le nouveau nœud.
     */
    public static ExprOpBin create(final Expr expr1, final EnumOper op, final Expr expr2) {
        return new ExprOpBin(ExprOpBin.class.getSimpleName(), Arrays.asList(expr1, expr2), new AstLocations(), expr1,
                op, expr2);
    }
}
