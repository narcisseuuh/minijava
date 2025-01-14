package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Utilisation de Variable.
 * <ul>
 * <li>{@link #varId}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param varId     le nom de la variable.
 */
public record ExprIdent(String label, List<AstNode> enfants, AstLocations locations, Ident varId) implements Expr {
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
     * @param varId le nom de la variable.
     * @return le nouveau nœud.
     */
    public static ExprIdent create(final Ident varId) {
        return new ExprIdent(ExprIdent.class.getSimpleName(), Arrays.asList(varId), new AstLocations(), varId);
    }
}
