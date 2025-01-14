package phase.c_semantic.symtab;

/**
 * Déclaration d'une variable pour la table de symboles. Suivant la portée une
 * variable est :
 * <ul>
 * <li>un champs de classe
 * <li>un paramètre formel de méthode
 * <li>une variable locale de méthode
 * <li>une variable locale de bloc
 * </ul>
 */
public class InfoVar implements Info, phase.d_intermediate.ir.IRVariable {
	/**
	 * Le nom de la variable.
	 */
	private final String name;
	/**
	 * Le nom du type de la variable.
	 */
	private final String type;

	/**
	 * Construit une déclaration de variable.
	 * 
	 * @param name le nom de la variable.
	 * @param type le type de la variable.
	 */
	public InfoVar(final String name, final String type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String type() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.type + " " + this.name;
	}
}
