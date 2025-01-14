package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Affectation d'une variable.
 * <ul>
 * <li>{@link #varId}
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
 * @param varId     le nom de la variable.
 * @param value     la nouvelle valeur.
 */
public record StmtAssign(String label, List<AstNode> enfants, AstLocations locations, Ident varId, Expr value)
        implements Stmt {
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
     * @param value la nouvelle valeur.
     * @return le nouveau nœud.
     */
    public static StmtAssign create(final Ident varId, final Expr value) {
        return new StmtAssign(StmtAssign.class.getSimpleName(), Arrays.asList(varId, value), new AstLocations(), varId,
                value);
    }
}
