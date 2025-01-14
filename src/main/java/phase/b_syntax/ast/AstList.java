package phase.b_syntax.ast;

import java.util.ArrayList;
import java.util.List;

import compil.util.AstLocations;

/**
 * Nœud de l'AST avec enfants homogènes. <br>
 * Construction itérative avec la méthode {@link #add}.
 * 
 * <p>This class cannot be a record: possible to write {@code new AstList<>()} when
 * a class, but not possible when a record.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param <R> le type d'enfant.
 */
public class AstList<R extends AstNode> implements AstNode {
	/**
	 * Un nom pour le debugging. Par défaut le nom de la classe instanciante
	 */
	private final String label;
	/**
	 * La liste des nœuds enfant.
	 */
	private final java.util.List<AstNode> enfants;
	/**
	 * La position dans le fichier source : début et fin.
	 */
	private AstLocations locations;

	/** Construit une liste vide. */
	public AstList() {
		label = AstList.class.getSimpleName();
		enfants = new ArrayList<>();
		locations = new AstLocations();
	}

	/**
	 * Construit la liste des enfants par itération.
	 * 
	 * @param node le nœud ajouté en fin de liste
	 * @return les enfants avec le nœud en argument à la fin.
	 */
	public AstList<R> add(final R node) {
		this.addEnfants(node);
		return this;
	}

	@Override
	public void accept(final AstVisitor v) {
		v.visit(this);
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public List<AstNode> enfants() {
		return enfants;
	}

	@Override
	public AstLocations locations() {
		return locations;
	}
	
	@Override
	public String toString() {
		return print();
	}
}
