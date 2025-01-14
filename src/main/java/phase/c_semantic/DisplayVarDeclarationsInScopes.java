package phase.c_semantic;

import compil.util.Debug;
import compil.util.IndentWriter;
import phase.b_syntax.ast.AstVisitorDefault;

/**
 * Premier exemple de Visiteur : détection des déclarations de variable dans
 * leur portée.
 * 
 * @author Denis Conan
 */
public class DisplayVarDeclarationsInScopes extends AstVisitorDefault {
    /**
     * Le Writer pour impression.
     */
    protected final IndentWriter out = new IndentWriter();
    /**
     * L'attribut servant à repérer lorsque l'on est à l'intérieur d'une méthode.
     */
    protected boolean insideMethod;

    /**
     * Constructeur.
     * 
     * @param semTree l'arbre sémantique.
     */
    public DisplayVarDeclarationsInScopes(final SemanticTree semTree) {
        out.print("= Affichage des identificateurs dans leur portée : ");
        insideMethod = false;
        semTree.axiom().accept(this);
        Debug.log(out);
    }

    /////////////////// Visit ////////////////////
}