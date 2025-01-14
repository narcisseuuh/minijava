# Compilateur Minijava

Compilateur du langage Minijava d'Andrew J. Appel, "Modern Compiler Implémentation in Java" vers l'assembleur MIPS.

Code en Java avec utilisation de JFlex et CUP.

Outillage : Make ou Ant, Eclipse (ou Intellij).

- Auteur : Pascal Hennequin
- Version : 2023
- sous-version : Final
- Site du Cours : CSC4251_4252
  <http://www-inf.telecom-sudparis.eu/COURS/CSC4251_4252/> 

### Version "Final"
Solution complète du projet Minijava correspondant au
travail à rendre dans le Cours et bonus variés

Inclus :
- Tableaux d'entiers et de booléens
- Contrôle de débordement de tableau (et contrôle taille >0 pour newArray)
- Fonctions avec plus de 4 arguments
- Optimisation des registres (élimination de recopies)
- Détection des identificateurs non définis, ou non utilisés
- Optimisation des constantes et expressions booléennes dans Intermediate
- Moins unaire sur entiers littéraux

### Différences avec la version originale "modern compiler" (A. J. Appel)
- ` ClassBody := (Variable | Method)* ` remplace ` Variable* Method* `
- ` MethodBody := (Variable | Statement)* `  remplace  ` Variable* Statement* ` 
- Variables locales de bloc : `Block := "{" (Variable | Statement)* "}"` remplace  `{"  Statement* "}` 
- Une classe racine implicite ` Object ` avec une méthode ` Object.equals(Object) `
- Gestion partielle de l'héritage, du polymorphisme et de la redéfinition (...)
- Pas de liaison dynamique (tardive) sur les méthodes
- Analyse sémantique : Contrôle de type, Détection boucle dans l'héritage, Détection identificateurs "Unused", "Undef" et "Already defined"  
- Génération de code : Optimisation et Allocation de registre "simpliste", pas de contrôle de flow
- Méthode avec plus de 3 paramètres : passage des paramètres par registres (0 à 3) et pile (4 à N)
- Initialisation des variables (?)


