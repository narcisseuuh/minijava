package phase.d_intermediate.ir;

/**
 * Visiteur de la forme intermédiaire par défaut. Sortie en erreur pour les
 * méthodes {@code visit()} non redéfinies
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public abstract class IRvisitorDefault implements IRvisitor {
	/**
	 * Visite par défaut.
	 * 
	 * @param n le nœud à visiter.
	 * @throws compil.util.CompilerException si {@code visit(n)} non redéfini.
	 */
	public void defaultVisit(final IRQuadruple n) {
		throw new compil.util.CompilerException("IRvisitor Quadrulple unmanaged :" + n);
	}

	@Override
	public void visit(final QLabelMeth n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QLabel n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QJump n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QJumpCond n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QReturn n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QParam n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QCall n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QCallStatic n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QNew n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QCopy n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QAssign n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QAssignUnary n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QNewArray n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QLength n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QAssignArrayFrom n) {
		defaultVisit(n);
	}

	@Override
	public void visit(final QAssignArrayTo n) {
		defaultVisit(n);
	}
}
