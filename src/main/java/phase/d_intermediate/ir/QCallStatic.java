package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Appel de méthode statique : {@code static void Call arg1 <nbParams=arg2>}.
 * <br>
 * Cas particulier des fonctions {@code static void} :
 * {@code main, _system_out_println, _system_exit}
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1 le nom de méthode.
 * @param arg2 le nombre de paramètres.
 */
public record QCallStatic(IRLabel arg1, IRConst arg2) implements IRQuadruple {
	@Override
	public EnumOper op() {
		return null;
	}

	@Override
	public IRVariable result() {
		return null;
	}

	@Override
	public void accept(final IRvisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return format("call " + this.arg1.name() + "<" + this.arg2.value() + ">");
	}
}
