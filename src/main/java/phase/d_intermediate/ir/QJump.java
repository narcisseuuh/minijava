package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Saut inconditionnel : {@code Goto arg1}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1 le label du saut.
 */
public record QJump(IRLabel arg1) implements IRQuadruple {
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
		return format("goto " + this.arg1.name());
	}
}
