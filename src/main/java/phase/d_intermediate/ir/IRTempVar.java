package phase.d_intermediate.ir;

/**
 * Variable IR : Variable Temporaire.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param name Le nom unique des variables temporaires.
 * @param scope Le nom de la méthode courante. Utilisé comme portée de la variable.
 */
public record IRTempVar(String name, String scope) implements IRVariable {
	/**
	 * La numérotation des variables temporaires.
	 */
	private static int index;

	/**
	 * Construit une variable temporaire avec un nom unique auto-généré.
	 * 
	 * @param scope le nom de la méthode courante.
	 */
	public IRTempVar(final String scope) {
		this("t_" + index++, scope);
	}

	@Override
	public String type() {
		return "IRTemp";
	}

	/**
	 * Donne la portée de la variable.
	 * 
	 * @return scope la portée = nom de la méthode courante.
	 */
	public String getScope() {
		return this.scope;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
