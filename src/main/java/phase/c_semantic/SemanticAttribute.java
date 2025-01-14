package phase.c_semantic;

import java.util.HashMap;
import java.util.Map;

import phase.b_syntax.ast.AstNode;

/**
 * Décoration de l'AST avec des attributs sémantiques.
 * 
 * <p>
 * Utilise une structure {@code Map<ASTNode, R>} pour décorer l'AST sans
 * modifier l'AST existant.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param <R> le type de l'attribut.
 */
public class SemanticAttribute<R> {
    /**
     * L'attribut sémantique.
     */
    private final Map<AstNode, R> attribut;

    /**
     * Construit un nouvel attribut.
     */
    public SemanticAttribute() {
        this.attribut = new HashMap<>();
    }

    /**
     * Retourne la valeur de l'attribut d'un nœud.
     * 
     * @param n le nœud.
     * @return la valeur ou {@code null} si absent.
     */
    public R get(final AstNode n) {
        return this.attribut.get(n);
    }

    /**
     * Positionne la valeur d'un attribut à un nœud.
     * 
     * @param n    le nœud.
     * @param attr la valeur de l'attribut.
     */
    public void set(final AstNode n, final R attr) {
        this.attribut.put(n, attr);
    }
}
