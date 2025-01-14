package phase.e_codegen.access;

import phase.e_codegen.MIPSRegister;

/**
 * L’accès MIPS à une variable de la forme intermédiaire. Définition des
 * instructions MIPS pour charger dans un registre (load) ou sauvegarder depuis
 * un registre (store) une variable. Matérialise l'allocation mémoire réalisée
 * par {@link phase.e_codegen.Allocator}.
 * 
 * @author Pascal Hennequin
 */
public interface Access {
	/**
	 * Enregistre le registre dans la variable.
	 * 
	 * @param reg le registre.
	 * @return l'instruction MIPS.
	 */
	String store(MIPSRegister reg);

	/**
	 * Charge la variable dans le registre.
	 * 
	 * @param reg le registre.
	 * @return l'instruction MIPS.
	 */
	String load(MIPSRegister reg);
}
