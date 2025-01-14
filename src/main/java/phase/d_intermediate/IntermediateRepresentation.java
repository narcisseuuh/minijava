package phase.d_intermediate;

import java.util.ArrayList;
import java.util.List;

import phase.c_semantic.SemanticTree;
import phase.c_semantic.symtab.Scope;
import phase.d_intermediate.ir.IRConst;
import phase.d_intermediate.ir.IRLabel;
import phase.d_intermediate.ir.IRQuadruple;
import phase.d_intermediate.ir.IRTempVar;

/**
 * Représentation Intermédiaire.
 * <ul>
 * <li>programme : séquence d'instructions ({@link IRQuadruple}).
 * <li>table des symboles de l'AST.
 * <li>table des variables IR : labels, constantes, temporaires.
 * </ul>
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 * 
 * @param program   Le programme intermédiaire = séquence d'instructions.
 * @param rootScope La racine de la table des symboles de l'AST.
 * @param tempos    La liste des Variables Tempos.
 * @param consts    La liste des Constantes.
 * @param labels    La liste des Labels.
 */
public record IntermediateRepresentation(List<IRQuadruple> program, Scope rootScope, List<IRTempVar> tempos,
        List<IRConst> consts, List<IRLabel> labels) {
    /**
     * Construit la forme intermédiaire avec l'arbre des portées transmis par
     * argument.
     * 
     * @param semanticTree l'arbre de sémantique.
     */
    public IntermediateRepresentation(final SemanticTree semanticTree) {
        this(new ArrayList<>(), semanticTree.rootScope(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Crée une variable temporaire IR. Nom de la variable auto-généré
     * 
     * @param scope le nom de la méthode courante.
     * @return la variable IR.
     */
    public IRTempVar createTemp(final String scope) {
        final IRTempVar v = new IRTempVar(scope);
        this.tempos.add(v);
        return v;
    }

    /**
     * Crée une constante IR. Nom de la variable {@code = value.toString()}.
     * 
     * @param value la constante entière.
     * @return la variable IR.
     */
    public IRConst createConst(final int value) {
        final IRConst v = new IRConst(value);
        this.consts.add(v);
        return v;
    }

    /**
     * Crée un label temporaire. Nom de label auto-généré.
     * 
     * @return la variable IR.
     */
    public IRLabel createLabel() {
        final IRLabel v = new IRLabel();
        this.labels.add(v);
        return v;
    }

    /**
     * Crée un label de méthode.
     * 
     * @param name le nom de la méthode.
     * @return la variable IR.
     */
    public IRLabel createLabel(final String name) {
        final IRLabel v = new IRLabel(name);
        this.labels.add(v);
        return v;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (IRQuadruple q : this.program) {
            sb.append(q).append(System.lineSeparator());
        }
        sb.append("= Répresentation intermédiaire, variables temporaires : ").append(tempos);
        sb.append(System.lineSeparator());
        sb.append("= Répresentation intermédiaire, labels                : ").append(labels);
        sb.append(System.lineSeparator());
        sb.append("= Répresentation intermédiaire, constantes            : ").append(consts);
        return sb.toString();
    }
}
