
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



2. lorsque l'on ajoute un nouveau type de nœud dans l'AST, faut-il modifier le visiteur par défaut AstVisitorDefault ? si oui, pourquoi ?



3. que fait notre compilateur en cas d'erreur dans la gestion de l'héritage (détection d'un cycle) ?



4. notre compilateur considère-t-il comme une erreur de redéfinition l'écrasement d'un paramètre d'appel par une variable locale ? Qu'en est-il du compilateur javac ?



## Phase 3 : Analyse Sémantique : Contrôle de Type

### État d'avancement des travaux sur la phase

- [ ] contrôle de type (visiteur TypeChecking) ainsi que les tests (intégrés à la construction du compilateur et qui « passent »).
- [ ] contrôle de type du Projet MiniJAVA.

### Commentaires du binôme sur la phase :

1. Le compilateur MiniJAVA gère-t-il la surcharge ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;



2. Le compilateur MiniJAVA gère-t-il la redéfinition ? la liaison dynamique ? Si ce n'est pas le cas, que fait le compilateur ? Pour cette question, cf. aussi la section 2.4 du Mémento MiniJAVA ;



3. Dans le code du visiteur TypeChecking, comment voit-on que le type est un attribut synthétisé ?



4. Pourquoi la méthode compareType appelée par la méthode checkType est-elle récursive ?



## Phase N : Titre de la phase

### État d'avancement des travaux sur la phase

...

### Commentaires du binôme sur la phase :
