package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Programme Minijava.
 * <ul>
 * <li>{@link #klassMain}
 * <li>{@link #klassList}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param klassMain la classe conventionnelle {@code main}.
 * @param klassList les autres classes.
 */
public record Axiom(String label, List<AstNode> enfants, AstLocations locations, KlassMain klassMain,
        AstList<Klass> klassList) implements AstNode {

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
     * @param klassMain la classe conventionnelle {@code main}.
     * @param klassList les autres classes.
     * @return le nouveau nœud.
     */
    public static Axiom create(final KlassMain klassMain, final AstList<Klass> klassList) {
        return new Axiom(Axiom.class.getSimpleName(), Arrays.asList(klassMain, klassList), new AstLocations(),
                klassMain, klassList);
    }
}
