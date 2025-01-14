package compil.util;

import java.io.PrintWriter;

/**
 * Classe utilitaire pour impression avec indentation.
 * <ul>
 * <li>Étend {@link java.io.PrintWriter} avec les méthodes {@link #indent} et
 * {@link #outdent}
 * <li>Usage pour l'impression dans un {@code String} :
 * 
 * <blockquote>
 * 
 * <pre>
 * IndentWriter iw = new IndentWriter();
 * iw.println(...); iw.print(...);
 * iw.indent();
 * iw.print(...); iw.println(...);
 * iw.outdent();
 * String s = iw.toString();
 * <i>ou</i> System.out.println(iw);
 * </pre>
 * 
 * </blockquote>
 * </ul>
 * 
 * @author Pascal Hennequin
 */
public class IndentWriter extends PrintWriter {
	/**
	 * La fin de ligne.
	 */
	private static final String NL = System.lineSeparator();
	/**
	 * La tabulation par défaut.
	 */
	private static final String DEFTAB = "  ";
	/**
	 * La chaine de tabulation.
	 */
	private final String tab;
	/**
	 * Le niveau d'indentation courant.
	 */
	private int indent;
	/**
	 * La chaine d'indentation courante. == {@link #indent} * {@link #tab}
	 */
	private String strIndent;
	/**
	 * {@code true} si prochaine impression en début de ligne.
	 */
	private boolean newLine;

	/**
	 * Construit un "IndentWriter" par défaut. Impression dans un {@code String}
	 */
	public IndentWriter() {
		this(new java.io.StringWriter(), DEFTAB, 0);
	}

	/**
	 * Construit un "IndentWriter" général.
	 * 
	 * @param out    un {@link java.io.Writer} de sortie.
	 * @param tab    la chaine de tabulation.
	 * @param indent l'indentation initiale.
	 */
	public IndentWriter(final java.io.Writer out, final String tab, final int indent) {
		super(out); // NB. super.out == out
		this.tab = tab;
		this.indent = indent;
		this.strIndent = currentIndent();
		this.newLine = true;
	}

	/**
	 * Le résultat de l'impression. (Utile uniquement pour des "Writers" dans des
	 * "chaines").
	 */
	@Override
	public String toString() {
		return super.out.toString();
	}

	/**
	 * Incrémente le niveau d'indentation.
	 */
	public void indent() {
		this.indent++;
		this.strIndent = currentIndent();
	}

	/**
	 * Décrémente le niveau d'indentation.
	 */
	public void outdent() {
		this.indent--;
		this.strIndent = currentIndent();
	}

	/**
	 * Retourne la chaine d'indentation courante.
	 * 
	 * @return la tabulation courante.
	 */
	private String currentIndent() {
		return tab.repeat(this.indent);
	}

	/**
	 * Redéfinitions des impressions de PrintWriter pour insérer l'indentation
	 * peut-être mieux ? : override Writer.write(char), write(char[]). write(String).
	 */
	@Override
	public void println() {
		super.println();
		this.newLine = true;
	}

	@Override
	public void print(final char c) {
		print(String.valueOf(c));
	}

	@Override
	public void print(final String str) {
		String s = str;
		if (this.newLine) {
			super.print(this.strIndent);
			this.newLine = false;
		}
		// may be String.indent(i) dans JAVA12+
		final boolean isEOL = s.endsWith(NL);
		if (isEOL) {
			s = s.substring(0, s.length() - 1);
		}
		s = s.replace(NL, NL + this.strIndent);
		super.print(s);
		if (isEOL) {
			println();
		}
	}
}
