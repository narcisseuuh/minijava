package compil;

/**
 * Énumération des types primitifs avec nom textuel.
 * 
 * @author Pascal Hennequin
 */
public enum EnumType {
	/**
	 * Le type booléen.
	 */
	BOOL("boolean"),
	/**
	 * Le type entier.
	 */
	INT("int"),
	/**
	 * Le type tableau d'entiers.
	 */
	INT_ARRAY("int[]"),
	/**
	 * Le type tableau de booléens.
	 */
	BOOL_ARRAY("boolean[]"),
	/**
	 * Le type vide (inutile).
	 */
	VOID("void"),
	/**
	 * Le type inconnu (inutile).
	 */
	UNDEF("undef");

	/**
	 * Le nom de type.
	 */
	private final String name;

	/**
	 * Constructeur privé pour nommage des types.
	 * 
	 * @param name le nom JAVA du type.
	 */
	EnumType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
