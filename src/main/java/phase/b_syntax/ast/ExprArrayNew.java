package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.EnumType;
import compil.util.AstLocations;

/**
 * Instanciation d'un tableau.
 * <ul>
 * <li>{@link #size}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param size      la taille du tableau.
 * @param type      Le type du tableau. {@code INT_ARRAY} par défaut.
 */
public record ExprArrayNew(String label, List<AstNode> enfants, AstLocations locations, Expr size, EnumType type)
        implements Expr {

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
     * @param size la taille du tableau.
     * @param type Le type du tableau. {@code INT_ARRAY} par défaut.
     * @return le nouveau nœud.
     */
    public static ExprArrayNew create(final Expr size, final EnumType type) {
        return new ExprArrayNew(ExprArrayNew.class.getSimpleName(), Arrays.asList(size), new AstLocations(), size,
                type);
    }
}
