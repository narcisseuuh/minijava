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
 * Cf. fichier SyntaxError/Test401.txt et Test402.txt
 * 
 * @author Denis Conan
 */
final class ErrorsSemanticsTypingTest {

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
	@DisplayName("fail type 1")
	void test401SemanticError1() {
		String toParse = codeInBarBar("i = op; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 2")
	void test401SemanticError2() {
		String toParse = codeInBarBar("b = i; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 3")
	void test401SemanticError3() {
		String toParse = codeInBarBar("i = true && false; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 4")
	void test401SemanticError4() {
		String toParse = codeInBarBar("i = 6 < 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 5")
	void test401SemanticError5() {
		String toParse = codeInBarBar("i = !false; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 6")
	void test401SemanticError6() {
		String toParse = codeInBarBar("b = 6 + 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 7")
	void test401SemanticError7() {
		String toParse = codeInBarBar("b = 6 - 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 8")
	void test401SemanticError8() {
		String toParse = codeInBarBar("b = 6 * 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 9")
	void test401SemanticError9() {
		String toParse = codeInBarBar("b = true && 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 10")
	void test401SemanticError10() {
		String toParse = codeInBarBar("b = true < 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 11")
	void test401SemanticError11() {
		String toParse = codeInBarBar("b = !6; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 12")
	void test401SemanticError12() {
		String toParse = codeInBarBar("i = true + 7; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 13")
	void test401SemanticError13() {
		String toParse = codeInBarBar("4 = 6 - false; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 14")
	void test401SemanticError14() {
		String toParse = codeInBarBar("i = true * false; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 15")
	void test401SemanticError15() {
		String toParse = codeInBarBar("op = op2; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 16")
	void test401SemanticError16() {
		String toParse = codeInBarBar("System.out.println(true); // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 17")
	void test401SemanticError17() {
		String toParse = codeInBarBar("System.out.println(op); // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 18")
	void test401SemanticError18() {
		String toParse = codeInBarBar("System.out.println(op.get()); // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 19")
	void test401SemanticError19() {
		String toParse = codeInBarBar("while (i) { } // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 20")
	void test401SemanticError21() {
		String toParse = codeInBarBar("while (op) { } // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 21")
	void test401SemanticError22() {
		String toParse = codeInBarBar("while (op.compute()) {} // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail type 22")
	void test401SemanticError23() {
		String toParse = codeInBarBar("if (i) { } else { } // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("this undef in static method")
	void test402SemanticError1() {
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(
				"class Test402{public static void main(String [] a) {System.out.println(this); // this undef in static method\n}}"));
	}

	@Test@Disabled
	@DisplayName("error in number of arguments 1")
	void test402SemanticError2() {
		String toParse = codeInBarBar("i = this.doit(op); // FAIL : number of arguments");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("error in number of arguments 2")
	void test402SemanticError3() {
		String toParse = codeInBarBar("i = this.doit(op, 3, 4); // FAIL : number of arguments");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("error argument type 1")
	void test402SemanticError4() {
		String toParse = codeInBarBar("i = this.doit(3, 4); // FAIL : arg type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("error argument type 2")
	void test402SemanticError5() {
		String toParse = codeInBarBar("i = this.doit(4, op); // FAIL : arg type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("error return type 1")
	void test402SemanticError6() {
		String toParse = codeInBarBar("") + "\n"
				+ "class Other {public int redoit(Bar op, int i) {return true;  //Fail return Type \n }}";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("error return type 2")
	void test402SemanticError7() {
		String toParse = codeInBarBar("") + "\n"
				+ "class Other {public int redoit(Bar op, int i) {return op;  //Fail return Type \n }}";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}
}
