
Ce projet est réalisé par :
- Perrin Augustin
- Hostettler Maël

# Notes du binôme pour expliquer le travail réalisé et pour répondre aux questions sur le projet

Cette page au format
[MarkDown](https://docs.gitlab.com/ee/user/rich_text_editor.html#input-rules
"MarkDown") sert (1) à indiquer l'état d'avancement des travaux dans
le projet MiniJAVA, (2) à fournir vos réponses aux questions qui sont
posées dans l'énoncé, et (3) à toute autre information permettant de
mieux comprendre ce qui a été fait, ce qui est en cours de réalisation
sans être finalisé, et ce qui pose problème.

## Phase 1 : Analyse lexicale et syntaxique

### État d'avancement des travaux sur la phase

- [x] spécifications jflex/cup.
- [x] PrettyPrinter.
- [x] analyse lexicale et syntaxique pour les tableaux.

### Commentaires du binôme sur la phase :

1. est-il possible de définir la valeur d'un entier sous la forme octale ?

Non il n'est pas possible de définir un entier en octal. Le 0 en début d'entier est refusé.

2. dans une méthode, peut-on écrire « int a; a = 0; int b; b = n; » ? ou doit-on plutôt écrire « int a; int b; a = 0; b = n; » ?

Dans une méthode, il est préférable de séparer les déclarations de variables des statements. Donc, il est recommandé d'écrire « `int a; int b; a = 0; b = n;` ».

3. est-il possible de mettre un attribut de classe (static) dans la classe qui contient la méthode main ?

Non, il n'est pas possible de mettre un attribut de classe à la classe main :
```java
klassMain ::= CLASS:a ident:b LBRACE PUBLIC STATIC VOID MAIN
              LPAREN STRING LBRACK RBRACK ident:c RPAREN
              LBRACE stmt:d RBRACE RBRACE:z
                {: RESULT = KlassMain.create(b, c, d);
                   RESULT.addPosition(axleft, zxright); :}
```
la classe main ne contient que la méthode main.

4. est-il possible d'avoir une variable locale dans la méthode main ?

Oui, il est possible d'obtenir une variable locale dans la méthode main de la manière suivante :
```java
public static void main(String[] args) {
    { // stmtBlock
        int i; // variable dans methodBody
        ...
    }
}
```

## Phase 2 : Analyse Sémantique : Table des Symboles

### État d'avancement des travaux sur la phase

- [x] construction de la table des symboles (visiteur BuildSymTab, mais aussi les visiteurs DisplayVarDeclarations, DisplayScopes, et DisplayVarDeclarationsInScopes) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [x] construction de la table des symboles du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. lorsque l'on écrit un visiteur héritant du visiteur par défaut AstVisitorDefault, à quoi sert l'appel à la méthode defaultVisit dans les méthodes redéfinies ?

La méthode `defaultVisit` est héritée du visiteur par défaut et va explorer tous les enfants du noeud. Son appel permet donc d'aller visiter tous les noeuds de l'AST.

2. lorsque l'on ajoute un nouveau type de nœud dans l'AST, faut-il modifier le visiteur par défaut AstVisitorDefault ? si oui, pourquoi ?

Non, tous nos noeuds héritent de AstNode, defaultVisitor n'a donc pas besoin d'être modifié.

3. que fait notre compilateur en cas d'erreur dans la gestion de l'héritage (détection d'un cycle) ?

Il renvoie une erreur (c.f. memento)

4. notre compilateur considère-t-il comme une erreur de redéfinition l'écrasement d'un paramètre d'appel par une variable locale ? Qu'en est-il du compilateur javac ?

```java
class kaka {
    public int kuku(int a) {
        int a;
        return 1;
    }
}
```
Notre compilo ne se plaint pas tandis que javac émet un message d'erreur.


## Phase 3 : Analyse Sémantique : Contrôle de Type

### État d'avancement des travaux sur la phase

- [x] contrôle de type (visiteur TypeChecking) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [x] contrôle de type du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. Le compilateur MiniJAVA gère-t-il la surcharge ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;

Non, les opérateurs ne sont définits que pour un type primitif (int ou bool).

2. Le compilateur MiniJAVA gère-t-il la redéfinition ? la liaison dynamique ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;

Non, ni la redéfinition ni la liaison dynamique ne sont géré, on injecte les méthodes et attributs dans la classe enfant et ne supportons pas les @Overloaded ni les redéfinitions de méthodes (overwrite).

3. Dans le code du visiteur TypeChecking, comment voit-on que le type est un attribut synthétisé ?

Oui, on doit visiter les noeuds enfants avant d' "inférer" (sous entendu on a besoin de visiter les enfants avant mais il ne s'agit pas là de réelle inférence, juste d'un besoin d'information).

