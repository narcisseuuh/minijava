package phase.c_semantic;

import phase.b_syntax.ast.Axiom;

/**
 * Analyse Sémantique.
 * 
 * @author Pascal Hennequin
 */
public class Semantic {
    /**
     * La structure de données en sortie de l'analyse sémantique.
     */
    private final SemanticTree semanticTree;
    /**
     * {@code true} si des erreurs sémantiques détectées.
     */
    private boolean error;

    /**
     * Construit l'analyse sémantique.
     * 
     * @param axiom l'axiom ou nœud racine de l'AST.
     */
    public Semantic(final Axiom axiom) {
        this.error = false;
        this.semanticTree = new SemanticTree(axiom);
    }

    /**
     * Exécute l'analyse sémantique.
     * 
     * @return l'arbre sémantique : AST + Table de symbole + Attributs sémantiques.
     * @throws compil.util.CompilerException si sémantique invalide.
     */
    public SemanticTree execute() {
        // Premières visites pour s'entraîner
        new DisplayVarDeclarations(this.semanticTree);
        new DisplayScopes(this.semanticTree);
        new DisplayVarDeclarationsInScopes(this.semanticTree);
        // Construction de la table des symboles (passe 1)
        // Contrôle de la redéfinition dans une même portée
        // Calcul de l'attribut "Scope"
        error = new BuildSymTab(this.semanticTree).execute() || error;
        // Construction de la table des symboles (passe2)
        // Intégration de l'héritage JAVA dans l'arbre des portées
        // Contrôle de consistance de l'héritage JAVA (boucle, "Object",..)
        error = new CheckInheritance(semanticTree).execute() || error;
        // Contrôle de type et calcul de l'attribut "Type"
        error = new TypeChecking(semanticTree).execute() || error;
        // Contrôle des identificateurs non définis ou non utilisés
        error = new VarUndefUnused(semanticTree).execute() || error;
        // Fin de compilation sur erreurs sémantiques
        if (error) {
            throw new compil.util.CompilerException("Semantic Error(s)");
        }
        return semanticTree;
    }
}
