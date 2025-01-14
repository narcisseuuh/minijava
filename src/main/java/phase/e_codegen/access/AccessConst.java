package phase.e_codegen.access;

import phase.e_codegen.MIPSRegister;

/**
 * Accès MIPS "Immediate" (Constante).
 * 
 * @author Pascal Hennequin
 */
public class AccessConst implements Access {
	/**
	 * La constante.
	 */
	private final int immediate;

	/**
	 * Construit l'accès à une constante.
	 * 
	 * @param immediate la valeur constante.
	 */
	public AccessConst(final int immediate) {
		this.immediate = immediate;
	}

	@Override
	public String store(final MIPSRegister reg) {
		throw new compil.util.CompilerException("codegen : store in immediate !?!");
	}

	@Override
	public String load(final MIPSRegister reg) {
		return "li   " + reg + ", " + this.immediate;
	}

	@Override
	public String toString() {
		return "" + this.immediate;
	}
}
