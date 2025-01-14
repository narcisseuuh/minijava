package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Taille de tableau : {@code result = length(arg1)}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1   l'adresse du tableau
 * @param result la taille en retour
 */
public record QLength(IRVariable arg1, IRVariable result) implements IRQuadruple {
	@Override
	public EnumOper op() {
		return null;
	}

	@Override
	public IRVariable arg2() {
		return null;
	}

	@Override
	public void accept(final IRvisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return format(this.result.name() + " = length(" + this.arg1.name() + ")");
	}
}
