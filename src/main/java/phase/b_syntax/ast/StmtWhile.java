package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Boucle While.
 * <ul>
 * <li>{@link #test}
 * <li>{@link #body}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param test      la condition.
 * @param body      le corps.
 */
public record StmtWhile(String label, List<AstNode> enfants, AstLocations locations, Expr test, Stmt body)
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
     * @param test la condition.
     * @param body le corps.
     * @return le nouveau nœud.
     */
    public static StmtWhile create(final Expr test, final Stmt body) {
        return new StmtWhile(StmtWhile.class.getSimpleName(), Arrays.asList(test, body), new AstLocations(), test,
                body);
    }
}
