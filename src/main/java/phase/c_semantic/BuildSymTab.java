package phase.c_semantic;

import compil.util.Debug;
import phase.b_syntax.ast.*;
import phase.c_semantic.symtab.Info;
import phase.c_semantic.symtab.InfoKlass;
import phase.c_semantic.symtab.InfoMethod;
import phase.c_semantic.symtab.InfoVar;
import phase.c_semantic.symtab.Scope;

/**
 * Construction de la table des symboles.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class BuildSymTab extends AstVisitorDefault {
    /**
     * Le nom de la classe « Object ».
     */
    protected static final String OBJECT = "Object";
    /**
     * La structure de données de l'analyse sémantique.
     */
    protected final SemanticTree semanticTree;
    /**
     * L'attribut hérité "Scope". Entrée dans la table des symboles du nœud
     */
    protected Scope currentScope;
    /**
     * L'attribut hérité "Klass". Fournit le type de la variable {@code this}.
     */
    protected InfoKlass currentKlass;
    /**
     * {@code true} si des erreurs sémantiques détectées.
     */
    protected boolean error;

    /**
     * Construit la génération de la table des symboles (passe 1).
     * 
     * @param semanticTree l'arbre sémantique.
     */
    public BuildSymTab(final SemanticTree semanticTree) {
        this.error = false;
        this.semanticTree = semanticTree;
        this.currentScope = semanticTree.rootScope();
        this.currentKlass = null;
    }

    /**
     * Réalise la génération de la table des symboles (passe 1). Intègre la
     * déclaration implicite de la classe {@code Object}, et les déclarations
     * implicites de la variable {@code this} dans les méthodes.
     * 
     * @return {@code true} si des erreurs signalées de redéfinition.
     */
    public boolean execute() {
        addObjectKlass();
        semanticTree.axiom().accept(this);
        if (Debug.SYMTAB) {
            Debug.log("= Table des symboles (passe1)");
            Debug.log(semanticTree.rootScope().toPrint());
        }
        return this.error;
    }

    /**
     * Crée la classe {@code Object} dans la table des symboles. La classe est
     * requise comme racine de la hiérarchie des classes. La méthode
     * {@code Object.equals()} est ajoutée pour l'exemple.
     */
    protected void addObjectKlass() {
        Scope sc = semanticTree.rootScope();
        final InfoKlass kl = new InfoKlass(OBJECT, null);
        sc = newKlassScope(sc, kl);
        final InfoMethod m = new InfoMethod("boolean", "equals", new InfoVar("this", OBJECT), new InfoVar("o", OBJECT));
        newMethodScope(sc, m);
    }

    // Helpers
    /**
     * Positionne l'attribut "Klass".
     * 
     * @param n     le nœud AST.
     * @param klass la classe englobante.
     */
    protected void setKlass(final AstNode n, final InfoKlass klass) {
        semanticTree.attrKlass().set(n, klass);
    }

    /**
     * Retourne l'attribut "Klass".
     * 
     * @param n le nœud AST.
     * @return la classe englobante.
     */
    protected InfoKlass getKlass(final AstNode n) {
        return semanticTree.attrKlass().get(n);
    }

    /**
     * Positionne l'attribut "Scope".
     * 
     * @param n  le nœud AST.
     * @param sc la portée.
     */
    protected void setScope(final AstNode n, final Scope sc) {
        semanticTree.attrScope().set(n, sc);
    }

    /**
     * Retourne l'attribut "Scope".
     * 
     * @param n le nœud AST.
     * @return la portée.
     */
    protected Scope getScope(final AstNode n) {
        return semanticTree.attrScope().get(n);
    }

    /**
     * Ajoute une déclaration de classe et crée une portée de classe.
     * 
     * @param sc la portée courante.
     * @param kl la définition de classe.
     * @return la portée pour la nouvelle classe.
     */
    protected Scope newKlassScope(final Scope sc, final InfoKlass kl) {
        checkRedef(sc.insertKlass(kl));
        final Scope enfants = new Scope(sc, kl.getName());
        kl.setScope(enfants);
        return enfants;
    }

    /**
     * Ajoute une déclaration de méthode et crée 2 nouvelles portées. Ajoute aussi
     * les paramètres formels dans la portée intermédiaire
     * 
     * @param sc la portée courante.
     * @param m  la définition de la méthode.
     * @return la portée pour la nouvelle méthode.
     */
    protected Scope newMethodScope(final Scope sc, final InfoMethod m) {
        checkRedef(sc.insertMethod(m));
        final Scope enfants = new Scope(sc, m.getName() + "_args");
        for (InfoVar v : m.getArgs()) {
            checkRedef(enfants.insertVariable(v));
        }
        final Scope pf = new Scope(enfants, m.getName());
        m.setScope(pf);
        return pf;
    }

    /**
     * Teste les redéfinitions de symboles dans une même portée.
     * <p>
     * Redéfinition = retour non {@code null} lors de l'ajout d'un symbole.
     * <p>
     * Erreur signalée avec traitement différé.
     * 
     * @param info la déclaration retournée par un {@code HashMap.add()}.
     * @return {@code true} si la déclaration est écrasée.
     */
    protected boolean checkRedef(final Info info) {
        if (info == null) {
            return false;
        }
        Debug.logErr("BuildSymtab : Duplication d'identificateur " + info);
        error = true;
        return true;
    }

    ////////////// Visit ////////////////////////
    
    /**
     * Redéfinition de la visite par défaut : Intégration de l'héritage par défaut
     * des attributs hérités (Scope, Klass).
     */
    @Override
    public void defaultVisit(final AstNode n) {
        setKlass(n, currentKlass);
        setScope(n, currentScope);
        for (AstNode f : n) {
            f.accept(this);
        }
        currentKlass = getKlass(n);
        currentScope = getScope(n);
    }

    // Visites Spécifiques :
    // - Création de portées : KlassMain, Klass, Method, StmtBlock
    // - Déclarations : KlassMain, Klass, Method, Variable, Formal (in Method)

    // Visites non écrites dans le squelette de MiniJAVA

    @Override
    public void visit(final Klass n) {
        //checkRedef(currentScope.lookupKlass(n.klassId().name()));
        setKlass(n, currentKlass);
        setScope(n, currentScope);
        currentKlass = new InfoKlass(n.klassId().name(), n.parentId().name());
        System.out.println(getScope(n).lookupKlass(n.klassId().name()));
        this.currentScope = newKlassScope(currentScope, currentKlass);
        n.klassId().accept(this);
        n.parentId().accept(this);
        n.vars().accept(this);
        n.methods().accept(this);
        currentKlass = getKlass(n);
        currentScope = getScope(n);
    }

    @Override
    public void visit(final Method n) {
        setKlass(n, currentKlass);
        setScope(n, currentScope);
        // les paramètres formels explicites
        final InfoVar[] formals = new InfoVar[n.fargs().nbChildren() + 1];
        int index = 1;
        for (AstNode f : n.fargs()) {
            formals[index] = new InfoVar(((Formal) f).varId().name(), ((Formal) f).typeId().name());
            index++;
        }
        // le paramètre implicite "this"
        formals[0] = new InfoVar("this", getKlass(n).getName());
        // la définition de la méthode
        final InfoMethod m = new InfoMethod(n.returnType().name(), n.methodId().name(), formals);
        currentScope = newMethodScope(currentScope, m);
        n.returnType().accept(this);
        n.methodId().accept(this);
        n.fargs().accept(this); // ne fait rien !
        n.vars().accept(this);
        n.stmts().accept(this);
        n.returnExp().accept(this);
        currentKlass = getKlass(n);
        currentScope = getScope(n);
    }

    @Override
    public void visit(final Variable n) {
        setKlass(n, currentKlass);
        setScope(n, currentScope);
        final InfoVar v = new InfoVar(n.varId().name(), n.typeId().name());
        checkRedef(getScope(n).insertVariable(v));
        currentKlass = getKlass(n);
        currentScope = getScope(n);
    }

    @Override
    public void visit(final StmtBlock n) {
        setKlass(n, currentKlass);
        setScope(n, currentScope);
        currentScope = new Scope(currentScope);
        n.vars().accept(this);
        n.stmts().accept(this);
        currentKlass = getKlass(n);
        currentScope = getScope(n);
    }
}
