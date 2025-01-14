package phase.c_semantic;

import java.util.HashSet;
import java.util.Set;

import compil.util.Debug;
import phase.c_semantic.symtab.InfoKlass;
import phase.c_semantic.symtab.Scope;

/**
 * Validation de la hiérarchie des classes (sans boucle, racine="Object", ...).
 * <br>
 * Reconstruction de l'arbre des portées pour intégration transparente de
 * l'héritage JAVA dans la table des symboles.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class CheckInheritance {
    /**
     * La structure de données de l'analyse sémantique.
     */
    private final SemanticTree semanticTree;
    /**
     * L'arbre des portées. Entrée et Sortie de la classe.
     */
    private final Scope rootScope;
    /**
     * La classe "Object". Requise comme racine de l'héritage JAVA.
     */
    private final InfoKlass objKlass;
    /**
     * {@code true} si des erreurs sémantiques sont détectées.
     */
    private boolean error;

    /**
     * Construit la validation de l'héritage JAVA et la reconstruction de la table
     * de symbole (passe 2).
     * 
     * @param semanticTree l'arbre sémantique.
     */
    public CheckInheritance(final SemanticTree semanticTree) {
        this.error = false;
        this.semanticTree = semanticTree;
        this.rootScope = semanticTree.rootScope();
        this.objKlass = this.rootScope.lookupKlass("Object");
    }

    /**
     * Réalise la validation de l'héritage JAVA et reconstruit de la table des
     * symboles (passe 2).
     * 
     * Important : « passe 2 » indique que la construction de la table des symboles
     * s'effectue en deux temps, et que, par conséquent, les informations de la
     * table des symboles ne sont pas « immuables », mais « modifiables ». On aurait
     * pu procéder en créant une copie dans une nouvelle table des symboles, la
     * première et la seconde contenant alors des informations « immuables ».
     * 
     * @return {@code true} si des erreurs sémantiques d'héritage JAVA.
     */
    public boolean execute() {
        // Classe racine valide ?
        if (!assume(objKlass != null, "Missing Object class")) {
            return error;
        }
        if (!assume(objKlass.getParent() == null, "Object is not root" + objKlass)) {
            return error;
        }
        // reconstruction de toutes les classes avec branche d'ancêtres valides
        for (InfoKlass kl : rootScope.getKlasses()) {
            if (checkAncestors(kl)) {
                for (InfoKlass k = kl; k != objKlass; k = parent(k)) {
                    k.getScope().addInheritance(rootScope, parent(k).getScope());
                }
            }
        }
        if (Debug.SYMTAB) {
            Debug.log("= Table des Symboles (passe2)");
            Debug.log(semanticTree.rootScope().toPrint());
            Debug.log("= Liste des variables");
            Debug.log(semanticTree.rootScope().getAllVariables());
        }
        if (this.error) {
            Debug.logErr("CheckInheritance : Héritage JAVA invalide");
        }
        return this.error;
    }

    /**
     * Teste une condition d'erreur. Erreurs signalées avec traitement différé en
     * fin d'analyse.
     * 
     * @param condition le test à valider.
     * @param message   le message d'erreur.
     * @return la valeur du test.
     */
    private boolean assume(final boolean condition, final String message) {
        if (!condition) {
            Debug.logErr(message);
            error = true;
        }
        return condition;
    }

    /**
     * Retourne la déclaration de la classe parente.
     * 
     * @param kl la déclaration d'une classe.
     * @return la déclaration de la classe parente.
     */
    private InfoKlass parent(final InfoKlass kl) {
        return rootScope.lookupKlass(kl.getParent());
    }

    /**
     * Vérifie la validité de l'héritage JAVA : branche sans boucles, classes
     * connues, et {@code Object} comme racine. Erreurs signalées avec traitement
     * différé en fin d'analyse.
     * 
     * @param kl la déclaration de classe.
     * @return {@code true} si la classe hérite sans boucle de {@code Object}.
     */
    private boolean checkAncestors(final InfoKlass kl) {
        final Set<InfoKlass> ancestors = new HashSet<>();
        boolean ok = true;
        for (InfoKlass k = kl; ok && k != objKlass; k = parent(k)) {
            ok = assume(ancestors.add(k), "Loop in ancestors from class " + kl)
                    && assume(parent(k) != null, "Unknown ancestor for " + k);
        }
        return ok;
    }
}
