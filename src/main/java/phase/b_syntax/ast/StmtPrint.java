package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Impression d'une valeur entière.
 * <ul>
 * <li>{@link #expr}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param expr      la valeur.
 */
public record StmtPrint(String label, List<AstNode> enfants, AstLocations locations, Expr expr) implements Stmt {
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
     * @param expr la valeur.
     * @return le nouveau nœud.
     */
    public static StmtPrint create(final Expr expr) {
        return new StmtPrint(StmtPrint.class.getSimpleName(), Arrays.asList(expr), new AstLocations(), expr);
    }
}