4. Pourquoi la méthode compareType appelée par la méthode checkType est-elle récursive ?

Elle permet de comparer les types de classe pour trouver un ancêtre commun.

## Phase 4 : Génération de la forme intermédiaire 

### Etat d'avancement des travaux sur la phase

- [x] génération de la forme intermédiaire (visiteur Intermediate).
- [x] génération de la forme intermédiaire du Projet MiniJAVA, c'est-à-dire pour avoir aussi des tableaux. 

### Commentaires du binôme sur la phase :


1. pour traduire les nœuds de l'AST de type ExprNew, que fait-on de l'appel au constructeur ?

On initialise tous les champs à 0 puis on traite le constructeur comme une fonction quelconque.

2. quel est le rôle de l'attribut currentMethod ? comment est-il manipulé ?

Il permet de se rappeler dans quelle méthode nous nous situons, il est très utile, pour créer des variables temporaires dans le bon scope.

3. que fait-on du problème du dangling else ? comment le traduit-on ?

A priori il n'y a pas de dangling else car après un if on attend un unique else, c.f la grammaire de minijava dans le memento.

4. quel usage fait-on des étiquettes/labels ? pour quels types de nœud de l'AST sont-elles utilisées et pourquoi ? (on tiendra compte des bonus si on les a fait)

Les étiquettes servent lors des JUMP en mips, les labels servent à déverminer.

## Phase N : Titre de la phase

### État d'avancement des travaux sur la phase

...

### Commentaires du binôme sur la phase :

Ce projet est réalisé par :
- Perrin Augustin
- Hostettler Maël

# Notes du binôme pour expliquer le travail réalisé et pour répondre aux questions sur le projet

Cette page au format
[MarkDown](https://docs.gitlab.com/ee/user/rich_text_editor.html#input-rules
"MarkDown") sert (1) à indiquer l'état d'avancement des travaux dans
le projet MiniJAVA, (2) à fournir vos réponses aux questions qui sont
posées dans l'énoncé, et (3) à toute autre information permettant de
mieux comprendre ce qui a été fait, ce qui est en cours de réalisation
sans être finalisé, et ce qui pose problème.

## Phase 1 : Analyse lexicale et syntaxique

### État d'avancement des travaux sur la phase

- [x] spécifications jflex/cup.
- [x] PrettyPrinter.
- [x] analyse lexicale et syntaxique pour les tableaux.

### Commentaires du binôme sur la phase :

1. est-il possible de définir la valeur d'un entier sous la forme octale ?

Non il n'est pas possible de définir un entier en octal. Le 0 en début d'entier est refusé.

2. dans une méthode, peut-on écrire « int a; a = 0; int b; b = n; » ? ou doit-on plutôt écrire « int a; int b; a = 0; b = n; » ?

Dans une méthode, il est préférable de séparer les déclarations de variables des statements. Donc, il est recommandé d'écrire « `int a; int b; a = 0; b = n;` ».

3. est-il possible de mettre un attribut de classe (static) dans la classe qui contient la méthode main ?

Non, il n'est pas possible de mettre un attribut de classe à la classe main :
```java
klassMain ::= CLASS:a ident:b LBRACE PUBLIC STATIC VOID MAIN
              LPAREN STRING LBRACK RBRACK ident:c RPAREN
              LBRACE stmt:d RBRACE RBRACE:z
                {: RESULT = KlassMain.create(b, c, d);
                   RESULT.addPosition(axleft, zxright); :}
```
la classe main ne contient que la méthode main.

