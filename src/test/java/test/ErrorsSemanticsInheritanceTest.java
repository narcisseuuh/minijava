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
 * Cf. fichier SyntaxError/Test403.txt
 * 
 * @author Denis Conan
 */
final class ErrorsSemanticsInheritanceTest {

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
		sb.append("\n" + "class B extends Bar {}");
		sb.append("\n" + "class C extends B {}");
		sb.append("\n" + "class Bar {Bar a; B b; C c;" + "public int compute() {a = b; a = c; b = c;");
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
	@DisplayName("class loop 1")
	void test403SemanticError1() {
		String toParse = codeInBarBar("") + "class G extends H { } class H extends G { } // Fail Class Loop";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class loop 2")
	void test403SemanticError2() {
		String toParse = codeInBarBar("") + "class F extends F { } // Fail Class Loop";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail class undef")
	void test403SemanticError3() {
		String toParse = codeInBarBar("") + "class E extends Unk { } // Fail Class Undef";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail duplicate class 1")
	void test403SemanticError4() {
		String toParse = codeInBarBar("") + "class D {} class D {}      // Fail Duplicated class";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("fail duplicate class 2")
	void test403SemanticError5() {
		String toParse = codeInBarBar("")
				+ "class Foo {}     // Fail Duplicated class (may be not if mainKlass not in Symbol table!)";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class type mismatch 1")
	void test403SemanticError6() {
		String toParse = codeInBarBar("c = a; // FAIL Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class type mismatch 2")
	void test403SemanticError7() {
		String toParse = codeInBarBar("c = b; // FAIL Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class type mismatch 3")
	void test403SemanticError8() {
		String toParse = codeInBarBar("c = new Bar();");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class type mismatch 4")
	void test403SemanticError9() {
		String toParse = codeInBarBar("a = new D(); // FAIL Type") + "class D extends Object { int x; int y; }";
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class type mismatch 5")
	void test403SemanticError10() {
		String toParse = codeInBarBar("b = this; // FAIL Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class type mismatch 6")
	void test403SemanticError11() {
		String toParse = codeInBarBar("c = this; // FAIL Type");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}
}
