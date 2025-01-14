package compil.util;

import java.io.PrintWriter;

/**
 * Options d'exécution du compilateur et gestion des impressions.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public final class Debug {
	/**
	 * Si {@code true}, impression des Tokens lus par le parser.
	 */
	public static final boolean TOKEN = false;
	/**
	 * Si {@code true}, impression de la trace d’exécution de l'automate LR.
	 */
	public static final boolean PARSE = false;
	/**
	 * Si {@code true}, impression de l'AST.
	 */
	public static final boolean TREE = true;
	/**
	 * Si {@code true}, impression des "Locations" dans l'AST.
	 */
	public static final boolean LOCATION = true;
	/**
	 * Si {@code true}, impression du programme Minijava par visite de l'AST.
	 */
	public static final boolean PRETTY = true;
	/**
	 * Si {@code true}, impression de la table des symboles.
	 */
	public static final boolean SYMTAB = true;
	/**
	 * Si {@code true}, impression des Variables non utilisées.
	 */
	public static final boolean UNUSED = true;
	/**
	 * Si {@code true}, impression de la forme intermédiaire.
	 */
	public static final boolean INTERMED = true;
	/**
	 * Si {@code true}, impression de l'allocation Mémoire.
	 */
	public static final boolean ALLOCATOR = true;
	/**
	 * Le flot d'impression standard.
	 */
	public static final PrintWriter PW = new PrintWriter(System.out, true);
	/**
	 * Le Flot d'impression en erreur.
	 */
	public static final PrintWriter PWERR = new PrintWriter(System.err, true);

	/**
	 * Si {@code true}, debug actif.
	 */
	private static boolean isLoggingOn = true;

	/**
	 * Constructeur interdit.
	 * 
	 * @throws IllegalStateException constructeur interdit.
	 */
	private Debug() {
		throw new IllegalStateException("Utility class");
	}
	
	/**
	 * Répond à la question est-ce que les <i>logs</i> sont activés.
	 * 
	 * @return {@code true} si <i>logging</i>.
	 */
	public static boolean isLoggingON() {
		return isLoggingOn;
	}

	/**
	 * Désactive le <i>log</i>.
	 */
	public static void noLogging() {
		isLoggingOn = false;
	}

	/**
	 * Imprime sur la sortie standard. Remplace {@code System.out.println()}.
	 * 
	 * @param o l'objet à imprimer.
	 */
	public static void log(final Object o) {
		if (isLoggingOn) {
			PW.println(o.toString());
		}
	}

	/**
	 * Imprime sur la sortie en erreur. Remplace {@code System.err.println()}.
	 * 
	 * @param o l'objet à imprimer.
	 */
	public static void logErr(final Object o) {
		PWERR.println(o.toString());
	}

	/**
	 * Lance une fin provisoire de compilation pour cause de travaux.
	 * 
	 * @throws CompilerException Fin provisoire de compilation.
	 */
	public static void toBeContinued() {
		final String s = new String(Character.toChars(0x1f6a7));
		PWERR.println(s + " Fin provisoire de Compilation");
	}
}
