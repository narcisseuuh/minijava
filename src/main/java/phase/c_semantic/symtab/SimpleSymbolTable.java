package phase.c_semantic.symtab;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Table de symboles élémentaire en utilisant {@link HashMap}.
 * 
 * @param <R> le type d'information
 */
public class SimpleSymbolTable<R extends Info> implements SymbolTable<String, R> {
	/**
	 * La Table de symbole.
	 */
	private Map<String, R> symbols;

	/**
	 * Construit une table de symbole vide.
	 */
	public SimpleSymbolTable() {
		this.symbols = new HashMap<>();
	}

	@Override
	public R lookup(final String name) {
		return this.symbols.get(name);
	}

	@Override
	public R insert(final String name, final R info) {
		return this.symbols.put(name, info);
	}

	@Override
	public Collection<R> getInfos() {
		return this.symbols.values();
	}

	@Override
	public String toString() {
		return "SimpleTable [" + this.symbols + "]";
	}
}
