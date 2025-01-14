package phase.b_syntax.ast;

import java.util.ArrayList;
import java.util.List;

import compil.EnumType;
import compil.util.AstLocations;

/**
 * Identificateur de type.
 * <ul>
 * <li>{@link #name}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param name      le nom du type.
 */
public record Type(String label, List<AstNode> enfants, AstLocations locations, String name) implements AstNode {

    @Override
    public String toString() {
        return print() + " " + this.name;
    }

    @Override
    public void accept(final AstVisitor v) {
        v.visit(this);
    }

    /**
     * construit un enregistrement avec des valeurs par défaut pour les premiers
     * attributs (label, etc.). Cette méthode est utilisée dans CUP.
     * 
     * @param name le nom du type.
     * @return le nouveau nœud.
     */
    public static Type create(final String name) {
        return new Type(Type.class.getSimpleName(), new ArrayList<>(), new AstLocations(), name);
    }

    /**
     * construit un enregistrement avec des valeurs par défaut pour les premiers
     * attributs (label, etc.). Cette méthode est utilisée dans CUP.
     * 
     * @param t l'énumérateur du type.
     * @return le nouveau nœud.
     */
    public static Type create(final EnumType t) {
        return new Type(Type.class.getSimpleName(), new ArrayList<>(), new AstLocations(), t.toString());
    }
}
