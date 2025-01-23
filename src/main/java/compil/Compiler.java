package compil;

import compil.util.CompilerException;
import compil.util.Debug;
import phase.b_syntax.ast.Axiom;
import phase.c_semantic.Semantic;
import phase.c_semantic.SemanticTree;
import phase.d_intermediate.Intermediate;
import phase.d_intermediate.IntermediateRepresentation;

/**
 * Point d'entrée du compilateur Minijava.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class Compiler {
    /**
     * L'argument par défaut du main (le fichier à compiler).
     */
    public static final String FILE = "input.txt";
    /**
     * Si {@code true}, exécution Mars sur le résultat de la compilation.
     */
    public static final boolean RUNMARS = true;
    /**
     * Si {@code true}, arrête silencieusement (sans erreurs) après la phase de
     * l'analyse syntaxique. Utile lors de la construction par phase du compilateur
     * et pour les stress tests de l'analyse syntaxique.
     */
    private static boolean stopAfterStepSyntax = false;
    /**
     * Si {@code true}, arrête silencieusement (sans erreurs) après la phase de
     * l'analyse sémantique. Utile lors de la construction par phase du compilateur
     * et pour les tests de l'analyse sémantique.
     */
    private static boolean stopAfterStepSemantic = false;
    /**
     * Si {@code true}, arrête silencieusement (sans erreurs) après la phase de
     * la génération de la forme intermédiaire. Utile lors de la construction par phase du compilateur
     * et pour les test.
     */
    private static boolean stopAfterIR = false;

    /**
     * Si {@code true}, arrête silencieusement (sans erreurs) après l'étape de
     * génération du code MIPS. Utile lors de la construction par phase du
     * compilateur et pour les tests de l'analyse sémantique.
     */
    private static boolean stopAfterStepGeneration = false;
    /**
     * Le fichier source en entrée.
     */
    private final String infile;
    /**
     * Le fichier MIPS en sortie.
     */
    private final String outfile;

    /**
     * Construit une compilation. {@code outfile=basename(infile).mips}.
     * 
     * @param infile le fichier source en entrée.
     */
    public Compiler(final String infile) {
        this.infile = infile;
        final int dot = infile.lastIndexOf('.');
        this.outfile = ((dot == -1) ? infile : infile.substring(0, dot)) + ".mips";
    }

    /**
     * Construit une compilation. (Alt.)
     * 
     * @param infile  le fichier Minijava en entrée.
     * @param outfile le fichier MIPS en sortie
     */
    public Compiler(final String infile, final String outfile) {
        this.infile = infile;
        this.outfile = outfile;
    }

    /**
     * Demande l'arrêt après l'analyse lexicale.
     */
    public static void stopAfterSyntax() {
        Compiler.stopAfterStepSyntax = true;
    }

    /**
     * Demande la continuation après l'analyse lexicale.
     */
    public static void doNotStopAfterSyntax() {
        Compiler.stopAfterStepSyntax = false;
    }

    /**
     * Demande l'arrêt après l'analyse sémantique.
     */
    public static void stopAfterSemantic() {
        Compiler.stopAfterStepSemantic = true;
    }

    /**
     * Demande la continuation après l'analyse sémantique.
     */
    public static void doNotStopAfterSemantic() {
        Compiler.stopAfterStepSemantic = false;
    }

    /**
     * Demande l'arrêt après la génération du code MIPS.
     */
    public static void stopAfterGeneration() {
        Compiler.stopAfterStepGeneration = true;
    }

    /**
     * Demande l'arrêt après la génération de la représentation intermédiaire..
     */
    public static void stopAfterIR() {
        Compiler.stopAfterIR = true;
    }

    /**
     * Demande la continuation après la génération de la représentation intermédiaire.
     */
    public static void doNotStopAfterIR() {
        Compiler.stopAfterIR = false;
    }

    /**
     * Demande la continuation après la génération du code MIPS.
     */
    public static void doNotStopAfterGeneration() {
        Compiler.stopAfterStepGeneration = false;
    }

    /**
     * Exécute la compilation.
     */
    public void execute() {
        try {
            Debug.log("=== Phase A Analyse Lexicale et Phase B Syntaxique ===");
            Debug.log("=== new Axiom                                      ===");
            Axiom axiom = new phase.b_syntax.Syntax(this.infile).execute();
            //Compiler.stopAfterSyntax();
            if (stopAfterStepSyntax) {
                Debug.toBeContinued();
                return;
            }
            Debug.log("=== Phase C Analyse Sémantique ===");
            Debug.log("=== new SemanticTree           ===");
            SemanticTree semanticTree = new Semantic(axiom).execute();
            //Compiler.stopAfterSemantic();
            if (stopAfterStepSemantic) {
                Debug.toBeContinued();
                return;
            }
            Debug.log("=== Phase D Génération de la Représentation Intermédiaire ===");
            Debug.log("=== new IntermediateRepresentation                        ===");
            IntermediateRepresentation ir = new Intermediate(axiom, semanticTree).execute();
            Compiler.stopAfterIR();
            if (stopAfterIR) {
                Debug.toBeContinued();
                return;
            }
            Debug.log("=== Phase E Génération de Code ===");
            new phase.e_codegen.CodeGen(ir, outfile).execute();
            Compiler.stopAfterGeneration();
            if (stopAfterStepGeneration) {
                Debug.toBeContinued();
                return;
            }
            if (RUNMARS) { // peut-être pas ici !
                Debug.log("=== Exécution Mars de " + outfile + " ===");
                final int returnedValue = new ProcessBuilder("java", "-jar", "lib/mars.jar", "nc", outfile)
                        .inheritIO().start().waitFor();
                System.out.println(returnedValue);
                if (returnedValue != 0) {
                    throw new CompilerException(
                            "Error when executing MIPS program: exit status of MARS is " + returnedValue);
                }
            }
        } catch (CompilerException | java.io.IOException e) {
            Debug.logErr("Compilation aborted : " + e.getMessage());
            throw new CompilerException("Compilation aborted : " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Exécution en ligne de commande de la compilation.
     * 
     * @param args une liste de fichiers Minijava, si vide {@code Debug.FILE}.
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            new Compiler(FILE).execute();
        }
        for (String infile : args) {
            new Compiler(infile).execute();
        }
    }
}
