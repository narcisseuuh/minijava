package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Instruction if_then_else.
 * <ul>
 * <li>{@link #test}
 * <li>{@link #ifTrue}
 * <li>{@link #ifFalse}
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
 * @param ifTrue    le cas vrai.
 * @param ifFalse   le cas faux.
 */
public record StmtIf(String label, List<AstNode> enfants, AstLocations locations, Expr test, Stmt ifTrue, Stmt ifFalse)
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
     * @param test    la condition.
     * @param ifTrue  le cas vrai.
     * @param ifFalse le cas faux.
     * @return le nouveau nœud.
     */
    public static StmtIf create(final Expr test, final Stmt ifTrue, final Stmt ifFalse) {
        return new StmtIf(StmtIf.class.getSimpleName(), Arrays.asList(test, ifTrue, ifFalse), new AstLocations(), test,
                ifTrue, ifFalse);
    }
}
