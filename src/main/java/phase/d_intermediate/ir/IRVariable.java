package phase.d_intermediate.ir;

/**
 * Interface commune pour les variables de la table des symboles de l'AST
 * {@link phase.c_semantic.symtab.InfoVar} et les variables de la forme intermédiaire
 * {@link IRConst}, {@link IRTempVar} et {@link IRLabel}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public interface IRVariable {
	/**
	 * Donne le nom de la variable.
	 * 
	 * @return le nom de la variable IR.
	 */
	String name();

	/**
	 * Donne le type de la variable.
	 * 
	 * @return le type de la variable IR.
	 */
	String type();
}