4. est-il possible d'avoir une variable locale dans la méthode main ?

Oui, il est possible d'obtenir une variable locale dans la méthode main de la manière suivante :
```java
public static void main(String[] args) {
    { // stmtBlock
        int i; // variable dans methodBody
        ...
    }
}
```

## Phase 2 : Analyse Sémantique : Table des Symboles

### État d'avancement des travaux sur la phase

- [x] construction de la table des symboles (visiteur BuildSymTab, mais aussi les visiteurs DisplayVarDeclarations, DisplayScopes, et DisplayVarDeclarationsInScopes) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [x] construction de la table des symboles du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. lorsque l'on écrit un visiteur héritant du visiteur par défaut AstVisitorDefault, à quoi sert l'appel à la méthode defaultVisit dans les méthodes redéfinies ?

La méthode `defaultVisit` est héritée du visiteur par défaut et va explorer tous les enfants du noeud. Son appel permet donc d'aller visiter tous les noeuds de l'AST.

2. lorsque l'on ajoute un nouveau type de nœud dans l'AST, faut-il modifier le visiteur par défaut AstVisitorDefault ? si oui, pourquoi ?

Non, tous nos noeuds héritent de AstNode, defaultVisitor n'a donc pas besoin d'être modifié.

3. que fait notre compilateur en cas d'erreur dans la gestion de l'héritage (détection d'un cycle) ?

Il renvoie une erreur (c.f. memento)

4. notre compilateur considère-t-il comme une erreur de redéfinition l'écrasement d'un paramètre d'appel par une variable locale ? Qu'en est-il du compilateur javac ?

```java
class kaka {
    public int kuku(int a) {
        int a;
        return 1;
    }
}
```
Notre compilo ne se plaint pas tandis que javac émet un message d'erreur.


## Phase 3 : Analyse Sémantique : Contrôle de Type

### État d'avancement des travaux sur la phase

- [x] contrôle de type (visiteur TypeChecking) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [x] contrôle de type du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. Le compilateur MiniJAVA gère-t-il la surcharge ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;

Non, les opérateurs ne sont définits que pour un type primitif (int ou bool).

2. Le compilateur MiniJAVA gère-t-il la redéfinition ? la liaison dynamique ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;

Non, ni la redéfinition ni la liaison dynamique ne sont géré, on injecte les méthodes et attributs dans la classe enfant et ne supportons pas les @Overloaded ni les redéfinitions de méthodes (overwrite).

3. Dans le code du visiteur TypeChecking, comment voit-on que le type est un attribut synthétisé ?

Oui, on doit visiter les noeuds enfants avant d' "inférer" (sous entendu on a besoin de visiter les enfants avant mais il ne s'agit pas là de réelle inférence, juste d'un besoin d'information).

4. Pourquoi la méthode compareType appelée par la méthode checkType est-elle récursive ?

Elle permet de comparer les types de classe pour trouver un ancêtre commun.

## Phase 4 : Génération de la forme intermédiaire 

### Etat d'avancement des travaux sur la phase

- [x] génération de la forme intermédiaire (visiteur Intermediate).
- [x] génération de la forme intermédiaire du Projet MiniJAVA, c'est-à-dire pour avoir aussi des tableaux. 

### Commentaires du binôme sur la phase :


1. pour traduire les nœuds de l'AST de type ExprNew, que fait-on de l'appel au constructeur ?

On initialise tous les champs à 0 puis on traite le constructeur comme une fonction quelconque.

2. quel est le rôle de l'attribut currentMethod ? comment est-il manipulé ?

Il permet de se rappeler dans quelle méthode nous nous situons, il est très utile, pour créer des variables temporaires dans le bon scope.

3. que fait-on du problème du dangling else ? comment le traduit-on ?

A priori il n'y a pas de dangling else car après un if on attend un unique else, c.f la grammaire de minijava dans le memento.

4. quel usage fait-on des étiquettes/labels ? pour quels types de nœud de l'AST sont-elles utilisées et pourquoi ? (on tiendra compte des bonus si on les a fait)

Les étiquettes servent lors des JUMP en mips, les labels servent à déverminer.

