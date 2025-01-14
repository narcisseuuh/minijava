package compil.util;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * Classe représentant les positions dans le fichier source analysé.
 * 
 * @author Denis Conan
 */
public class AstLocations {
	/**
	 * La localisation dans le fichier source : Debut.
	 */
	private Location start;
	/**
	 * La localisation dans le fichier source : Fin.
	 */
	private Location stop;

	/**
	 * constructeur par défaut. Les positions ne sont pas connues lorsque le nœud
	 * est construit.
	 */
	public AstLocations() {
		// nop
	}

	/**
	 * assigne les positions.
	 * 
	 * @param start le début.
	 * @param stop  la fin?
	 */
	public void setLocations(final Location start, final Location stop) {
		this.start = start;
		this.stop = stop;
	}

	/**
	 * obtient la position début.
	 * 
	 * @return la position du début.
	 */
	public Location start() {
		return start;
	}

	/**
	 * obtient la position fin.
	 * 
	 * @return la position de la fin.
	 */
	public Location stop() {
		return stop;
	}
}
