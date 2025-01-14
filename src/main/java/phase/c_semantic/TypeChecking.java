package phase.c_semantic;

import compil.util.Debug;
import phase.b_syntax.ast.AstNode;
import phase.b_syntax.ast.AstVisitorDefault;
import phase.c_semantic.symtab.InfoKlass;
import phase.c_semantic.symtab.InfoVar;
import phase.c_semantic.symtab.Scope;

/**
 * Contrôle de Type.
 * <ul>
 * <li>Calcule l'attribut synthétisé "Type" pour les nœuds Expressions,
 * <li>Vérifie les contraintes de typage de Minijava.
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class TypeChecking extends AstVisitorDefault {
	/**
	 * Le nom de type booléen.
	 */
	protected static final String BOOL = compil.EnumType.BOOL.toString();
	/**
	 * Le nom de type entier.
	 */
	protected static final String INT = compil.EnumType.INT.toString();
	/**
	 * Le nom de type tableau d'entier.
	 */
	protected static final String INT_ARRAY = compil.EnumType.INT_ARRAY.toString();
	/**
	 * Le nom de type tableau de booléen.
	 */
	protected static final String BOOL_ARRAY = compil.EnumType.BOOL_ARRAY.toString();
	/**
	 * Le nom de type indéfini.
	 */
	protected static final String VOID = compil.EnumType.UNDEF.toString();
	/**
	 * La structure de données de l'analyse sémantique.
	 */
	protected final SemanticTree semanticTree;
	/**
	 * {@code true} si des erreurs de typage détectées.
	 */
	protected boolean error;

	/**
	 * Construit le contrôle de type.
	 * 
	 * @param semanticTree l'arbre sémantique.
	 */
	public TypeChecking(final SemanticTree semanticTree) {
		this.error = false;
		this.semanticTree = semanticTree;
	}

	/**
	 * Réalise le contrôle de type.
	 * 
	 * @return {@code true} si des erreurs sémantiques de typage.
	 */
	public boolean execute() {
		Debug.log("= Contrôle de typage");
		semanticTree.axiom().accept(this);
		return error;
	}

	// Helpers
	/**
	 * Retourne l'attribut Type.
	 * 
	 * @param n le nœud de l'AST.
	 * @return le nom de type.
	 */
	protected String getType(final AstNode n) {
		return semanticTree.attrType().get(n);
	}

	/**
	 * Positionne l'attribut Type.
	 * 
	 * @param n    le nœud de l'AST.
	 * @param type le nom de type.
	 */
	protected void setType(final AstNode n, final String type) {
		semanticTree.attrType().set(n, type);
	}

	/**
	 * Retourne l'attribut Scope.
	 * 
	 * @param n le nœud de l'AST.
	 * @return la portée courante du nœud.
	 */
	protected Scope getScope(final AstNode n) {
		return semanticTree.attrScope().get(n);
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
	 * avec traitement par exception retardé en fin d'analyse
	 * 
	 * @param where le nœud de l'AST en faute.
	 * @param msg   le message d'erreur.
	 */
	protected void erreur(final AstNode where, final String msg) {
		compil.util.Debug.logErr(where + " " + msg);
		error = true;
	}

	/**
	 * Teste le transtypage implicite entre 2 types.
	 * 
	 * @param t1 le nom de type 1.
	 * @param t2 le nom de type 2.
	 * @return {@code true} si t2 est sous-type de t1.
	 */
	protected boolean compareType(final String t1, final String t2) {
		if (t2 == null) {
			return false;
		}
		if (t2.equals(t1)) {
			return true;
		}
		// sinon (t1 ancêtre de t2) ?
		final InfoKlass kl2 = lookupKlass(t2);
		if (kl2 != null) {
			return compareType(t1, kl2.getParent());
		}
		return false;
	}

	/**
	 * Valide une condition de typage et signale l'erreur éventuelle.
	 * 
	 * @param t1    le type attendu.
	 * @param t2    le type testé.
	 * @param msg   le message d'erreur si t2 ne cast pas en t1.
	 * @param where le nœud de l'AST en faute.
	 */
	protected void checkType(final String t1, final String t2, final String msg, final AstNode where) {
		if (!compareType(t1, t2)) {
			erreur(where, "Wrong Type : " + t2 + "->" + t1 + ";  " + msg);
		}
	}

	/**
	 * Valide un nom de type et signale l'erreur éventuelle.
	 * 
	 * @param type  le nom de type testé.
	 * @param where le nœud de l'AST en faute.
	 */
	protected void checkTypeName(final String type, final AstNode where) {
		if (type.equals(BOOL) || type.equals(INT)) {
			return;
		}
		if (type.equals(BOOL_ARRAY) || type.equals(INT_ARRAY)) {
			return;
		}
		if (type.equals(VOID)) {
			return;
		}
		if (lookupKlass(type) != null) {
			return;
		}
		erreur(where, "Unknown Type : " + type);
	}

	/**
	 * Recherche le type d'une variable dans la table des symboles.
	 * 
	 * @param n    le nœud de l'AST (pour obtenir la portée courante).
	 * @param name le nom de la variable.
	 * @return le nom de type, {@code VOID} pour type inconnu.
	 */
	protected String lookupVarType(final AstNode n, final String name) {
		final InfoVar v = getScope(n).lookupVariable(name);
		if (v == null) {
			return VOID;
		}
		return v.type();
	}

	/////////////////// Visit ////////////////////
	// Visites spécifiques :
	// - Expr* : Positionner l'attribut Type
	// - Stmt* + Expr* (sauf exceptions) : Compatibilité des Types
	// - Type : Validité des noms de type dans Variable, Method, Formal
	// - Method : returnType compatible avec Type(returnExpr)
}
