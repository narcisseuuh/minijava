package phase.b_syntax.ast;

import java.util.ArrayList;
import java.util.List;

import compil.util.AstLocations;

/**
 * Déclaration du corps d'une méthode.
 * 
 * <ul>
 * <li>{@link #vars}
 * <li>{@link #instructions}
 * </ul>
 * 
 * @author Denis Conan
 * 
 * @param label        le label du nœud.
 * @param enfants      les enfants du nœud.
 * @param locations    les positions de début et de fin du nœud dans le fichier
 *                     source.
 * @param vars         les variables de la méthode.
 * @param instructions les instructions de la méthode.
 */
public record MethodBody(String label, List<AstNode> enfants, AstLocations locations, AstList<Variable> vars,
        AstList<Stmt> instructions) implements AstNode {

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
     * attributs (label, etc.) et avec des listes vides pour les variables et les
     * instructions. Cette méthode est utilisée dans CUP.
     * 
     * @return le nouveau nœud.
     */
    public static MethodBody create() {
        return new MethodBody(MethodBody.class.getSimpleName(), new ArrayList<>(), new AstLocations(), new AstList<>(),
                new AstList<>());
    }

    /* méthodes d'ajout de variables/instructions pour le parsing */
    public void addVar(Variable attribut) {
        this.vars.add(attribut);
    }

    public void addInstruction(Stmt instruction) {
        this.instructions.add(instruction);
    }
}
