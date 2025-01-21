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
 * Cf. fichier SyntaxError/Test409.txt
 * 
 * @author Denis Conan
 */
final class ErrorsSemanticsArrayTest {

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
		sb.append("\n" + "class Bar {boolean b; int i; Bar op; int[] tab; public int compute() {int i2; int[] tab2;");
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

	@Test
	@DisplayName("no error in codeInFooBar")
	void noErrorInCodeInBarBar() {
		String toParse = codeInBarBar("");
		Assertions.assertDoesNotThrow(() -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("non integer size for new array 1")
	void test409SemanticError1() {
		String toParse = codeInBarBar("tab = new int[true]; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("non integer size for new array 2")
	void test409SemanticError2() {
		String toParse = codeInBarBar("tab = new int[op.get()]; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array access on non-array 1")
	void test409SemanticError3() {
		String toParse = codeInBarBar("i = i2[10]; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array access on non-array 2")
	void test409SemanticError4() {
		String toParse = codeInBarBar("i2[10] = 5; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array access on non-array 3")
	void test409SemanticError5() {
		String toParse = codeInBarBar("op[10] = 5; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array index requires integer")
	void test409SemanticError6() {
		String toParse = codeInBarBar("i = tab[true]; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("int->int[] 1")
	void test409SemanticError7() {
		String toParse = codeInBarBar("tab = 5; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("int->int[] 2")
	void test409SemanticError8() {
		String toParse = codeInBarBar("tab = i2; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array index requires integer in assignment 1")
	void test409SemanticError9() {
		String toParse = codeInBarBar("tab[true] = 20; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array index requires integer in assignment 2")
	void test409SemanticError10() {
		String toParse = codeInBarBar("tab[op] = 20; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array index requires integer in assignment 3")
	void test409SemanticError11() {
		String toParse = codeInBarBar("tab[op.get()] = 20; // FAIL : Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array index and right type in assignment 1")
	void test409SemanticError12() {
		String toParse = codeInBarBar("tab[op.compute()] = 20; // OK !");
		Assertions.assertDoesNotThrow(() -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("assign non int in int[] 1")
	void test409SemanticError13() {
		String toParse = codeInBarBar("tab[i] = true; // FAIL : Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("assign non int in int[] 2")
	void test409SemanticError14() {
		String toParse = codeInBarBar("tab[i] = op; // FAIL : Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("assign non int in int[] 3")
	void test409SemanticError15() {
		String toParse = codeInBarBar("tab[1] = op.get(); // FAIL : Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("array index and right type in assignment 2")
	void test409SemanticError16() {
		String toParse = codeInBarBar("tab[1] = op.compute();// OK");
		Assertions.assertDoesNotThrow(() -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("invalid Type for operator 1")
	void test409SemanticError17() {
		String toParse = codeInBarBar("b = !op; // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("invalid Type for operator 2")
	void test409SemanticError18() {
		String toParse = codeInBarBar("b = !op.compute(); // FAIL : type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test
	@DisplayName("length only applies to arrays")
	void test409SemanticError19() {
		String toParse = codeInBarBar("i = i2.length; // FAIL : .length for non Array");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}
}
