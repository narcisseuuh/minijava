/**
 * Allocation mémoire. Fonctions MIPS de transfert entre registre et variables
 * IR :
 * <ul>
 * <li>les constantes {@link phase.e_codegen.access.AccessConst},</li>
 * <li>les variables dans un registre {@link phase.e_codegen.access.AccessReg},</li>
 * <li>les variables en mémoire : un registre d'adresse et un offset entier
 * {@link phase.e_codegen.access.AccessOff}.</li>
 * </ul>
 * 
 * @author Pascal Hennequin
 */

package phase.e_codegen.access;
