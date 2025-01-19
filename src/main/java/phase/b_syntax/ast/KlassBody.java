package phase.b_syntax.ast;

import java.util.ArrayList;
import java.util.List;

import compil.util.AstLocations;

/**
 * Déclaration du corps d'une classe.
 * 
 * <ul>
 * <li>{@link #attributs}
 * <li>{@link #methodes}
 * </ul>
 * 
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param attributs les attributs ou variables de la classe.
 * @param methodes  les méthodes de la classe.
 */
public record KlassBody(String label, List<AstNode> enfants, AstLocations locations, AstList<Variable> attributs,
        AstList<Method> methodes) implements AstNode {

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
     * attributs (label, etc.) et avec des listes vides pour les attributs et les
     * méthodes. Cette méthode est utilisée dans CUP.
     * 
     * @return le nouveau nœud.
     */
    public static KlassBody create() {
        return new KlassBody(KlassBody.class.getSimpleName(), new ArrayList<>(), new AstLocations(), new AstList<>(),
                new AstList<>());
    }

    /* méthodes d'ajout de attributs/methodes pour le parsing */
    public void addAttribut(Variable attribut) {
        this.attributs.add(attribut);
    }

    public void addMethode(Method methode) {
        this.methodes.add(methode);
    }
}
