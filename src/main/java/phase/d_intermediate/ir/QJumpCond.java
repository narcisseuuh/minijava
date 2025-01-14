package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Saut conditionnel : {@code IfFalse arg2 Goto arg1}.
  * 
 * @author Pascal Hennequin
 * @author Denis Conan
* 
 * @param arg1 le label du saut.
 * @param arg2 le test de non-saut.
 */
public record QJumpCond(IRLabel arg1, IRVariable arg2) implements IRQuadruple {
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
		return format("ifnot " + this.arg2.name() + " goto " + this.arg1.name());
	}
}
