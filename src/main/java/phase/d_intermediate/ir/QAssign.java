package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Opérateur binaire : {@code result = arg1 op arg2}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param op     l'opérateur.
 * @param arg1   l'opérande gauche.
 * @param arg2   l'opérande droite.
 * @param result le résultat.
 */
public record QAssign(EnumOper op, IRVariable arg1, IRVariable arg2, IRVariable result) implements IRQuadruple {
	@Override
	public void accept(final IRvisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return format(this.result.name() + " = " + this.arg1.name() + " " + this.op + " " + this.arg2.name());
	}
}
