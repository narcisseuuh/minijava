// CHECKSTYLE:OFF
package test_other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import compil.Compiler;

/**
 * Test du compilateur avec des exemples variés dans les fichiers des
 * répertoires {@code Modern}.
 * 
 * Tous ces tests sont censés passer.
 * 
 * @author Denis Conan
 */
final class BookModernCompilerTest {

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
	void testFileBinarySearch() {
		Assertions.assertDoesNotThrow(() -> new Compiler("Modern/BinarySearch.txt", "BinarySearch.mips").execute());
	}

	@Test@Disabled
	void testFileBinaryTree() {
		Assertions.assertDoesNotThrow(() -> new Compiler("Modern/BinaryTree.txt", "BinaryTree.mips").execute());
	}

	@Test@Disabled
	void testFileBubbleSort() {
		Assertions.assertDoesNotThrow(() -> new Compiler("Modern/BubbleSort.txt", "BubbleSort.mips").execute());
	}

	@Test@Disabled
	void testFileFactorial() {
		Assertions.assertDoesNotThrow(() -> new Compiler("Modern/Factorial.txt", "Factorial.mips").execute());
	}

	@Test@Disabled
	void testFileLinearSearch() {
		Assertions.assertDoesNotThrow(() -> new Compiler("Modern/LinearSearch.txt", "LinearSearch.mips").execute());
	}

	@Test@Disabled
	void testFileQuickSort() {
		Assertions.assertDoesNotThrow(() -> new Compiler("Modern/QuickSort.txt", "QuickSort.mips").execute());
	}
}
