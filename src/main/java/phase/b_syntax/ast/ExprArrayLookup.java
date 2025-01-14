package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Accès à l'élément d'un tableau.
 * <ul>
 * <li>{@link #array}
 * <li>{@link #index}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param array     l'expression tableau.
 * @param index     l'indice de la case du tableau qui est accédée.
 */
public record ExprArrayLookup(String label, List<AstNode> enfants, AstLocations locations, Expr array, Expr index)
        implements AstNode, Expr {
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
     * @param array l'expression tableau.
     * @param index l'indice de la case du tableau qui est accédée.
     * @return le nouveau nœud.
     */
    public static ExprArrayLookup create(final Expr array, final Expr index) {
        return new ExprArrayLookup(ExprArrayLookup.class.getSimpleName(), Arrays.asList(array, index),
                new AstLocations(), array, index);
    }
}
