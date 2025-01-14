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
 * doivent être détectées par l'analyse lexicale ou l'analyse syntaxique.
 * 
 * <p>
 * Cf. fichier SyntaxError/Test301.txt et Test302.txt
 * 
 * @author Denis Conan
 */
final class ErrorsLexicalSyntacticTest {
	/**
	 * Produit un programme MiniJAVA à partir de "bouts de code". La première chaîne
	 * de caractères est du code dans la méthode {@code main} de la classe
	 * {@code Foo}. Les chaînes de caractères suivantes sont les autres classes.
	 * 
	 * @param blocMain le corps de la méthode Main.
	 * @param classes  les définitions des autres classes.
	 * @return le code d'un programme MiniJAVA.
	 */
	private static String codeInFooMain(final String blocMain, final String... classes) {
		final StringBuilder sb = new StringBuilder();
		sb.append("class Foo {public static void main(String[] args) {{System.out.println(42);");
		sb.append(blocMain).append("}}}");
		for (String s : classes) {
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * Produit un programme MiniJAVA à partir de "bouts de code". Le code est dans
	 * la méthode {@code bar} de la classe {@code Bar}, qui possède une méthode
	 * {@code Compute}.
	 * 
	 * @param blocBar le corps de la méthode Bar.
	 * @return le code d'un programme MiniJAVA.
	 */
	private static String codeInFooBar(final String blocBar) {
		final StringBuilder sb = new StringBuilder();
		sb.append("class Foo {public static void main(String[] args) {System.out.println(new Bar().Compute());}}");
		sb.append("\n" + "class Bar {public int Compute() {");
		sb.append(blocBar);
		sb.append("return 0;}}");
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
		// s'arrêter après l'analyse syntaxique
		Compiler.stopAfterSyntax();
	}

	@Test@Disabled
	@DisplayName("no error in codeInMain")
	void noErrorInCodeInMain() {
		String toParse = codeInFooMain("");
		Assertions.assertDoesNotThrow(() -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("no error in codeInFooBar")
	void noErrorInCodeInFooBar() {
		String toParse = codeInFooBar("");
		Assertions.assertDoesNotThrow(() -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("invalid C89 Comment")
	void test301lexicalError1() {
		String toParse = codeInFooMain("/* So should this /* and this still */ but not this */ // invalid C89 Comment");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("invalid identifier")
	void test301lexicalError2() {
		String toParse = codeInFooMain("int 0a; // invalid identifier");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("invalid Decimal Literal (should be Octal!)")
	void test301lexicalError3() {
		String toParse = codeInFooMain("int a = 01; // invalid Decimal Literal (should be Octal!)");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("not a Literal")
	void test301lexicalError4() {
		String toParse = codeInFooMain("bool b=True; // not a Literal");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("unknown character /")
	void test301lexicalError5() {
		String toParse = codeInFooMain("int a = 3 / 4; // unknown character /");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("this is a reserved keyword")
	void test301lexicalError6() {
		String toParse = codeInFooMain("int this; // this is a reserved keyword");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("String only in main")
	void test301lexicalError7() {
		String toParse = codeInFooBar("String s; // String only in main");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("String[] only in main, (and not usable)")
	void test301lexicalError8() {
		String toParse = codeInFooBar("String[] args; // only in main, (and not usable)");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("misleading character .")
	void test302lexicalError9() {
		String toParse = codeInFooBar("System.out.println.(42) // misleading character .");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("only 1 statement in main")
	void test302syntaxError1() {
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(
				"class Foo {public static void main(String[] args) {System.out.println(42);System.out.println(42);}}"));
	}

	@Test@Disabled
	@DisplayName("formal parameter error")
	void test302syntaxError2() {
		Assertions.assertThrows(CompilerException.class,
				() -> stringCompiler("class Foo {public static void main(String[] args) {System.out.println(42);}}"
						+ " class Bar {public int Compute(int) {}}"));
	}

	@Test@Disabled
	@DisplayName("variable declaration error")
	void test302syntaxError3() {
		String toParse = codeInFooBar("int ; // variable declaration error");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("only one argument in System.out.println")
	void test302syntaxError4() {
		String toParse = codeInFooBar("System.out.println(3, 2);");
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(toParse));
	}

	@Test@Disabled
	@DisplayName("class body error")
	void test302syntaxError5() {
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(
				"class Foo {public static void main(String[] args) {System.out.println(42);}} Klass {}"));
	}

	@Test@Disabled
	@DisplayName("missing return type of method")
	void test302syntaxError6() {
		Assertions.assertThrows(CompilerException.class,
				() -> stringCompiler("class Foo {public static void main(String[] args) {System.out.println(42);}}"
						+ " class Bar {public Foo (int num){ return 1; } // method body error}"));
	}

	@Test@Disabled
	@DisplayName("erroneous right brace at the end")
	void test302syntaxError7() {
		Assertions.assertThrows(CompilerException.class, () -> stringCompiler(
				"class Foo {public static void main(String[] args) {System.out.println(42);}} } // error on }"));
	}
}
