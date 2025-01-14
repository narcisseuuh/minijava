package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Déclaration de variables.
 * <ul>
 * <li>{@link #typeId}
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
 * @param typeId    le nom du type de la variable.
 * @param varId     le nom de la variable.
 */
public record Variable(String label, List<AstNode> enfants, AstLocations locations, Type typeId, Ident varId)
        implements AstNode {
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
     * @param typeId le nom du type de la variable.
     * @param varId  le nom de la variable.
     * @return le nouveau nœud.
     */
    public static Variable create(final Type typeId, final Ident varId) {
        return new Variable(Variable.class.getSimpleName(), Arrays.asList(typeId, varId), new AstLocations(), typeId,
                varId);
    }
}
