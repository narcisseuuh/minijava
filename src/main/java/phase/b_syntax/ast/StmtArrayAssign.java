package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Affectation d'un élément de tableau.
 * <ul>
 * <li>{@link #arrayId}
 * <li>{@link #index}
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
 * @param arrayId   le nom de la variable tableau.
 * @param index     l'indice de l'élément accédé.
 * @param value     la nouvelle valeur.
 */
public record StmtArrayAssign(String label, List<AstNode> enfants, AstLocations locations, Ident arrayId, Expr index,
        Expr value) implements Stmt {
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
     * @param arrayId le nom de la variable tableau.
     * @param index   l'indice de l'élément accédé.
     * @param value   la nouvelle valeur.
     * @return le nouveau nœud.
     */
    public static StmtArrayAssign create(final Ident arrayId, final Expr index, final Expr value) {
        return new StmtArrayAssign(StmtArrayAssign.class.getSimpleName(), Arrays.asList(arrayId, index, value),
                new AstLocations(), arrayId, index, value);
    }
}
