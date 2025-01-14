package phase.c_semantic.symtab;

/**
 * Déclaration de méthode pour la table de symboles. <br>
 * La portée du corps de la méthode est fille de la portée des arguments qui est
 * fille de la portée de la déclaration de méthode. <br>
 * NB : les paramètres formels sont dupliqués comme une liste ordonnée dans
 * {@code InfoMethod} et comme une collection non ordonnée dans la portée des
 * arguments. <br>
 * <br>
 * Insertion d'une méthode :
 * <ul>
 * <li>Ajout de la méthode : {@code scope.add(new InfoMethod);}
 * <li>Entrée dans une nouvelle portée : {@code scope=new Scope(scope);}
 * <li>Ajout des paramètres formels: {@code scope.add(new InfoVar(...);}
 * <li>Entrée dans une nouvelle portée : {@code scope=new Scope(scope);}
 * <li>Et mise à jour de la portée : {@code InfoMethod.setScope(scope);}
 * </ul>
 */
public class InfoMethod implements Info {
	/**
	 * Le type de retour de la méthode.
	 */
	private final String returnType;
	/**
	 * Le nom de la méthode.
	 */
	private final String name;
	/**
	 * La liste des paramètres formels. NB : {@code args[0] = "this"}.
	 */
	private final InfoVar[] formals;
	/**
	 * Le lien vers la portée du corps de la méthode.
	 */
	private Scope scope;

	/**
	 * Construit une déclaration de méthode.
	 * 
	 * @param returnType le type de retour de la méthode.
	 * @param name       le nom de la méthode.
	 * @param formals    la liste des paramètres formels.
	 */
	public InfoMethod(final String returnType, final String name, final InfoVar... formals) {
		this.returnType = returnType;
		this.name = name;
		this.formals = formals;
		this.scope = null;
	}

	/**
	 * Retourne le type de retour.
	 * 
	 * @return le type de retour.
	 */
	public String getReturnType() {
		return this.returnType;
	}

	/**
	 * Retourne le nom.
	 * 
	 * @return le nom de la méthode.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retourne les paramètres formels.
	 * 
	 * @return les paramètres de la méthode.
	 */
	public InfoVar[] getArgs() {
		return this.formals;
	}

	/**
	 * Retourne la portée du corps de la méthode.
	 * 
	 * @return la portée de la méthode.
	 */
	public Scope getScope() {
		return this.scope;
	}

	/**
	 * Positionne la portée du corps de la méthode.
	 * 
	 * @param sc la portée de la méthode.
	 */
	public void setScope(final Scope sc) {
		this.scope = sc;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.returnType).append(' ').append(this.name);
		sb.append('(');
		if (this.formals.length != 0) {
			sb.append(this.formals[0]);
		}
		for (int i = 1; i < this.formals.length; i++) {
			sb.append(", ").append(this.formals[i]);
		}
		sb.append(')');
		return sb.toString();
	}

	/**
	 * Retourne un nom canonique pour la méthode. (pas utilisé)
	 * 
	 * @return un nom canonique de la méthode.
	 */
	public String getCanonicalName() {
		final StringBuilder sb = new StringBuilder();
		sb.append("__").append(this.name);
		for (InfoVar v : this.formals) {
			sb.append('_').append(v.type());
		}
		return sb.toString();
	}
}
