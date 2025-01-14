// CHECKSTYLE:OFF
package test_other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import compil.Compiler;
import compil.util.Debug;

/**
 * Test de l'analyse lexicale et syntaxique avec fichiers en entrée.
 * 
 * <p>
 * Cf. fichiers Running/StressTestSyntax*.txt
 * 
 * @author Denis Conan
 */
final class SuccessfulStressSyntaxTest {

	@BeforeEach
	void setUp() {
		// mettre en commentaire la ligne suivante pour moins d'affichage
		Debug.noLogging();
		// arrêt après l'analyse syntaxique, donc pas de génération MIPS
		Compiler.stopAfterSyntax();
	}

	@Test@Disabled
	@DisplayName("stress test syntaxe, sans tableau")
	void jalonFile207() {
		Assertions.assertDoesNotThrow(
				() -> new Compiler("Running/StressTestSyntax207.txt", "StressTestSyntax207.mips").execute());
	}

	@Test@Disabled
	@DisplayName("stress test syntaxe, avec tableau")
	void jalonFile209() {
		Assertions.assertDoesNotThrow(
				() -> new Compiler("Running/StressTestSyntax209.txt", "StressTestSyntax209.mips").execute());
	}
}
