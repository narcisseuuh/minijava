package phase.c_semantic.symtab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import compil.util.IndentWriter;

/**
 * Table des Symboles.
 * <ul>
 * <li>Implémentation plus large que nécessaire pour JAVA ou MiniJAVA</li>
 * <li>Implémentation générale d'un arbre de portées avec recherche
 * récursive</li>
 * <li>3 espaces de nommage séparés : Classe, Méthode, Variable</li>
 * <li>Permet d'intégrer l'héritage des classes JAVA dans l'arbre de portée</li>
 * <li>.....</li>
 * </ul>
 */
public class Scope {
    /**
     * L'héritage Bottom-Up des portées.
     */
    private Scope parent;
    /**
     * La table des variables de la portée.
     */
    private final SymbolTable<String, InfoVar> variables;
    /**
     * La table des méthodes de la portée.
     */
    private final SymbolTable<String, InfoMethod> methods;
    /**
     * La table des classes de la portée.
     */
    private final SymbolTable<String, InfoKlass> klasses;
    // En pratique avec Minijava :
    // - Classes uniquement dans la portée Racine
    // - Méthodes uniquement dans les portées de classe
    // - Variables partout sauf dans la portée Racine
    /**
     * Le Nom de portée. Uniquement pour impression.
     */
    private final String scopeName;
    /**
     * L'arbre Top-Down des portées. Uniquement pour impression.
     */
    private final List<Scope> scopes;

    /**
     * Construit une portée dans l'arbre avec nommage implicite.
     * 
     * @param parent la portée parente.
     */
    public Scope(final Scope parent) {
        this(parent, (parent == null) ? "Root" : parent.scopeName + "_" + parent.scopes.size());
    }

    /**
     * Construit une portée dans l'arbre avec nommage explicite.
     * 
     * @param parent la portée parente.
     * @param name   le nom de la portée.
     */
    public Scope(final Scope parent, final String name) {
        this.scopeName = name;
        this.parent = parent;
        this.variables = new SimpleSymbolTable<>();
        this.methods = new SimpleSymbolTable<>();
        this.klasses = new SimpleSymbolTable<>();
        this.scopes = new ArrayList<>();
        if (parent != null) {
            parent.scopes.add(this);
        }
    }

    // Interface de la table des symboles
    /**
     * Recherche et retourne la déclaration d'une variable. Remonte récursivement
     * l'arbre des portées.
     * 
     * @param name le nom de variable.
     * @return l'information dans la table des symboles, {@code null} si absent.
     */
    public InfoVar lookupVariable(final String name) {
        InfoVar v = null;
        for (Scope s = this; s != null && v == null; s = s.parent) {
            v = s.variables.lookup(name);
        }
        return v;
    }

    /**
     * Ajoute une déclaration de variable dans la portée courante.
     * 
     * @param v l'information ajoutée.
     * @return l'information précédente si déjà existante.
     */
    public InfoVar insertVariable(final InfoVar v) {
        return this.variables.insert(v.name(), v);
    }

    /**
     * Retourne la liste des variables de la portée courante.
     * 
     * @return les variables de la portée courante.
     */
    public Collection<InfoVar> getVariables() {
        return this.variables.getInfos();
    }

    /**
     * Retourne la liste des variables de la portée courante et des sous-portées.
     * 
     * @return les variables de la portée courante et des sous-portées.
     */
    public Collection<InfoVar> getAllVariables() {
        final List<InfoVar> res = new ArrayList<>();
        res.addAll(this.variables.getInfos());
        for (Scope s : this.scopes) {
            res.addAll(s.getAllVariables());
        }
        return res;
    }

    /**
     * Recherche et retourne la déclaration d'une variable. Remonte récursivement
     * l'arbre des portées.
     * 
     * @param name le nom de la méthode.
     * @return l'information dans la table de symbole, {@code null} si absent.
     */
    public InfoMethod lookupMethod(final String name) {
        InfoMethod m = null;
        for (Scope s = this; s != null && m == null; s = s.parent) {
            m = s.methods.lookup(name);
        }
        return m;
    }

    /**
     * Ajoute une déclaration de méthode dans la portée courante.
     * 
     * @param m l'information ajoutée.
     * @return l'information précédente si déjà existante.
     */
    public InfoMethod insertMethod(final InfoMethod m) {
        return this.methods.insert(m.getName(), m);
    }

    /**
     * Retourne la liste des méthodes de la portée courante.
     * 
     * @return les méthodes de la portée courante.
     */
    public Collection<InfoMethod> getMethods() {
        return this.methods.getInfos();
    }

    /**
     * Recherche et retourne la déclaration d'une classe. Remonte récursivement
     * l'arbre des portées.
     * 
     * @param name le nom de classe.
     * @return l'information dans la table de symbole.
     */
    public InfoKlass lookupKlass(final String name) {
        InfoKlass kl = null;
        for (Scope s = this; s != null && kl == null; s = s.parent) {
            kl = s.klasses.lookup(name);
        }
        return kl;
    }

    /**
     * Ajoute une déclaration de classe dans la portée courante.
     * 
     * @param kl l'information ajoutée.
     * @return l'information précédente si déjà existante.
     */
    public InfoKlass insertKlass(final InfoKlass kl) {
        return this.klasses.insert(kl.getName(), kl);
    }

    /**
     * Retourne la liste des classes de la portée courante.
     * 
     * @return les classes de la portée courante.
     */
    public Collection<InfoKlass> getKlasses() {
        return this.klasses.getInfos();
    }

    /**
     * Reconstruit une portée "classe" de la table des symboles. Intégration de
     * l'héritage JAVA à la racine de l'arbre des portées. Seules les portées de
     * classe à la racine peuvent muter.
     * 
     * @param oldParent l'ancien père, doit être la racine.
     * @param newParent le nouveau père.
     * @return {@code true} si modification de l'arbre de portée.
     */
    public boolean addInheritance(final Scope oldParent, final Scope newParent) {
        // mutation uniquement à la racine
        if (this.parent == null || this.parent.parent != null) {
            return false;
        }
        // pas de mutation vers la racine
        if (newParent == null) {
            return false;
        }
        // pas de mutation depuis un parent inconnu
        if (this.parent != oldParent) {
            return false;
        }
        // OK
        this.parent = newParent;
        this.parent.scopes.add(this);
        oldParent.scopes.remove(this);
        return true;
    }

    /**
     * Retourne une représentation textuelle d'une portée.
     */
    @Override
    public String toString() {
        return "Scope " + this.scopeName;
    }

    /**
     * Retourne une représentation textuelle d'une portée et ses ancêtres.
     * 
     * @return une représentation textuelle bottom-up des portées.
     */
    public String toPrintUp() {
        return this.scopeName + " -> " + this.parent.toPrintUp();
    }

    /**
     * Retourne une représentation textuelle de la table des symboles (top-down).
     * 
     * @return une représentation textuelle top-down de la table des symboles.
     */
    public String toPrint() {
        final IndentWriter iw = new IndentWriter();
        treePrint(iw);
        return iw.toString();
    }

    /**
     * Imprime récursivement la table des symboles.
     * 
     * @param iw l'{@link IndentWriter} qui reçoit l'impression.
     */
    private void treePrint(final IndentWriter iw) {
        iw.println("Scope " + this.scopeName);
        iw.indent();
        for (Info i : getKlasses()) {
            iw.println(i);
        }
        for (Info i : getMethods()) {
            iw.println(i);
        }
        for (Info i : getVariables()) {
            iw.println(i);
        }
        for (Scope s : this.scopes) {
            s.treePrint(iw);
        }
        iw.outdent();
    }
}
