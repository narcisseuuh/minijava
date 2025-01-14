package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Label de saut : {@code Label arg1}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1 le nom du label.
 */
public record QLabel(IRLabel arg1) implements IRQuadruple {
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
		return format("\"" + this.arg1.name() + "\"");
	}
}