## Phase N : Titre de la phase

### État d'avancement des travaux sur la phase

...

### Commentaires du binôme sur la phase :

Ce projet est réalisé par :
- Perrin Augustin
- Hostettler Maël

# Notes du binôme pour expliquer le travail réalisé et pour répondre aux questions sur le projet

Cette page au format
[MarkDown](https://docs.gitlab.com/ee/user/rich_text_editor.html#input-rules
"MarkDown") sert (1) à indiquer l'état d'avancement des travaux dans
le projet MiniJAVA, (2) à fournir vos réponses aux questions qui sont
posées dans l'énoncé, et (3) à toute autre information permettant de
mieux comprendre ce qui a été fait, ce qui est en cours de réalisation
sans être finalisé, et ce qui pose problème.

## Phase 1 : Analyse lexicale et syntaxique

### État d'avancement des travaux sur la phase

- [x] spécifications jflex/cup.
- [x] PrettyPrinter.
- [x] analyse lexicale et syntaxique pour les tableaux.

### Commentaires du binôme sur la phase :

1. est-il possible de définir la valeur d'un entier sous la forme octale ?

Non il n'est pas possible de définir un entier en octal. Le 0 en début d'entier est refusé.

2. dans une méthode, peut-on écrire « int a; a = 0; int b; b = n; » ? ou doit-on plutôt écrire « int a; int b; a = 0; b = n; » ?

Dans une méthode, il est préférable de séparer les déclarations de variables des statements. Donc, il est recommandé d'écrire « `int a; int b; a = 0; b = n;` ».

3. est-il possible de mettre un attribut de classe (static) dans la classe qui contient la méthode main ?

Non, il n'est pas possible de mettre un attribut de classe à la classe main :
```java
klassMain ::= CLASS:a ident:b LBRACE PUBLIC STATIC VOID MAIN
              LPAREN STRING LBRACK RBRACK ident:c RPAREN
              LBRACE stmt:d RBRACE RBRACE:z
                {: RESULT = KlassMain.create(b, c, d);
                   RESULT.addPosition(axleft, zxright); :}
```
la classe main ne contient que la méthode main.

4. est-il possible d'avoir une variable locale dans la méthode main ?

Oui, il est possible d'obtenir une variable locale dans la méthode main de la manière suivante :
```java
public static void main(String[] args) {
    { // stmtBlock
        int i; // variable dans methodBody
        ...
    }
}
```

## Phase 2 : Analyse Sémantique : Table des Symboles

### État d'avancement des travaux sur la phase

- [x] construction de la table des symboles (visiteur BuildSymTab, mais aussi les visiteurs DisplayVarDeclarations, DisplayScopes, et DisplayVarDeclarationsInScopes) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [x] construction de la table des symboles du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. lorsque l'on écrit un visiteur héritant du visiteur par défaut AstVisitorDefault, à quoi sert l'appel à la méthode defaultVisit dans les méthodes redéfinies ?

La méthode `defaultVisit` est héritée du visiteur par défaut et va explorer tous les enfants du noeud. Son appel permet donc d'aller visiter tous les noeuds de l'AST.

2. lorsque l'on ajoute un nouveau type de nœud dans l'AST, faut-il modifier le visiteur par défaut AstVisitorDefault ? si oui, pourquoi ?

Non, tous nos noeuds héritent de AstNode, defaultVisitor n'a donc pas besoin d'être modifié.

