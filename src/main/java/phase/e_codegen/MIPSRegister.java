package phase.e_codegen;

/**
 * Énumération des 32 registres MIPS avec noms textuels.
 * 
 * @author Pascal Hennequin
 */
public enum MIPSRegister {
	/**
	 * Toujours à 0.
	 */
	ZERO("$zero", 0),
	/**
	 * Global pointer : variables globales.
	 */
	GP("$gp", 28),
	/**
	 * Stack pointer : la pile .
	 */
	SP("$sp", 29),
	/**
	 * Frame pointer : variables locales. Sauvegardé par l'appelé.
	 */
	FP("$fp", 30),
	/**
	 * Adresse de retour. Sauvegardé par l'appelé.
	 */
	RA("$ra", 31),
	/**
	 * Stockage des résultats de fonction.
	 */
	V0("$v0", 2),
	/**
	 * Stockage des résultats de fonction.
	 */
	V1("$v1", 3),
	/**
	 * Stockage des arguments de fonction. Sauvegardés par l'appelant, A0=this.
	 */
	A0("$a0", 4),
	/**
	 * Stockage des arguments de fonction. Sauvegardés par l'appelant.
	 */
	A1("$a1", 5),
	/**
	 * Stockage des arguments de fonction. Sauvegardés par l'appelant.
	 */
	A2("$a2", 6),
	/**
	 * Stockage des arguments de fonction. Sauvegardés par l'appelant.
	 */
	A3("$a3", 7),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T0("$t0", 8),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T1("$t1", 9),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T2("$t2", 10),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T3("$t3", 11),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T4("$t4", 12),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T5("$t5", 13),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T6("$t6", 14),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T7("$t7", 15),
	/**
	 * Registre temporaire. Sauvegardés par l'appelant.
	 */
	T8("$t8", 24),
	/**
	 * Registre temporaire. Sauvegardés par appelant.
	 */
	T9("$t9", 25),

	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S0("$s0", 16),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S1("$s1", 17),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S2("$s2", 18),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S3("$s3", 19),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S4("$s4", 20),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S5("$s5", 21),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S6("$s6", 22),
	/**
	 * Registre temporaire. Sauvegardé par l'appelé.
	 */
	S7("$s7", 23),
	/**
	 * Registre réservé au système.
	 */
	K0("$k0", 26),
	/**
	 * Registre réservé au système.
	 */
	K1("$k1", 27),
	/**
	 * Registre réservé à l'assembleur.
	 */
	AT("$at", 1);

	/**
	 * Le nom du registre.
	 */
	private final String name;

	/**
	 * Le numéro du registre.
	 */
	private final int numero;

	/**
	 * Constructeur privé pour nommage des registres.
	 * 
	 * @param name   le nom MIPS.
	 * @param numero le numéro MIPS.
	 */
	MIPSRegister(final String name, final int numero) {
		this.name = name;
		this.numero = numero;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Alternative de {@code toString()} avec numéros de registre.
	 * 
	 * @return le nom MIPS avec numéro.
	 */
	public String toStringBis() {
		return "$" + this.numero;
	}
}
