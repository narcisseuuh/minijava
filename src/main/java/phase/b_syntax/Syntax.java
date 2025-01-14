package phase.b_syntax;

import compil.util.CompilerException;
import compil.util.Debug;
import phase.b_syntax.ast.AstNode;
import phase.b_syntax.ast.Axiom;

/**
 * Analyse Lexicale et Syntaxique. Encapsulation de la classe CupParse.
 * 
 * @author Pascal Hennequin
 */
public class Syntax {
	/**
	 * Le fichier en entrée de l'analyse syntaxique.
	 */
	private String infile;

	/**
	 * Construit l'analyse syntaxique.
	 * 
	 * @param infile le fichier en entrée, {@code "stdin"} pour entrée standard.
	 */
	public Syntax(final String infile) {
		this.infile = infile;
	}

	/**
	 * Exécute l'analyseur.
	 * 
	 * @return l'arbre de syntaxe abstraite.
	 * @throws CompilerException si l'AST est invalide.
	 */
	public Axiom execute() {
		final Object cupResult = new CupParse(this.infile, Debug.PW).run(Debug.TOKEN, Debug.PARSE);
		if (cupResult == null) {
			throw new CompilerException("Lexical or syntactic error");
		}
		if (!(cupResult instanceof AstNode ast)) {
			throw new CompilerException("axiom is not an AstNode");
		}
		if (Debug.TREE) {
			Debug.log("= AST");
			Debug.log(ast.toPrint());
		}
		if (!(ast instanceof Axiom axiom)) {
			throw new CompilerException("axiom is not an Axiom");
		}
		if (Debug.PRETTY) {
			Debug.log("= Pretty Print");
			Debug.log(new PrettyPrint(axiom).execute());
		}
		return axiom;
	}
}
