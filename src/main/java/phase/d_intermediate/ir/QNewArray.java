package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Instanciation de tableau : {@code result = new arg1[arg2]}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1   le type de tableau (pas utilisé).
 * @param arg2   la taille tableau.
 * @param result l'adresse du tableau crée.
 */
public record QNewArray(IRLabel arg1, IRVariable arg2, IRVariable result) implements IRQuadruple {
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
		return format(this.result.name() + " = new " + this.arg1.name() + "[" + this.arg2.name() + "]");
	}
}
