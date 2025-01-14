package phase.d_intermediate.ir;

/**
 * Variable IR : Constante.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param value La valeur de la constante.
 */
public record IRConst(Integer value) implements IRVariable {

	@Override
	public String name() {
		return value().toString();
	}

	@Override
	public String type() {
		return "IRConst";
	}

	@Override
	public String toString() {
		return "c_" + name();
	}
}
