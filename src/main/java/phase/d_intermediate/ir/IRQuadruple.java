package phase.d_intermediate.ir;

import compil.EnumOper;

/**
 * Classe abstraite mère des Instructions Intermédiaires. <br>
 * Utilise un "Code à 3 adresses" : <br>
 * {@code [EnumOper op, IRvariable arg1, IRvariable arg2, IRvariable result]}
 * <br>
 * avec l'interprétation par défaut : {@code result = op(arg1, arg2)}
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public interface IRQuadruple {
	/**
	 * L'opération de l'instruction intermédiaire.
	 * 
	 * @return l'énumérateur de l'opération de l'instruction intermédiaire.
	 */
	EnumOper op();

	/**
	 * Le premier argument de l'instruction intermédiaire.
	 * 
	 * @return le premier argument.
	 */
	IRVariable arg1();

	/**
	 * Le deuxième argument de l'instruction intermédiaire.
	 * 
	 * @return le deuxième argument.
	 */
	IRVariable arg2();

	/**
	 * Le troisième argument de l'instruction intermédiaire.
	 * 
	 * @return le troisième argument.
	 */
	IRVariable result();

	/**
	 * Réalise le patron de conception "Visitor".
	 * 
	 * @param v le visiteur.
	 */
	void accept(IRvisitor v);

	/**
	 * Retourne une forme textuelle de l'instruction.
	 * 
	 * @return la chaîne de caractères.
	 */
	default String print() {
		return format("");
	}

	/**
	 * Formatage d'une instruction IR.
	 * 
	 * @param abrev impression abrégée dans les classes concrète.
	 * @return l'instruction IR.
	 */
	default String format(final String abrev) {
		return String.format("%-25s | %s", abrev,
				this.getClass().getSimpleName() + "[" + op() + ", " + arg1() + ", " + arg2() + ", " + result() + "]");
	}
}
