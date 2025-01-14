package phase.e_codegen;

import phase.d_intermediate.IntermediateRepresentation;

/**
 * Génération du code assembleur. Enchaîne l'allocation mémoire, la traduction
 * de la forme intermédiaire, et la concaténation du Runtime MIPS.
 * 
 * @author Pascal Hennequin
 */
public class CodeGen {
	/**
	 * La forme intermédiaire en entrée.
	 */
	private final phase.d_intermediate.IntermediateRepresentation ir;
	/**
	 * Le fichier MIPS en sortie.
	 */
	private final String outfile;

	/**
	 * Construit la génération de code MIPS.
	 * 
	 * @param ir      la forme intermédiaire.
	 * @param outfile le nom du fichier MIPS en sortie.
	 */
	public CodeGen(final IntermediateRepresentation ir, final String outfile) {
		this.ir = ir;
		this.outfile = outfile;
	}

	/**
	 * Exécute la génération de code MIPS.
	 * 
	 * @throws compil.util.CompilerException si erreur sur le fichier de sortie.
	 */
	public void execute() {
		compil.util.Debug.log("= Allocation Mémoire");
		final Allocator allocator = new Allocator(ir).execute();
		try (MipsWriter mipsWriter = new MipsWriter(outfile)) { // try-with-resources
			compil.util.Debug.log("= Traduction IR to MIPS -> " + this.outfile);
			new ToMips(ir, allocator, mipsWriter).execute(); // new ToMips
			compil.util.Debug.log("= Édition de lien dans " + outfile);
			new LinkRuntime(mipsWriter).execute();
		} catch (java.io.FileNotFoundException e) {
			throw new compil.util.CompilerException(e.getMessage());
		}
	}
}
