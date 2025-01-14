package phase.c_semantic.symtab;

import java.util.Collection;

/**
 * Déclaration de classe pour la table de symboles. <br>
 * Insertion d'une classe :
 * <ul>
 * <li>Ajout de la classe : {@code scope.add(new InfoKlass(..));}
 * <li>Entrée dans une nouvelle portée : {@code scope=new Scope(scope);}
 * <li>Et mise a jour de la portée : {@code InfoKlass.setScope(scope);}
 * </ul>
 */
public class InfoKlass implements Info {
	/**
	 * Le nom de la classe.
	 */
	private final String name;
	/**
	 * Le nom de la classe parente.
	 */
	private final String parent;
	/**
	 * Le lien vers la portée de la classe. Cette portée contient les attributs de
	 * la classe (méthodes, champs).
	 */
	private Scope scope;

	/**
	 * Construit une déclaration de classe.
	 * 
	 * @param name       le nom de la classe.
	 * @param parentName le nom de la classe mère.
	 */
	public InfoKlass(final String name, final String parentName) {
		this.name = name;
		this.parent = parentName;
		this.scope = null;
	}

	/**
	 * Retourne le nom de la classe.
	 * 
	 * @return le nom de la classe.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retourne le nom de la classe parente.
	 * 
	 * @return le nom de la classe parente.
	 */
	public String getParent() {
		return this.parent;
	}

	/**
	 * Retourne la portée de la classe.
	 * 
	 * @return la portée des attributs de la classe.
	 */
	public Scope getScope() {
		return this.scope;
	}

	/**
	 * Positionne la portée de la classe.
	 * 
	 * @param scope la portée des attributs de la classe.
	 */
	public void setScope(final Scope scope) {
		this.scope = scope;
	}

	/**
	 * Retourne les méthodes de la classe.
	 * 
	 * @return les méthodes de la classe.
	 */
	public Collection<InfoMethod> getMethods() {
		return this.scope.getMethods();
	}

	/**
	 * Retourne les champs de la classe.
	 * 
	 * @return les champs de la classe.
	 */
	public Collection<InfoVar> getFields() {
		return this.scope.getVariables();
	}

	@Override
	public String toString() {
		return "class " + this.name + " extends " + this.parent;
	}
}
