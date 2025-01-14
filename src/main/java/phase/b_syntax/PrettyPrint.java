package phase.b_syntax;

import compil.util.IndentWriter;
import phase.b_syntax.ast.AstNode;
import phase.b_syntax.ast.AstVisitorDefault;
import phase.b_syntax.ast.Axiom;
import phase.b_syntax.ast.ExprArrayLength;
import phase.b_syntax.ast.ExprArrayLookup;
import phase.b_syntax.ast.ExprArrayNew;
import phase.b_syntax.ast.ExprCall;
import phase.b_syntax.ast.ExprLiteralBool;
import phase.b_syntax.ast.ExprLiteralInt;
import phase.b_syntax.ast.ExprNew;
import phase.b_syntax.ast.ExprOpBin;
import phase.b_syntax.ast.ExprOpUn;
import phase.b_syntax.ast.Formal;
import phase.b_syntax.ast.Ident;
import phase.b_syntax.ast.Klass;
import phase.b_syntax.ast.KlassMain;
import phase.b_syntax.ast.Method;
import phase.b_syntax.ast.StmtArrayAssign;
import phase.b_syntax.ast.StmtAssign;
import phase.b_syntax.ast.StmtBlock;
import phase.b_syntax.ast.StmtIf;
import phase.b_syntax.ast.StmtPrint;
import phase.b_syntax.ast.StmtWhile;
import phase.b_syntax.ast.Type;
import phase.b_syntax.ast.Variable;

/**
 * PrettyPrint Minijava par visite de l'AST.
 * 
 * @author Pascal Hennequin
 */
public final class PrettyPrint extends AstVisitorDefault {
	/**
	 * Le "Writer" pour l'impression avec indentation.
	 */
	private final IndentWriter out;

	/**
	 * L'arbre de Syntaxe.
	 */
	private final AstNode axiom;

	/**
	 * Construit le PrettyPrint.
	 * 
	 * @param axiom l'arbre de syntaxe.
	 */
	public PrettyPrint(final AstNode axiom) {
		this.out = new IndentWriter();
		this.axiom = axiom;
	}

	/**
	 * Exécute le PrettyPrint.
	 * 
	 * @return le texte reformaté du programme Minijava.
	 */
	public String execute() {
		this.axiom.accept(this); // => visit((Axiom)axiom)
		return out.toString();
	}

	// Helpers
	/**
	 * Un début de bloc "{".
	 */
	private void openBlock() {
		out.println('{');
		out.indent();
	}

	/**
	 * Une fin de bloc "}".
	 */
	private void closeBlock() {
		out.outdent();
		out.println('}');
	}

	/**
	 * Un espace.
	 */
	private void space() {
		out.print(' ');
	}

	/////////////////// Visit ////////////////////
	/**
	 * Visite générique pour une liste séparée par des virgules.
	 * 
	 * @param n le nœud courant.
	 */
	private void visitCommaList(final AstNode n) {
		boolean first = true;
		for (AstNode f : n) {
			if (first) {
				first = false;
			} else {
				out.print(", ");
			}
			f.accept(this);
		}
	}

	// Productions de base
	@Override
	public void visit(final Axiom n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final Klass n) {
		out.println();
		out.print("class ");
		n.klassId().accept(this);
		out.print(" extends ");
		n.parentId().accept(this);
		space();
		openBlock();
		n.vars().accept(this);
		n.methods().accept(this);
		closeBlock();
	}

	@Override
	public void visit(final KlassMain n) {
		out.print("class ");
		n.klassId().accept(this);
		openBlock();
		out.print("public static void main(String [] ");
		n.argId().accept(this);
		out.print(')');
		space();
		openBlock();
		n.stmt().accept(this);
		closeBlock();
		closeBlock();
	}

	@Override
	public void visit(final Method n) {
		out.println();
		out.print("public ");
		n.returnType().accept(this);
		n.methodId().accept(this);
		out.print('(');
		visitCommaList(n.fargs());
		out.print(')');
		space();
		openBlock();
		n.vars().accept(this);
		n.stmts().accept(this);
		out.print("return ");
		n.returnExp().accept(this);
		out.println(';');
		closeBlock();
	}

	@Override
	public void visit(final Formal n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final Ident n) {
		out.print(n.name());
	}

	@Override
	public void visit(final Type n) {
		out.print(n.name());
		space();
	}

	@Override
	public void visit(final Variable n) {
		defaultVisit(n);
		out.println(';');
	}

	// Expressions
	@Override
	public void visit(final ExprArrayLength n) {
		defaultVisit(n);
		out.print(".length");
	}

	@Override
	public void visit(final ExprArrayLookup n) {
		n.array().accept(this);
		out.print('[');
		n.index().accept(this);
		out.print(']');
	}

	@Override
	public void visit(final ExprArrayNew n) {
		out.print("new int [");
		defaultVisit(n);
		out.print(']');
	}

	@Override
	public void visit(final ExprCall n) {
		n.receiver().accept(this);
		out.print('.');
		n.methodId().accept(this);
		out.print('(');
		visitCommaList(n.args());
		out.print(')');
	}

	@Override
	public void visit(final ExprLiteralBool n) {
		out.print(n.value());
	}

	@Override
	public void visit(final ExprLiteralInt n) {
		out.print(n.value());
	}

	@Override
	public void visit(final ExprNew n) {
		out.print("new ");
		defaultVisit(n);
		out.print("()");
	}

	@Override
	public void visit(final ExprOpBin n) {
		// des parenthèses pas toutes utiles, difficile à optimiser !
		out.print('(');
		n.expr1().accept(this);
		space();
		out.print(n.op());
		space();
		n.expr2().accept(this);
		out.print(')');
	}

	@Override
	public void visit(final ExprOpUn n) {
		out.print(n.op());
		space();
		n.expr().accept(this);
	}

	// Instructions
	@Override
	public void visit(final StmtArrayAssign n) {
		n.arrayId().accept(this);
		out.print('[');
		n.index().accept(this);
		out.print("] = ");
		n.value().accept(this);
		out.println(';');
	}

	@Override
	public void visit(final StmtAssign n) {
		n.varId().accept(this);
		out.print(" = ");
		n.value().accept(this);
		out.println(';');
	}

	@Override
	public void visit(final StmtBlock n) {
		switch (n.stmts().nbChildren() + n.vars().nbChildren()) {
		case 0 -> out.println("{}");
		case 1 -> {
			n.vars().accept(this);
			n.stmts().accept(this);
		}
		default -> {
			openBlock();
			n.vars().accept(this);
			n.stmts().accept(this);
			closeBlock();
		}
		}
	}

	@Override
	public void visit(final StmtIf n) {
		out.print("if (");
		n.test().accept(this);
		out.print(") ");
		n.ifTrue().accept(this);
		out.print("else ");
		n.ifFalse().accept(this);
	}

	@Override
	public void visit(final StmtPrint n) {
		out.print("System.out.println(");
		n.expr().accept(this);
		out.println(");");
	}

	@Override
	public void visit(final StmtWhile n) {
		out.print("while (");
		n.test().accept(this);
		out.print(')');
		space();
		n.body().accept(this);
	}
}
