package phase.c_semantic.symtab;

import java.util.Collection;

/**
 * Interface standard d'une table de symboles.
 * 
 * @param <T> le type de « Nom » (String).
 * @param <R> le type d'« Information ».
 */
public interface SymbolTable<T, R extends Info> {
	/**
	 * Recherche dans la table.
	 * 
	 * @param name le nom.
	 * @return l'information trouvée, ou {@code null} si absent.
	 */
	R lookup(T name);

	/**
	 * Ajout dans la table.
	 * 
	 * @param name le nom.
	 * @param info l'information ajoutée.
	 * @return l'information précédente, si déjà existante.
	 */
	R insert(T name, R info);

	/**
	 * La liste des symboles de la table.
	 * 
	 * @return les informations.
	 */
	Collection<R> getInfos();
}
