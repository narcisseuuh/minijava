
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

- [x] spécifications jflex/cup : terminé/presque terminé/en cours/commencé/non commencé
- [ ] PrettyPrinter : terminé/presque terminé/en cours/commencé/non commencé
- [x] analyse lexicale et syntaxique pour les tableaux : terminé/presque terminé/en cours/commencé/non commencé

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


## Phase N : Titre de la phase

### État d'avancement des travaux sur la phase

...

### Commentaires du binôme sur la phase :
