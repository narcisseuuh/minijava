package phase.b_syntax.ast;

import java.util.Arrays;
import java.util.List;

import compil.util.AstLocations;

/**
 * Bloc d'instructions.
 * <ul>
 * <li>{@link #vars}
 * <li>{@link #stmts}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param label     le label du nœud.
 * @param enfants   les enfants du nœud.
 * @param locations les positions de début et de fin du nœud dans le fichier
 *                  source.
 * @param vars      la liste des variables locales.
 * @param stmts     la liste des instructions.
 */
public record StmtBlock(String label, List<AstNode> enfants, AstLocations locations, AstList<Variable> vars,
        AstList<Stmt> stmts) implements Stmt {
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
     * @param vars  la liste des variables locales.
     * @param stmts la liste des instructions.
     * @return le nouveau nœud.
     */
    public static StmtBlock create(final AstList<Variable> vars, final AstList<Stmt> stmts) {
        return new StmtBlock(StmtBlock.class.getSimpleName(), Arrays.asList(vars, stmts), new AstLocations(), vars,
                stmts);
    }
}
