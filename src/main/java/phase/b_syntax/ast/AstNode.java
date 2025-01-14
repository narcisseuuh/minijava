package phase.b_syntax.ast;

import java.util.Iterator;
import java.util.List;

import compil.util.AstLocations;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * La classe abstraite ancêtre pour l'AST.
 * <ul>
 * <li>Construction VarArgs
 * <li>Itérable : {@code for (AstNode enfants : parent) }
 * <li>Impression de l'arbre : {@link #toPrint}
 * <li>Gestion des positions dans le source : {@link #addPosition}
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public interface AstNode extends Iterable<AstNode> {
	/**
	 * Un nom pour le debugging. Par défaut le nom de la classe instanciante.
	 * 
	 * @return le label du nœud.
	 */
	String label();

	/**
	 * La liste des nœuds enfants.
	 * 
	 * @return les enfants du nœud.
	 */
	List<AstNode> enfants();

	/**
	 * Les positions dans le fichier source.
	 * 
	 * @return les positions de début et de fin du nœud dans le fichier source.
	 */
	AstLocations locations();

	/**
	 * La localisation dans le fichier source : Début.
	 * 
	 * @return la position de début du nœud dans le fichier source.
	 */
	default Location start() {
		return locations().start();
	}

	/**
	 * La localisation dans le fichier source : Fin.
	 * 
	 * @return la position de fin du nœud dans le fichier source.
	 */
	default Location stop() {
		return locations().stop();
	}

	/**
	 * Construit les enfants par itération, uniquement pour {@link AstList}.
	 * 
	 * @param enfant le nœud enfant à ajouter.
	 */
	default void addEnfants(final AstNode... enfant) {
		for (AstNode e : enfant) {
			enfants().add(e);
		}
	}

	/**
	 * Réalise le patron de conception "Visitor".
	 * 
	 * @param v le visiteur.
	 */
	void accept(AstVisitor v);

	/**
	 * Itération sur les enfants. <br>
	 * {@code AstNode p; for (AstNode enfant : p) ...}.
	 */
	@Override
	default Iterator<AstNode> iterator() {
		return enfants().iterator();
	}

	/**
	 * Donne le nombre de nœuds enfants.
	 * 
	 * @return le nombre d'enfants.
	 */
	default int nbChildren() {
		return enfants().size();
	}

	/**
	 * Gestion optionnelle des positions des symboles dans le fichier source. <br>
	 * Exemple dans CUP :
	 * 
	 * <pre>
	 * x ::= A:a .. Z:z {: RESULT = new Node(A,..,Z);
	 *                    RESULT.addPosition(axleft,zxright); :}
	 * </pre>
	 * 
	 * @param left  la position de début du symbole.
	 * @param right la position de fin du symbole.
	 */
	default void addPosition(final Location left, final Location right) {
		locations().setLocations(left, right);
	}

	/**
	 * Imprime un noeud.
	 * 
	 * @return la chaîne de caractères représentant le contenu du nœud.
	 */
	default String print() {
		if (!compil.util.Debug.LOCATION) {
			return label();
		}
		if (start() == null && stop() == null) {
			return label();
		}
		return label() + "[" + locStr(start()) + "-" + locStr(stop()) + "]";
	}

	/**
	 * "Redéfinition" de {@code Location.toString()}.
	 * 
	 * @param l la Location à imprimer.
	 * @return la chaîne imprimable.
	 */
	static String locStr(final Location l) {
		return l.getLine() + "/" + l.getColumn() + "(" + l.getOffset() + ")";
	}

	/**
	 * Imprime l'Arbre de Syntaxe Abstraite.
	 * 
	 * @return la représentation textuelle de L'AST.
	 */
	default String toPrint() {
		final StringBuilder sb = new StringBuilder();
		Print.astPrint(this, "", sb);
		return sb.toString();
	}

	/**
	 * Impression récursive de l'AST.
	 */
	final class Print {
		/**
		 * Les jeux de caractères pour impression d'arbre.
		 * <ul>
		 * <li>ASCII : "|-", "| ", "\\-", " "
		 * <li>Unicode: "\u251c\u2500","\u2502\u0020","\u2514\u2500","\u0020\u0020"
		 * <li>Unicode Compact: "\u251c", "\u2502", "\u2514", "\u0020"
		 * </ul>
		 */
		private static final String[][] CSS = {{"|-", "| ", "\\-", "  "}, // ASCII
				{"├─", "│ ", "└─", "  "}, // Unicode
				{"├", "│", "└", " "} // Unicode Compact
		};
		/**
		 * Le jeu de caractères pour impression d'arbre.
		 */
		private static final String[] CS = CSS[1]; // Unicode

		/**
		 * Constructeur pour la forme.
		 */
		private Print() {
		}

		/**
		 * Imprime récursivement un arbre à la ASCII art.
		 * 
		 * @param n      le nœud racine.
		 * @param indent la chaîne d'indentation courante.
		 * @param sb     le {StringBuilder} en sortie.
		 */
		private static void astPrint(final AstNode n, final String indent, final StringBuilder sb) {
			sb.append(n).append(System.lineSeparator());
			for (java.util.Iterator<AstNode> it = n.iterator(); it.hasNext();) {
				final AstNode f = it.next();
				final int indice = it.hasNext() ? 0 : 2; /* last or not? */
				sb.append(indent).append(CS[indice]);
				astPrint(f, indent + CS[indice + 1], sb);
			}
		}
	}
}
