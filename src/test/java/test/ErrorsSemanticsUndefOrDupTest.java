// CHECKSTYLE:OFF
package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import compil.Compiler;
import compil.util.CompilerException;

/**
 * Test du compilateur sur chaînes de caractères en entrée avec des erreurs qui
 * doivent être détectées par l'analyseur sémantique.
 * 
 * <p>
 * Cf. fichier SyntaxError/Test405.txt
 * 
 * @author Denis Conan
 */
final class ErrorsSemanticsUndefOrDupTest {

	/**
	 * Produit un programme MiniJAVA à partir de "bouts de code". Le code est dans
	 * la méthode {@code bar} de la classe {@code Bar}, qui possède une méthode
	 * {@code compute}.
	 * 
	 * @param blocBar le corps de la méthode Bar.
	 * @return le code d'un programme MiniJAVA.
	 */
	private static String codeInBarBar(final String blocBar) {
		final StringBuilder sb = new StringBuilder();
		sb.append("class Foo {public static void main(String[] args) {{System.out.println(new Bar().compute());}}}");
		sb.append("\n" + "class Op2 {}");
		sb.append("\n" + "class Bar {boolean b; int i; Bar op; Op2 op2;"
				+ " public int doit(Bar op, int i) {return 0;} " + "public int compute() {");
		sb.append(blocBar);
		sb.append("\n" + "return 0;}}");
		return sb.toString();
	}

	private static void stringCompiler(final String prog) {
		System.setIn(new java.io.ByteArrayInputStream(prog.getBytes()));
		new Compiler("stdin", "CompilerTest.mips").execute();
	}

	@BeforeEach
	void setUp() {
		// mettre en commentaire la ligne suivante pour plus d'affichage
		// Debug.noLogging();
		// ne pas s'arrêter après l'analyse syntaxique
		Compiler.doNotStopAfterSyntax();
		// arrêt après l'analyse syntaxique, donc pas de génération MIPS
		Compiler.stopAfterSemantic();
	}

	@Test@Disabled
	@DisplayName("no error in codeInFooBar")
	void noErrorInCodeInBarBar() {
		Assertions.assertDoesNotThrow(() -> stringCompiler(codeInBarBar("")));
	}

	@Test@Disabled
	@DisplayName("duplicate attribute")
	void test405SemanticError1() {
		String toParse = codeInBarBar("") + "class Other {int i; boolean i;}";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("undefined class")
	void test405SemanticError2() {
		String toParse = codeInBarBar("") + "class Other {FooBar m;}";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("no duplication field/formal")
	void test405SemanticError3() {
		Assertions.assertDoesNotThrow(() -> stringCompiler(codeInBarBar("")
				+ "class Other {int k; public int k(){ return k;} // Not a duplication (field/method)\n}"));
	}

	@Test@Disabled
	@DisplayName("no duplication field/formal")
	void test405SemanticError4() {
		Assertions.assertDoesNotThrow(() -> stringCompiler(codeInBarBar("")
				+ "class Other{public boolean l(int l){int l; // may be duplication with formal\n return l < l;}}"));
	}

	@Test@Disabled
	@DisplayName("override or duplication ?!?")
	void test405SemanticError5() {
		Assertions.assertDoesNotThrow(() -> stringCompiler(codeInBarBar("")
				+ "class Other{public int over(int x, int y){ return 0; } // Override or Duplication ?!?\n }"));
	}

	@Test@Disabled
	@DisplayName("duplicated method")
	void test405SemanticError6() {
		Assertions.assertDoesNotThrow(
				() -> Assertions.assertThrows(CompilerException.class, () -> stringCompiler(codeInBarBar("")
						+ "class Other {Bar op; public Bar get() {return op;} public int get() {return 0;}// Fail Duplication\n }")));
	}

	@Test@Disabled
	@DisplayName("undefined variable (type is undef, then type mismatch)")
	void test405SemanticError7() {
		String toParse = codeInBarBar("op = oper; // FAIL : \"oper\" undef (+ type)");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("undef method on type (a non-method, then type mismatch")
	void test405SemanticError8() {
		String toParse = codeInBarBar("i = i.compute(); // FAIL : undef Class (+...)");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("undef method on type 2")
	void test405SemanticError9() {
		String toParse = codeInBarBar("i = op.start(); // FAIL : undef method");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("unknown type, then type mismatch")
	void test405SemanticError10() {
		String toParse = codeInBarBar("op = new i(); // FAIL : class undef (+ type)");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("unknown type, then type mismatch 2")
	void test405SemanticError11() {
		String toParse = codeInBarBar("op = new op(); // FAIL : class undef (+ type)");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("unknown variable, then undef and type mismatch")
	void test405SemanticError12() {
		String toParse = codeInBarBar("i1 = 10; // FAIL : var undef");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("redefinition 1")
	void test405SemanticError13() {
		Assertions.assertDoesNotThrow(() -> stringCompiler(codeInBarBar("boolean b; // Redefinition")));
	}

	@Test@Disabled
	@DisplayName("redefinition 2")
	void test405SemanticError14() {
		Assertions.assertDoesNotThrow(() -> stringCompiler(codeInBarBar("boolean i; // Redefinition2")));
	}
}
