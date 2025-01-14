// CHECKSTYLE:OFF
package test_other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import compil.Compiler;

/**
 * Test du compilateur avec des exemples variés dans les fichiers des
 * répertoires {@code Running}.
 * 
 * @author Denis Conan
 */
final class RunningTest {

    @BeforeEach
    void setUp() {
        // retirer des commentaires la ligne suivante pour moins d'affichages
        // Debug.noLogging();
        // ne pas s'arrêter après l'analyse syntaxique
        Compiler.doNotStopAfterSyntax();
        // ne pas s'arrêter après l'analyse sémantique
        Compiler.doNotStopAfterSemantic();
    }

    @Test@Disabled
    @DisplayName("opérateurs")
    void testFile201() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test201.txt", "src/test/resources/Running/Test201.mips").execute());
    }

    @Test@Disabled
    @DisplayName("expressions et variables")
    void testFile202() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test202.txt", "src/test/resources/Running/Test202.mips").execute());
    }

    @Test@Disabled
    @DisplayName("initialisation")
    void testFile203() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test203.txt", "src/test/resources/Running/Test203.mips").execute());
    }

    @Test@Disabled
    @DisplayName("appel de fonction et (moins de 4) arguments")
    void testFile204() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test204.txt", "src/test/resources/Running/Test204.mips").execute());
    }

    @Test@Disabled
    @DisplayName("appel de fonction et (plus de 4) arguments")
    void testFile205() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test205.txt", "src/test/resources/Running/Test205.mips").execute());
    }

    @Test@Disabled
    @DisplayName("tableaux d'entiers")
    void testFile209() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test209.txt", "src/test/resources/Running/Test209.mips").execute());
    }

    @Test@Disabled
    @DisplayName("tableaux de booléens")
    void testFile219() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/Test219.txt", "src/test/resources/Running/Test219.mips").execute());
    }

    @Test@Disabled
    @DisplayName("fonction de Ackermann (récursion gourmande)")
    void testFileFonctionDeAckermann() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/AckermannFunction.txt", "AckermannFunction.mips").execute());
    }

    @Test@Disabled
    @DisplayName("implem. simpl. de BigNum")
    void testFileImplemSimplisticBigNum() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/ImplemSimplisticBigNum.txt", "ImplemSimplisticBigNum.mips").execute());
    }

    @Disabled("disabled car pb caractère utf8 système de fichiers")
    @Test
    @DisplayName("peano + utf8 Матрёшка")
    void testFilePeanoPlusUTF8Матрёшка() {
        Assertions.assertDoesNotThrow(
                () -> new Compiler("Running/PeanoPlusUTF8Матрёшка.txt", "PeanoPlusUTF8Матрёшка.mips").execute());
    }
}
