package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Retour de fonction : {@code Return arg1}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1 la valeur de retour.
 */
public record QReturn(IRVariable arg1) implements IRQuadruple {
	@Override
	public EnumOper op() {
		return null;
	}

	@Override
	public IRVariable arg2() {
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
		return format("return " + this.arg1.name());
	}
}
