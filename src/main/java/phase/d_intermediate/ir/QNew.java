package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Instanciation d'objet : {@code result = new arg1}.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param arg1   la classe de l'objet (Nom).
 * @param result l'adresse de l'objet crée.
 */
public record QNew(IRLabel arg1, IRVariable result) implements IRQuadruple {
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
		return format(this.result.name() + " = new " + this.arg1.name());
	}
}
