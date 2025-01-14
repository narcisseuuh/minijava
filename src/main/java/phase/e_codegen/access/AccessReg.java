package phase.e_codegen.access;

import phase.e_codegen.MIPSRegister;

/**
 * Accès MIPS dans un registre.
 * 
 * @author Pascal Hennequin
 */
public class AccessReg implements Access {
	/**
	 * Le registre contenant la variable.
	 */
	private final MIPSRegister register;

	/**
	 * Construit l'accès à une variable dans un registre.
	 * 
	 * @param register le registre.
	 */
	public AccessReg(final MIPSRegister register) {
		this.register = register;
	}

	/**
	 * Donne le registre d'une variable allouée dans un registre.
	 * 
	 * <p>Permet éventuellement une optimisation de copie entre registre.
	 * @return  le registre.
	 */
	public MIPSRegister getRegister() {
		return this.register;
	}
	
	@Override
	public String store(final MIPSRegister reg) {
		if (reg.equals(this.register)) {
			return null;
		}
		return "move " + this.register + ", " + reg;
	}

	@Override
	public String load(final MIPSRegister reg) {
		if (reg.equals(this.register)) {
			return null;
		}
		return "move " + reg + ", " + this.register;
	}

	@Override
	public String toString() {
		return this.register.toString();
	}
}
