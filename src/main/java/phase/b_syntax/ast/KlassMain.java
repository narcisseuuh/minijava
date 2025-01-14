package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Déclaration de la classe main().
 * <ul>
 * <li>{@link #klassId}
 * <li>{@link #argId}
 * <li>{@link #stmt}
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
 * @param argId     le nom de l'argument (pas utilisé).
 * @param stmt      l'instruction (unique) de {@code main}.
 */
public record KlassMain(String label, List<AstNode> enfants, AstLocations locations, Ident klassId, Ident argId,
        Stmt stmt) implements AstNode {
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
     * @param klassId le nom de la classe.
     * @param argId   le nom de l'argument (pas utilisé).
     * @param stmt    l'instruction (unique) de {@code main}.
     * @return le nouveau nœud.
     */
    public static KlassMain create(final Ident klassId, final Ident argId, final Stmt stmt) {
        return new KlassMain(KlassMain.class.getSimpleName(), Arrays.asList(klassId, argId, stmt), new AstLocations(),
                klassId, argId, stmt);
    }
}
