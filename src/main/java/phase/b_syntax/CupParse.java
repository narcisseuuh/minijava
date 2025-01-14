package phase.b_syntax;

import compil.util.Debug;

/**
 * Analyseur Syntaxique.
 * <ul>
 * <li>Encapsule les classes {@code Yylex} et {@code parser} de CUP/JFlex
 * <li>Méthode {@link #main} pour l'analyse en ligne de commande
 * </ul>
 * 
 * @author Pascal Hennequin
 */
public class CupParse {
	/**
	 * L'impression des messages en sortie.
	 */
	private java.io.PrintWriter pw;
	/**
	 * La définition des Tokens partagés entre JFlex et CUP.
	 */
	private java_cup.runtime.SymbolFactory csf;
	/**
	 * La capture de Tokens échangés entre JFlex et CUP.
	 */
	private java_cup.runtime.ScannerBuffer scanBuffer;
	/**
	 * L'analyseur lexical JFlex.
	 */
	private java_cup.runtime.Scanner lexer;
	/**
	 * L'analyseur syntaxique CUP.
	 */
	private java_cup.runtime.lr_parser parser;

	/**
	 * Construit l'analyseur syntaxique CUP/JFlex.
	 * 
	 * @param file le fichier analysé, {@code "stdin"} pour l'entrée standard.
	 * @param pw   un "Writer" pour les impressions.
	 */
	public CupParse(final String file, final java.io.PrintWriter pw) {
		this.pw = pw;
		// redirection de l'entrée standard
		if ("stdin".equals(file) || "-".equals(file)) {
			if (Debug.isLoggingON()) {
				pw.println("Reading standard input");
			}
		} else {
			String input = Thread.currentThread().getContextClassLoader().getResource(file).getPath();
			try {
				System.setIn(new java.io.FileInputStream(input));
				if (Debug.isLoggingON()) {
					pw.println("Reading file " + input);
				}
			} catch (java.io.FileNotFoundException e) {
				if (Debug.isLoggingON()) {
					pw.println("File " + input + " not found");
					pw.println("Reading standard input");
				}
			}
		}
		// création du lexer et du parser
		this.csf = new java_cup.runtime.ComplexSymbolFactory();
		this.lexer = new Yylex(new java.io.InputStreamReader(System.in), this.csf);
		this.scanBuffer = new java_cup.runtime.ScannerBuffer(this.lexer);
		this.parser = new parser(this.scanBuffer, this.csf);
	}

	/**
	 * Exécution de l'analyse et validation de la valeur de retour.
	 * 
	 * @param debugToken {@code true} pour impression des tokens lus.
	 * @param debugParse {@code true} pour trace d’exécution de l'automate LR.
	 * @return la valeur du symbole axiome de la grammaire.
	 */
	public Object run(final boolean debugToken, final boolean debugParse) {
		Object axiom = null;
		final java_cup.runtime.Symbol result;
		try {
			result = debugParse ? this.parser.debug_parse() : this.parser.parse();
			if (result == null || result.sym != 0) {
				this.pw.println("Parsing ends with symbol: " + result);
			} else {
				axiom = result.value;
				this.pw.println("Parsing OK, Axiom = " + axiom);
			}
		} // CHECKSTYLE:OFF
			// catch pas propre de l'Exception de lr_parser.report_fatal_error()!!
		catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			this.pw.println(e.getMessage());
		}
		// CHECKSTYLE:ON
		finally {
			if (debugToken) {
				this.pw.println("= Token Debug");
				this.pw.println(this.scanBuffer.getBuffered().toString());
			}
		}
		return axiom;
	}

	/**
	 * Exécution en ligne de commande de l'analyseur syntaxique.
	 * 
	 * @param args [-help|-h] [-debug|-d] [-trace|-t] [files].
	 */
	public static void main(final String[] args) {
		final java.io.PrintWriter pw = new java.io.PrintWriter(System.err, true);
		final java.util.List<String> files = new java.util.ArrayList<>();
		final String usage = """
				CupParse [-help|-h] [-debug|-d] [-trace|-t] [files]
				   -debug   dump tokens,
				   -trace   trace LR Automaton
				   files    stdin by default""";
		boolean debug = false;
		boolean trace = false;
		for (String opt : args) {
			switch (opt) {
			case "-h", "-help":
				pw.println(usage);
				System.exit(0);
				break;
			case "-d", "-debug":
				debug = true;
				break;
			case "-t", "-trace":
				trace = true;
				break;
			default:
				files.add(opt);
			}
		}
		if (files.isEmpty()) {
			files.add("stdin");
		}
		for (String file : files) {
			new CupParse(file, pw).run(debug, trace);
		}
	}
}
