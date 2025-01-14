package phase.c_semantic;

import phase.b_syntax.ast.AstNode;
import phase.b_syntax.ast.Axiom;
import phase.c_semantic.symtab.InfoKlass;
import phase.c_semantic.symtab.Scope;

/**
 * L'Arbre Sémantique : AST + Table de Symboles + Attributs Sémantiques des
 * nœuds.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param axiom     La racine de l'AST.
 * @param rootScope La racine de la table des symboles. Fournit aussi le point
 *                  d'entrée de la recherche dans la table des classes.
 * @param attrKlass L'attribut hérité "Klass" : la classe englobante d'un nœud
 *                  de l'AST. Fournit aussi le type de la "variable"
 *                  {@code this}.
 * @param attrScope L'attribut hérité "Scope" : la portée courante d'un nœud de
 *                  l'AST. Fournit le point d'entrée de la recherche dans la
 *                  table des symboles.
 * @param attrType  L'attribut synthétisé "Type" : le type de données d'un nœud
 *                  "Expression" de l'AST.
 */

public record SemanticTree(AstNode axiom, Scope rootScope, SemanticAttribute<InfoKlass> attrKlass,
        SemanticAttribute<Scope> attrScope, SemanticAttribute<String> attrType) {
    /**
     * Construit un nouvel arbre sémantique.
     * 
     * @param axiom l'axiom ou nœud racine de l'AST.
     */
    public SemanticTree(final Axiom axiom) {
        this(axiom, new Scope(null, "Root"), new SemanticAttribute<>(), new SemanticAttribute<>(),
                new SemanticAttribute<>());
    }
}
