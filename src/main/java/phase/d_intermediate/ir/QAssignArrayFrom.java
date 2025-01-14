package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Lecture dans un tableau : {@code result = arg1[arg2]}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1   l'adresse du tableau.
 * @param arg2   l'index dans le tableau.
 * @param result la valeur en retour.
 */
public record QAssignArrayFrom(IRVariable arg1, IRVariable arg2, IRVariable result) implements IRQuadruple {
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
		return format(this.result.name() + " = " + this.arg1.name() + "[" + this.arg2.name() + "]");
	}
}
