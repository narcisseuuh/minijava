package phase.c_semantic;

import compil.util.Debug;
import compil.util.IndentWriter;
import phase.b_syntax.ast.AstVisitorDefault;

/**
 * Deuxième exemple de Visiteur : détection des portées (scopes).
 * 
 * @author Denis Conan
 */
public class DisplayScopes extends AstVisitorDefault {
    /**
     * Le Writer pour impression.
     */
    protected final IndentWriter out = new IndentWriter();

    /**
     * Constructeur.
     * 
     * @param semTree l'arbre sémantique.
     */
    public DisplayScopes(final SemanticTree semTree) {
        out.print("= Affichage des portées : ");
        semTree.axiom().accept(this);
        Debug.log(out);
   }

    /////////////////// Visit ////////////////////
}
