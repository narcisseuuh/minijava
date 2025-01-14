package phase.b_syntax.ast;

import java.util.ArrayList;
import java.util.List;

import compil.util.AstLocations;

/**
 * Constante Booléenne.
 * <ul>
 * <li>{@link #value}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param value     la valeur booléenne.
 */
public record ExprLiteralBool(String label, List<AstNode> enfants, AstLocations locations, boolean value)
        implements Expr {
    @Override
    public String toString() {
        return print() + " " + this.value;
    }

    @Override
    public void accept(final AstVisitor v) {
        v.visit(this);
    }

    /**
     * construit un enregistrement avec des valeurs par défaut pour les premiers
     * attributs (label, etc.). Cette méthode est utilisée dans CUP.
     * 
     * @param value la valeur booléenne.
     * @return le nouveau nœud.
     */
    public static ExprLiteralBool create(final boolean value) {
        return new ExprLiteralBool(ExprLiteralBool.class.getSimpleName(), new ArrayList<>(), new AstLocations(), value);
    }
}
