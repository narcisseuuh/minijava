package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Appel de méthode : {@code result = Call arg1 <nbParams=arg2>}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1   le nom de méthode.
 * @param arg2   le nombre de paramètres.
 * @param result la valeur de retour.
 */
public record QCall(IRLabel arg1, IRConst arg2, IRVariable result) implements IRQuadruple {
	@Override
	public EnumOper op() {
		return null;
	}

	@Override
	public void accept(final IRvisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return format(this.result.name() + " = " + "call " + this.arg1.name() + "<" + this.arg2.value() + ">");
	}
}
