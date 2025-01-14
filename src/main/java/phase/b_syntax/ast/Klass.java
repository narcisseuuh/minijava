package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Déclaration de classe.
 * <ul>
 * <li>{@link #klassId}
 * <li>{@link #parentId}
 * <li>{@link #vars}
 * <li>{@link #methods}
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
 * @param parentId  le nom de la classe parente (par défaut {@code Object}).
 * @param vars      la liste des attributs de la classe.
 * @param methods   la liste de méthodes.
 */
public record Klass(String label, List<AstNode> enfants, AstLocations locations, Ident klassId, Ident parentId,
        AstList<Variable> vars, AstList<Method> methods) implements AstNode {
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
     * @param klassId  le nom de la classe.
     * @param parentId le nom de la classe parente (par défaut {@code Object}).
     * @param vars     la liste des attributs de la classe.
     * @param methods  la liste de méthodes.
     * @return le nouveau nœud.
     */
    public static Klass create(final Ident klassId, final Ident parentId, final AstList<Variable> vars,
            final AstList<Method> methods) {
        return new Klass(Klass.class.getSimpleName(), Arrays.asList(klassId, parentId, vars, methods),
                new AstLocations(), klassId, parentId, vars, methods);
    }
}