3. que fait notre compilateur en cas d'erreur dans la gestion de l'héritage (détection d'un cycle) ?

Il renvoie une erreur (c.f. memento)

4. notre compilateur considère-t-il comme une erreur de redéfinition l'écrasement d'un paramètre d'appel par une variable locale ? Qu'en est-il du compilateur javac ?

```java
class kaka {
    public int kuku(int a) {
        int a;
        return 1;
    }
}
```
Notre compilo ne se plaint pas tandis que javac émet un message d'erreur.


## Phase 3 : Analyse Sémantique : Contrôle de Type

### État d'avancement des travaux sur la phase

- [x] contrôle de type (visiteur TypeChecking) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [x] contrôle de type du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. Le compilateur MiniJAVA gère-t-il la surcharge ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;

Non, les opérateurs ne sont définits que pour un type primitif (int ou bool).

2. Le compilateur MiniJAVA gère-t-il la redéfinition ? la liaison dynamique ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;

Non, ni la redéfinition ni la liaison dynamique ne sont géré, on injecte les méthodes et attributs dans la classe enfant et ne supportons pas les @Overloaded ni les redéfinitions de méthodes (overwrite).

3. Dans le code du visiteur TypeChecking, comment voit-on que le type est un attribut synthétisé ?

Oui, on doit visiter les noeuds enfants avant d' "inférer" (sous entendu on a besoin de visiter les enfants avant mais il ne s'agit pas là de réelle inférence, juste d'un besoin d'information).

4. Pourquoi la méthode compareType appelée par la méthode checkType est-elle récursive ?

Elle permet de comparer les types de classe pour trouver un ancêtre commun.

## Phase 4 : Génération de la forme intermédiaire 

### Etat d'avancement des travaux sur la phase

- [x] génération de la forme intermédiaire (visiteur Intermediate).
- [x] génération de la forme intermédiaire du Projet MiniJAVA, c'est-à-dire pour avoir aussi des tableaux. 

### Commentaires du binôme sur la phase :


1. pour traduire les nœuds de l'AST de type ExprNew, que fait-on de l'appel au constructeur ?

On initialise tous les champs à 0 puis on traite le constructeur comme une fonction quelconque.

2. quel est le rôle de l'attribut currentMethod ? comment est-il manipulé ?

Il permet de se rappeler dans quelle méthode nous nous situons, il est très utile, pour créer des variables temporaires dans le bon scope.

3. que fait-on du problème du dangling else ? comment le traduit-on ?

A priori il n'y a pas de dangling else car après un if on attend un unique else, c.f la grammaire de minijava dans le memento.

4. quel usage fait-on des étiquettes/labels ? pour quels types de nœud de l'AST sont-elles utilisées et pourquoi ? (on tiendra compte des bonus si on les a fait)

Les étiquettes servent lors des JUMP en mips, les labels servent à déverminer.

## Phase 5 : 

### État d'avancement des travaux sur la phase

- [x] Réaliser la génération de l'assembleur MIPS (visiteur IRvisitorDefault) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »). Attention : vérifier aussi les affichages !
- [x] Réaliser la génération de l'assembleur MIPS du Projet MiniJAVA, c'est-à-dire pour avoir aussi des tableaux. 

### Commentaires du binôme sur la phase :

1. Quels sont les instructions de la représentation intermédiaire (quadruplets) qui sont impliquées dans la réalisation de la convention d'appel ? Quelles sont les méthodes utilitaires de la classe ToMips que l'on peut enchaîner pour la mise en œuvre de la convention d'appel (de l'appel au retour, chez l'appelante et chez l'appelée) ? Indiquer la séquence.

Tout ce qui est lié au contrôle de flot entre les fonctions i.e QCall, QCallStatic, QParam et QReturn.

2. Dans une méthode d'instance, comment accède-t-on à un attribut de l'instance ? Via quel registre ?

On les considère comme des variables du scope (elles sont dans le root scope), cela passe donc par l'allocator optimisé si possible:
```java
    public void visit(final QCopy q) {
        // optimisation 1
        if (this.allocator.access(q.arg1()) instanceof AccessReg regArg1) {
            regStore(regArg1.getRegister(), q.result());
            return;
        }
        // optimisation 2
        if (this.allocator.access(q.result()) instanceof AccessReg regResult) {
            regLoad(regResult.getRegister(), q.arg1());
            return;
        }
        // cas général
        regLoad(MIPSRegister.V0, q.arg1());
        regStore(MIPSRegister.V0, q.result());
    }
```
sinon par V0.

3. Quelles sont les variables globales dans un programme MiniJAVA ? Où sont-elles en mémoire ?

Il ne nous semble pas y en avoir dans la sémantique du langage mais après la génération MIPS, toutes les constantes non triviales (non instantanées) sont stockées dans .data donc sont globales au sens de l'assembleur.