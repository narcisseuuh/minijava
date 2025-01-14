package compil.util;

/**
 * Runtime Exception de Compilation.
 * 
 * @author Pascal Hennequin
 */
public class CompilerException extends RuntimeException {
	/** La loi des séries. */
	private static final long serialVersionUID = 42L;

	/**
	 * Construit une exception pour le compilateur.
	 * 
	 * @param message le message d'erreur
	 */
	public CompilerException(final String message) {
		super(message);
	}
}
