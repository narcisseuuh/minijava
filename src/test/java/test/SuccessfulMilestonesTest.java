// CHECKSTYLE:OFF
package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import compil.Compiler;

/**
 * Test du compilateur sur chaîne de caractères en entrée et sur fichiers du
 * répertoire {@code src/test/resources/Jalons}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
final class SuccessfulMilestonesTest {
    /**
     * Une classe exemple.
     */
    private static final String CLASS_TEST3 = "class Test3 extends Test2 {public int zero() {return 0;}}";
    /**
     * Les exemples similaires au fichier des jalons.
     */
    private final String[] jalons = { codeInFooMain("System.out.println(42);"),
            codeInFooMain("System.out.println(2 + 6 * 7 - (5 - 3));"),
            codeInFooMain("System.out.println(new Test2().Start());",
                    "class Test2 { public int Start() {return 7 * 6;}}", CLASS_TEST3),
            codeInFooMain("System.out.println(new Test3().Start(6,7));",
                    "class Test2 {public int Start(int i, int j) {return i * j;}}", CLASS_TEST3),
            codeInFooMain("System.out.println(new Test3().Start(6,7));",
                    "class Test2 {int a; public int Start(int i, int j)",
                    "{int k; return i * j;}public int un() {return 1;} int b;}", CLASS_TEST3),
            codeInFooMain("System.out.println(new Test2().Start(0));",
                    "class Test2 {public int Start(int y) { int a; int b; boolean b1;",
                    "boolean b2; a = 3; a = 1 + a + a + 1; a = a - 1 - 1; a = 1 - a;",
                    "a = a * a; b = y; b = 3 * 3; b = this.evil(); b1 = true;",
                    "b2 = false; b1 = b1 && b2 && !b1; return 2 * a - 8; }", "public int evil() { return 666; }}"),
            codeInFooMain("{ System.out.println(new Test2().Start(0));" + "System.out.println(new Test2().Start(1)); }",
                    "class Test2 { public int Start(int n) {",
                    "int a; int b; a = 0; b = n; if (!(a < b)) {a = 7;} else a = 2;",
                    "while (a < 6) {a = a + 1;} return a * (a + 2 * b - 1);}}"),
            codeInFooMain("System.out.println(new Test2().Start(0));",
                    "class Test2 extends Test3 { int d; int[] e; public int Start(int y)",
                    "{e = new int[3]; a = 3; d = 4; e[0] = a; e[1] = d;",
                    "e[2] = e[0] + e[1]; System.out.println(e[2]); return e.length;}}",
                    "class Test3 extends Test4 {int b; boolean c;}", "class Test4 {int a;}") };

    /**
     * Produit un programme MiniJAVA à partir de "bouts de code". La première chaîne
     * de caractères est du code dans la méthode {@code main} de la classe
     * {@code Foo}. Les chaînes de caractères suivantes sont les autres classes.
     * 
     * @param blocMain le corps de la méthode Main.
     * @param classes  les définitions des autres classes.
     * @return le code d'un programme Minijava.
     */
    private static String codeInFooMain(final String blocMain, final String... classes) {
        final StringBuilder sb = new StringBuilder();
        sb.append("class Foo {public static void main(String[] args) {");
        sb.append(blocMain).append("}}");
        for (String s : classes) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Exécute la compilation sur une {@code String}.
     * 
     * @param prog le code Minijava en entrée.
     */
    private static void stringCompiler(final String prog) {
        System.setIn(new java.io.ByteArrayInputStream(prog.getBytes()));
        new Compiler("stdin", "CompilerTest.mips").execute();
    }

    @BeforeEach
    void setUp() {
        // retirer des commentaires la ligne suivante pour moins d'affichages
        // Debug.noLogging();
        // ne pas s'arrêter après l'analyse syntaxique
        Compiler.doNotStopAfterSyntax();
        // ne pas s'arrêter après l'analyse sémantique
        Compiler.doNotStopAfterSemantic();
    }

    @Test
    @DisplayName("première chaîne de caractères")
    void premierString() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(codeInFooMain("if (true) System.out.println(1); else {}")));
    }

    @Test
    @DisplayName("jalon 1, avec chaîne de caractères")
    void jalonString1() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[1 - 1]));
    }

    @Test
    @DisplayName("jalon 2, avec chaîne de caractères")
    void jalonString2() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[2 - 1]));
    }

    @Test
    @DisplayName("jalon 3, avec chaîne de caractères")
    void jalonString3() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[3 - 1]));
    }

    @Test
    @DisplayName("jalon 4, avec chaîne de caractères")
    void jalonString4() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[4 - 1]));
    }

    @Test
    @DisplayName("jalon 5, avec chaîne de caractères")
    void jalonString5() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[5 - 1]));
    }

    @Test
    @DisplayName("jalon 6, avec chaîne de caractères")
    void jalonString6() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[6 - 1]));
    }

    @Test
    @DisplayName("jalon 7, avec chaîne de caractères")
    void jalonString7() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[7 - 1]));
    }

    // pas de jalon 8

    @Test
    @DisplayName("jalon 9, avec chaîne de caractères")
    void jalonString9() {
        Assertions.assertDoesNotThrow(() -> stringCompiler(jalons[9 - 2]));
    }

    @Test
    @DisplayName("jalon 1, fichier Jalons/Test101.txt")
    void jalonFile1() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test101.txt", "src/test/resources/Jalons/Test101.mips").execute());
    }

    @Test
    @DisplayName("jalon 2, fichier Jalons/Test102.txt")
    void jalonFile2() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test102.txt", "src/test/resources/Jalons/Test102.mips").execute());
    }

    @Test
    @DisplayName("jalon 3, fichier Jalons/Test103.txt")
    void jalonFile3() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test103.txt", "src/test/resources/Jalons/Test103.mips").execute());
    }

    @Test
    @DisplayName("jalon 4, fichier Jalons/Test104.txt")
    void jalonFile4() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test104.txt", "src/test/resources/Jalons/Test104.mips").execute());
    }

    @Test
    @DisplayName("jalon 5, fichier Jalons/Test105.txt")
    void jalonFile5() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test105.txt", "src/test/resources/Jalons/Test105.mips").execute());
    }

    @Test
    @DisplayName("jalon 6, fichier Jalons/Test106.txt")
    void jalonFile6() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test106.txt", "src/test/resources/Jalons/Test106.mips").execute());
    }

    @Test
    @DisplayName("jalon 7, fichier Jalons/Test107.txt")
    void jalonFile7() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test107.txt", "src/test/resources/Jalons/Test107.mips").execute());
    }

    // pas de jalon 8

    @Test
    @DisplayName("jalon 9, fichier Jalons/Test109.txt")
    void jalonFile9() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Jalons/Test109.txt", "src/test/resources/Jalons/Test109.mips").execute());
    }
}
