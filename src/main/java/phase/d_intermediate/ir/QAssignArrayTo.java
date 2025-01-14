package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Affectation dans un tableau : {@code result[arg2] = arg1}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1   la valeur à affecter.
 * @param arg2   l'index dans le tableau.
 * @param result l'adresse du tableau.
 */
public record QAssignArrayTo(IRVariable arg1, IRVariable arg2, IRVariable result) implements IRQuadruple {
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
		return format(this.result.name() + "[" + this.arg2.name() + "]" + " = " + this.arg1.name());
	}
}
