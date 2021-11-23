# ll1-compiler

## Quickstart

```
java -jar dist/compiler.jar Main test/test.il   
```

Explore results in `test/test.ll`

# Expressions régulières

Afin de décrire les mots (tokens) acceptés par le langage Iulius, nous
utilisons les expressions régulières étendues.

Voici le tableau qui reprend l’ensemble des tokens du langage Iulius.
Chaque ligne de celui-ci est composée du nom du token, le type dans
l’enum LexicalUnit qui lui est associé et de l’expression régulière qui
correspond à ce token.  

<div class="tabular">
  
| **Nom**                     | **Type**                  | **Expression régulière**        |
|-----------------------------|---------------------------|---------------------------------|
| Comment                     | /                         | ` #.*(\\r)?\\n`                 |
| Circumflex (^)              | POWER                     | ` \^ `                          |
| Percentage (\%)             | REMAINDER                 | ` % `                           |
| Tilde (   extasciitilde)    | BITWISE\_NOT              | ` ~ `                           |
| Pipe (\|)                   | BITWISE\_OR               | ` \| `                          |
| Dollar (\$)                 | BITWISE\_XOR              | ` \$ `                          |
| Dubble greater (>>)         | ARITHMETIC\_SHIFT\_RIGHT  | ` >> `                          |
| Dubble lower (<<)           | ARITHMETIC\_SHIFT\_LEFT   | ` << `                          |
| Dubble equal (==)           | EQUALITY                  | ` == `                          |
| Exclamation equal (!=)      | INEQUALITY                | ` != `                          |
| Function                    | FUNCTION                  | ` function `                    |
| Return                      | RETURN                    | ` return `                      |
| Arrow right (->)            | MAP\_TO                   | ` -> `                          |
| Question mark (?)           | TERNARY\_IF               | ` \? `                          |
| Exclamation mark (!)        | NEGATION                  | ` ! `                           |
| Colon (:)                   | TERNARY\_ELSE             | ` : `                           |
| Doubble ampersand (&&)      | LAZY\_AND                 | ` && `                          |
| Doubble pipe (\|\|)         | LAZY\_OR                  | ` \|\| `                        |
| While                       | WHILE                     | ` while `                       |
| For                         | FOR                       | ` for `                         |
| Semicolon (;)               | END\_OF\_INSTRUCTION      | ` ; `                           |
| Println                     | PRINTLN                   | ` println `                     |
| Const                       | CONST                     | ` const `                       |
| Let                         | LET                       | ` let `                         |
| Dubble colon (::)           | TYPE\_DEFINITION          | ` :: `                          |
| Boolean (type)              | BOOLEAN\_TYPE             | ` Bool `                        |
| Real (type)                 | REAL\_TYPE                | ` FloatingPoint `               |
| Integer (type)              | INTEGER\_TYPE             | ` Integer `                     |
| Integer (cast)              | INTEGER\_CAST             | ` int `                         |
| Real (cast)                 | REAL\_CAST                | ` float `                       |
| Read integer                | READ\_INTEGER             | ` readint `                     |
| Read real                   | READ\_REAL                | ` readfloat `                   |
| Boolean (cast)              | BOOLEAN\_CAST             | ` bool `                        |
| Backslash (\\)              | INVERSE\_DIVIDE           | ` \\ `                          |
| Do                          | DO                        | ` do `                          |
| End                         | END                       | ` end `                         |
| Comma (,)                   | COMMA                     | ` , `                           |
| Left parenthesis(()         | LEFT\_PARENTHESIS         | ` \( `                          |
| Right parenthesis ())       | RIGHT\_PARENTHESIS        | ` \) `                          |
| Minus sign (-)              | MINUS                     | ` - `                           |
| Plus sign (+)               | PLUS                      | ` \+ `                          |
| Equal sign (=)              | ASSIGNATION               | ` = `                           |
| Asterisk (*)                | TIMES                     | ` \* `                          |
| Slash (/)                   | DIVIDE                    | ` / `                           |
| True                        | BOOLEAN                   | ` true `                        |
| False                       | BOOLEAN                   | ` false `                       |
| Lower sign (<)              | LESS\_THAN                | ` < `                           |
| Greater sign (>)            | GREATER\_THAN             | ` > `                           |
| Lower or equals (<=)        | LESS\_OR\_EQUALS\_THAN    | ` <= `                          |
| Greater or equals (>=)      | GREATER\_OR\_EQUALS\_THAN | ` >= `                          |
| If                          | IF                        | ` if `                          |
| Else                        | ELSE                      | ` else `                        |
| Elseif                      | ELSE\_IF                  | ` elseif `                      |
| Identifier                  | IDENTIFIER                | ` ([a-z]\|[A-Z]\|_) `           |
|                             |                           | ` ([a-z]\|[A-Z]\|[0-9]\|_)* `   |
| Integer                     | INTEGER                   | ` (([1-9][0-9]*) \| 0) `         |
| Real                        | REAL                      | ` (([1-9][0-9]*) \| 0)\.[0-9]+ ` |


</div>

# Choix d’implémentation

## Gestion des nombres et opérations

Afin de gérer les expressions arithmétiques contenant des nombres et les
opérateurs plus et moins, nous avons du supprimer les opérateurs plus et
moins dans l’expression régulière des nombres entiers et réels.  
Par exemple, si on a 4+5 on détectera "4", "+" et "5" avec notre
implémentation tandis que si on avait laissé le plus dans l’expression
régulière des entiers nous aurions obtenu "4" et "+5".  
Par contre un entier +2 sera détecté comme "+" et "2" mais ce sera plus
simple à interpréter lors de la construction de l’arbre syntaxique.

## DFA

Premièrement, nous utilisons les notations suivantes :

-   signifie l’ensemble des lettres minuscules de a à z

-   signifie l’ensemble des lettres majuscules de a à z

-   signifie l’ensemble des chiffres de 0 à 9

-   Par extension \[d-y\] signifie l’ensemble des lettres minuscules de
    d à y

-   signifie le . dans les expressions régulières soit tous les
    caractères

Ensuite, dans la partie à droite de l’état initiale dans le graphe, soit
les différents mots-clés et les identifiers, pour plus de lisibilité
nous n’avons pas inclus les transitions d’un état faisant partie d’un
mot-clé vers un identifier, normalement chaque état d’un mot-clé devrait
contenir une transition depuis lui même vers l’état identifier, la
transition comprend {\[a-z\] , \[A-Z\], \[0-9\], \_} en excluant les
autres transitions sortantes de cet état.
