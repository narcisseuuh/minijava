package phase.c_semantic;
import java.util.Collection;

import compil.util.Debug;
import phase.b_syntax.ast.*;
import phase.c_semantic.symtab.InfoKlass;
import phase.c_semantic.symtab.InfoVar;
import phase.c_semantic.symtab.Scope;

/**
 * Vérification de l'utilisation des identificateurs. <br>
 * Identificateurs non définis (classe, méthode, variable). <br>
 * Variables non utilisées.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class VarUndefUnused extends AstVisitorDefault {
	/**
	 * La structure de données de l'analyse sémantique.
	 */
	protected final SemanticTree semanticTree;
	/**
	 * La liste des variables non utilisées. Construction par élimination des
	 * variables utilisées.
	 */
	protected Collection<InfoVar> unused;
	/**
	 * {@code true} si des erreurs sémantiques d'identificateurs non définis.
	 */
	protected boolean error;

	/**
	 * Construit la vérification des identificateurs.
	 * 
	 * @param semanticTree l'arbre sémantique.
	 */
	public VarUndefUnused(final SemanticTree semanticTree) {
		this.error = false;
		this.semanticTree = semanticTree;
		this.unused = semanticTree.rootScope().getAllVariables();
	}

	/**
	 * Réalise la validation des identificateurs.
	 * 
	 * @return {@code true} si des erreurs sémantiques de définition.
	 */
	public boolean execute() {
		Debug.log("= Contrôle de définitions : identificateurs non définis");
		semanticTree.axiom().accept(this);
		if (Debug.UNUSED) {
			Debug.log("= Contrôle de définitions : variables inutilisées");
			Debug.log(unused);
		}
		return this.error;
	}

	// Helpers
	/**
	 * Retourne l'attribut "Scope".
	 * 
	 * @param n le nœud de l'AST.
	 * @return la valeur de la portée du nœud.
	 */
	protected Scope getScope(final AstNode n) {
		return semanticTree.attrScope().get(n);
	}

	/**
	 * Retourne l'attribut "Type".
	 * 
	 * @param n le nœud de l'AST.
	 * @return la valeur de Type du nœud.
	 */
	protected String getType(final AstNode n) {
		return semanticTree.attrType().get(n);
	}

	/**
	 * Recherche une classe dans la table des symboles.
	 * 
	 * @param name le nom de la classe.
	 * @return la définition de la classe ou {@code null} si inconnue.
	 */
	protected InfoKlass lookupKlass(final String name) {
		return semanticTree.rootScope().lookupKlass(name);
	}

	/**
	 * Signale une erreur avec message et localisation dans l'AST. Erreur signalée
	 * avec traitement par exception retardé en fin d'analyse.
	 * 
	 * @param where le nœud de l'AST en faute.
	 * @param name  l'identificateur inconnu.
	 */
	protected void undefError(final AstNode where, final String name) {
		Debug.log(where + " Use of undefined identifier " + name);
		error = true;
	}

	/////////////////// Visit ////////////////////
    @Override
    public void visit(final ExprIdent n) {
        
        final InfoVar iv = getScope(n).lookupVariable(n.varId().name());
        if (iv == null)
            undefError(n, n.varId().name());
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtAssign n) {
        
        final InfoVar iv = getScope(n).lookupVariable(n.label());
        this.unused.remove(iv);
        defaultVisit(n);
    }

    @Override
    public void visit(final StmtArrayAssign n) {
        
        final InfoVar iv = getScope(n).lookupVariable(n.arrayId().name());
        this.unused.remove(iv);
        defaultVisit(n);
    }

    @Override
    public void visit(final ExprCall n) {
        
        final InfoVar iv = getScope(n).lookupVariable(n.label());
        this.unused.remove(iv);
        defaultVisit(n);
    }
}
