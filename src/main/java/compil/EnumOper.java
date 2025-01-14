package compil;

/**
 * Énumération des opérateurs Binaires et Unaires, avec nom textuel.
 * 
 * @author Pascal Hennequin
 */
public enum EnumOper {
	/**
	 * L'opérateur binaire Addition.
	 */
	PLUS("+"),
	/**
	 * L'opérateur binaire Soustraction.
	 */
	MINUS("-"),
	/**
	 * L'opérateur binaire Multiplication.
	 */
	TIMES("*"),
	/**
	 * L'opérateur binaire Et logique.
	 */
	AND("&&"),
	/**
	 * L'opérateur binaire Inférieur.
	 */
	LESS("<"),
	/**
	 * L'opérateur unaire Négation.
	 */
	NOT("!"),
	/**
	 * L'opérateur indéfini (inutile).
	 */
	UNDEF("undef");

	/**
	 * Le nom de l'opérateur pour debug.
	 */
	private final String name;

	/**
	 * Constructeur privé pour nommage des opérateurs.
	 * 
	 * @param name la notation usuelle de l'opérateur.
	 */
	EnumOper(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
