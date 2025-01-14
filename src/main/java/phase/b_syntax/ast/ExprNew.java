package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Instanciation d'objet.
 * <ul>
 * <li>{@link #klassId}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param klassId   le nom de la classe.
 */
public record ExprNew(String label, List<AstNode> enfants, AstLocations locations, Ident klassId) implements Expr {
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
     * @param klassId le nom de la classe.
     * @return le nouveau nœud.
     */
    public static ExprNew create(final Ident klassId) {
        return new ExprNew(ExprNew.class.getSimpleName(), Arrays.asList(klassId), new AstLocations(), klassId);
    }
}
