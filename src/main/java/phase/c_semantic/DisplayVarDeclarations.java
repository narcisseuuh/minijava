package phase.c_semantic;

import compil.util.Debug;
import compil.util.IndentWriter;
import phase.b_syntax.ast.AstVisitorDefault;

/**
 * Premier exemple de Visiteur : détection des déclarations de variable.
 * 
 * @author Denis Conan
 */
public class DisplayVarDeclarations extends AstVisitorDefault {
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
    public DisplayVarDeclarations(final SemanticTree semTree) {
        out.print("= Affichage des Identificateurs : ");
        insideMethod = false;
        semTree.axiom().accept(this);
        Debug.log(out);
    }

    /////////////////// Visit ////////////////////
}
