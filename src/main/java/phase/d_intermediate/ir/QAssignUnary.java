package phase.d_intermediate.ir;

/**
 * Opérateur unaire : {@code result = op arg1}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param op     l'opérateur.
 * @param arg1   l'opérande.
 * @param result le résultat.
 */
public record QAssignUnary(compil.EnumOper op, IRVariable arg1, IRVariable result) implements IRQuadruple {
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
		return format(this.result.name() + " = " + this.op + " " + this.arg1.name());
	}
}
