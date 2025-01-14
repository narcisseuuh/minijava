package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Label de méthode : {@code LabelMeth arg1 <nbParams=arg2>}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1 le nom de la méthode.n
 * @param arg2 le nombre de paramètres.
 */
public record QLabelMeth(IRLabel arg1, IRConst arg2) implements IRQuadruple {
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
		return format("\"" + this.arg1.name() + "\"" + "<" + this.arg2.value() + ">");
	}
}
