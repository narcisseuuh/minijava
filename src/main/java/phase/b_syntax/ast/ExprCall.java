package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Appel de méthode d'objet.
 * <ul>
 * <li>{@link #receiver}
 * <li>{@link #methodId}
 * <li>{@link #args}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param receiver  le receveur.
 * @param methodId  le nom de la méthode appelé.
 * @param args      la liste des arguments.
 */
public record ExprCall(String label, List<AstNode> enfants, AstLocations locations, Expr receiver, Ident methodId,
        AstList<? extends Expr> args) implements Expr {
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
     * @param receiver le receveur.
     * @param methodId le nom de la méthode appelé.
     * @param args     la liste des arguments.
     * @return le nouveau nœud.
     */
    public static ExprCall create(final Expr receiver, final Ident methodId, final AstList<? extends Expr> args) {
        return new ExprCall(ExprCall.class.getSimpleName(), Arrays.asList(receiver, methodId, args), new AstLocations(),
                receiver, methodId, args);
    }
}
