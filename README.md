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

[See table here](/doc/regex_token.md)

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


# Introduction

Dans cette partie du projet, il nous est demandé de modifier la
grammaire donnée dans l’énoncé afin de rendre celle-ci LL(1). Il faut
donc appliquer toute une série de règles et d’algorithmes pour rendre
cette grammaire moins ambigüe et LL(1), dans le but de construire
“l’action table” à partir des ensembles first et follow. L’inclusion des
fonctions dans la grammaire fait partie d’un bonus que nous avons décidé
de réaliser.

# Transformation de la grammaire donnée

## Opérateurs binaires et unaires

La première étape pour rendre la grammaire LL(1) fut la distinction des
deux types d’opérateurs : les opérateurs binaires qui agissent sur deux
expressions l’une située à gauche et l’autre à droite de ces opérateurs
et les opérateurs unaires qui agissent sur une seule expression située à
droite de ces opérateurs. Il faut différencier les deux types
d’opérateurs afin de ne pas avoir des expressions du style : &gt;&gt;4,
\*2, 6\|,... .Les opérateurs unaires sont au nombre de quatre : !
(negation), ∼ (not bit à bit), + et -. Les opérateurs + et - sont
également des opérateurs binaires.

## Priorité et associativité des opérateurs

Lors de cette étape, nous avons modifié la grammaire afin de fixer la
priorité et l’associativité des différents opérateurs, ceci afin de
rendre la grammaire moins ambigüe. Nous avons remarqué que les
opérateurs unaires étaient plus prioritaires que les opérateurs binaires
et que les opérateurs unaires étaient tous associatifs à droite, tandis
que les opérateurs binaires l’étaient tous à gauche. Pour fixer la
priorité, nous avons mis les opérateurs les moins prioritaires le plus
“haut” possible dans la grammaire et les plus prioritaires le plus “bas”
possible. Dans la grammaire ci-dessous où le “start symbol” est E,
l’opérateur “+” est considéré comme plus “haut” que l’opérateur “\*”
dans la grammaire car il est possible d’obtenir l’opérateur “+” en
utilisant moins de règles de production à partir du “start symbol” que
pour obtenir l’opérateur “\*”.

    E->E + T
     ->T
    T->T * F
     ->F
    F->ID
     ->( E )

## Suppression des récursions à gauche

L’étape suivante fut l’étape de suppression des récursions à gauche qui
sont incompatibles avec les “top-down parser” car cela fait entrer ce
genre de parser dans une boucle ou récursion infinie. Cette étape rend
tout opérateur associatif à droite si l’on ne fait rien pour résoudre ce
problème lors de l’analyse sémantique.

## Left factoring

Cette étape rassemble les règles de production d’une variable qui ont un
préfixe commun en une seule règle de production qui contient ce préfixe
commun et une nouvelle variable. Cette nouvelle variable possède des
règles de production vers les différents suffixes qui étaient présents à
l’origine dans les différentes règles de production qui ont été mises en
commun. Ceci est nécessaire car le parser qu’on va créer est LL(1), il
faut donc qu’il puisse choisir la bonne règle de production en regardant
seulement le prochain token qui est en entrée.

## Les variables &lt;Instruction&gt; et &lt;InstructionList&gt;

Dans la grammaire donnée, à chaque fois que la variable
&lt;Instruction&gt; se trouvait dans la partie droite d’une règle de
production excepté lorsque la partie gauche de la règle était
&lt;InstructionList&gt;, il y avait une autre règle de production pour
cette même partie gauche où &lt;Instruction&gt; était remplacé par
&lt;InstructionList&gt;. Par exemple, &lt;If&gt;→&lt;Expression&gt;
&lt;Empty&gt; &lt;InstructionList&gt; &lt;IfEnd&gt; et
&lt;If&gt;→&lt;Expression&gt; &lt;Empty&gt; &lt;Instruction&gt;
&lt;IfEnd&gt;. Ceci permettant de ne pas mettre de END\_OF\_INSTRUCTION
à la fin d’une &lt;Instruction&gt; lorsque le corps d’un block (ici, le
corps du “if”) ne contenait qu’une seule &lt;Instruction&gt;. Le
problème posé par le doublement systématique des règles de production
contenant des instructions (une règle pour &lt;Instruction&gt; et une
autre pour &lt;InstructionList&gt;) est que ce n’est pas factorisé à
gauche et que le first de &lt;InstructionList&gt; peut être
&lt;Instruction&gt;. Afin de résoudre ce problème, nous avons décidé de
ne plus utilisé que la variable &lt;InstructionList&gt; dans les autres
règles de production. Ainsi, on évite le doublement des règles de
production. Une &lt;InstructionList&gt; est une liste
d’&lt;Instruction&gt; séparées par des END\_OF\_INSTRUCTION, avec la
dernière &lt;Instruction&gt; qui n’est pas nécessairement suivie d’un
END\_OF\_INSTRUCTION. Ceci permet d’avoir un programme du style :
if(a&gt;b);a=10 end; Donc seule une &lt;InstructionList&gt; permet de
produire des &lt;Instruction&gt;. De plus, vu qu’une
&lt;InstructionList&gt; peut contenir des instructions vide (une
instruction contenant seulement END\_OF\_INSTRUCTION) la variable
&lt;Empty&gt; n’est plus nécessaire car celle-ci servait uniquement à
indiquer qu’il fallait au moins un terminal END\_OF\_INSTRUCTION.

## Fonctions

Les fonctions amènent deux types d’instructions en plus : les
définitions de fonction et les appels de fonction. Les appels sont
considérés comme des expressions atomiques afin de pourvoir mettre des
appels de fonction au sein d’une expression. Par exemple, a=foo()+5; De
plus, il faut qu’une fonction puisse être appelée sans avoir besoin de
faire une assignation ou un autre type d’instruction car une fonction
peut ne rien retourner et simplement avoir un effet de bord. Par
exemple, la fonction println est une fonction qui ne retourne rien mais
produit un effet de bord qui est l’affichage dans le terminal. Il faut
donc pouvoir faire, par exemple, println(a). C’est pour cela qu’il faut
qu’un appel de fonction soit une instruction à part entière.

## Instructions commencant par un identifier

Plusieurs instructions commencent par un identifier. Il s’agit des
assignations, des déclarations de variables et des appels de fonctions.
Afin que la grammaire soit LL(1) il faut factoriser ces instructions.
C’est pour cela que la variable &lt;IdentifierInstruction&gt; a été
créée. Celle-ci représente une instruction qui commence par un
identifier. La variable &lt;IdentifierInstructionTail&gt; a été
également créée afin de distinguer les différents types d’instructions
qui commencent par un identifier. La distinction est après aisément
faite car lorsque le symbole suivant l’identifier est “=”, on sait que
c’est une assignation, lorsque ce symbole est “::”, on sait que c’est
une déclaration de variable et lorsque que ce symbole est “(”, on sait
que c’est un appel de fonction.

# Grammaire

Voici la grammaire donnée dans l’énoncé et transformée en grammaire
LL(1). A chaque règle de production correspond un numéro qui identifie
cette règle.


|    |                                   |   |                                               |
|----|-----------------------------------|---|-----------------------------------------------|
|1   |`<Program>`                        |-> |`<InstructionList>`                            |
|2   |`<Instruction>`                    |-> |`<IdentifierInstruction>`                      |
|3   |                                   |-> |`<ConstDefinition>`                            |
|4   |                                   |-> |`<Block>`                                      |
|5   |                                   |-> |`<Loop>`                                       |
|6   |                                   |-> |`<BuiltInFunctionCall>`                        |
|7   |                                   |-> |`<FunctionDefinition>`                         |
|8   |`<InstructionList>`                |-> |`<Instruction> <InstructionListTail>`          |
|9   |                                   |-> |`<InstructionListTail>`                        |
|10  |`<InstructionListTail>`            |-> |`END\_OF\_INSTRUCTION <InstructionList>`       |
|11  |                                   |-> |`\epsilon`                                     |
|12  |`<IdentifierInstruction>`          |-> |`IDENTIFIER <IdentifierInstructionTail>`       |
|13  |`<IdentifierInstructionTail>`      |-> |`<AssignationTail>`                            |
|14  |                                   |-> |`TYPE\_DEFINITION <Type>`                      |
|15  |                                   |-> |`<FunctionCallTail>`                           |
|16  |`<AssignationTail>`                |-> |`ASSIGNATION <Expression>`                     |
|17  |                                   |-> |`COMMA IDENTIFIER <AssignationTail> COMMA`     |
|18  |`<ConstDefinition>`                |-> |`CONST IDENTIFIER <AssignationTail>`           |
|19  |`<Block>`                          |-> |`LET IDENTIFIER <AssignationTail>`             |
|20  |`<Loop>`                           |-> |`<If>`                                         |
|21  |                                   |-> |`WHILE <Expression> END\_OF\_INSTRUCTION`      |
|22  |                                   |-> |`FOR IDENTIFIER ASSIGNATION <Expression>`      |
|23  |`<ForTail>`                        |-> |`END\_OF\_INSTRUCTION <InstructionList> END`   |
|24  |                                   |-> |`TERNARY\_ELSE <Expression>`                   |
|25  |`<Type>`                           |-> |`BOOLEAN\_TYPE`                                |
|26  |                                   |-> |`REAL\_TYPE`                                   |
|27  |                                   |-> |`INTEGER\_TYPE`                                |
|28  |`<Expression>`                     |-> |`<BinaryExpression> <TernaryIfExpression>`     |
|29  |`<TernaryIfExpression>`            |-> |`TERNARY\_IF <Expression>`                     |
|30  |                                   |-> |`\epsilon`                                     |
|31  |`<TernaryElseExpression>`          |-> |`TERNARY\_ELSE <Expression>`                   |
|32  |`<AtomicExpression>`               |-> |`<AtomicIdentifierExpression>`                 |
|33  |                                   |-> |`INTEGER`                                      |
|34  |                                   |-> |`REAL`                                         |
|35  |                                   |-> |`BOOLEAN`                                      |
|36  |                                   |-> |`<BuiltInFunctionCall>`                        |
|37  |`<AtomicIdentifierExpression>`     |-> |`IDENTIFIER`                                   |
|38  |`<AtomicIdentifierExpressionTail>` |-> |`<FunctionCallTail>`                           |
|39  |                                   |-> |`\epsilon`                                     |
|40  |`<UnaryExpression>`                |-> |`NEGATION <UnaryExpression>`                   |
|41  |                                   |-> |`<UnaryBitwiseNotExpression>`                  |
|42  |`<UnaryBitwiseNotExpression>`      |-> |`BITWISE\_NOT <UnaryBitwiseNotExpression>`     |
|43  |                                   |-> |`<UnaryMinusPlusExpression>`                   |
|44  |`<UnaryMinusPlusExpression>`       |-> |`MINUS <UnaryMinusPlusExpression>`             |
|45  |                                   |-> |`PLUS <UnaryMinusPlusExpression>`              |
|46  |                                   |-> |`<UnaryAtomicExpression>`                      |
|47  |`<UnaryAtomicExpression>`          |-> |`<AtomicExpression>`                           |
|48  |                                   |-> |`LEFT\_PARENTHESIS <Expression>`               |
|49  |`<BinaryExpression>`               |-> |`<BinaryLazyOrExpression>`                     |
|50  |`<BinaryExpression'>`              |-> |`LAZY\_OR <BinaryLazyOrExpression>`            |
|51  |                                   |-> |`\epsilon`                                     |
|52  |`<BinaryLazyOrExpression>`         |-> |`<BinaryLazyAndExpression>`                    |
|53  |`<BinaryLazyOrExpression'>`        |-> |`LAZY\_AND <BinaryLazyAndExpression>`          |
|54  |                                   |-> |`\epsilon`                                     |
|55  |`<BinaryLazyAndExpression>`        |-> |`<BinaryNumericExpression>`                    |
|56  |`<BinaryLazyAndExpression'>`       |-> |`GREATER\_THAN <BinaryNumericExpression>`      |
|57  |                                   |-> |`LESS\_THAN <BinaryNumericExpression>`         |
|58  |                                   |-> |`GREATER\_OR\_EQUALS\_THAN`                    |
|59  |                                   |-> |`LESS\_OR\_EQUALS\_THAN`                       |
|60  |                                   |-> |`EQUALITY <BinaryNumericExpression>`           |
|61  |                                   |-> |`INEQUALITY <BinaryNumericExpression>`         |
|62  |                                   |-> |`\epsilon`                                     |
|63  |`<BinaryNumericExpression>`        |-> |`<BinaryTermExpression>`                       |
|64  |`<BinaryNumericExpression'>`       |-> |`PLUS <BinaryTermExpression>`                  |
|65  |                                   |-> |`MINUS <BinaryTermExpression>`                 |
|66  |                                   |-> |`BITWISE\_OR <BinaryTermExpression>`           |
|67  |                                   |-> |`BITWISE\_XOR <BinaryTermExpression>`          |
|68  |                                   |-> |`\epsilon`                                     |
|69  |`<BinaryTermExpression>`           |-> |`<BinaryShiftedExpression>`                    |
|70  |`<BinaryTermExpression'>`          |-> |`ARITHMETIC\_SHIFT\_LEFT`                      |
|71  |                                   |-> |`ARITHMETIC\_SHIFT\_RIGHT`                     |
|72  |                                   |-> |`\epsilon`                                     |
|73  |`<BinaryShiftedExpression>`        |-> |`<BinaryFactorExpression>`                     |
|74  |`<BinaryShiftedExpression'>`       |-> |`TIMES <BinaryFactorExpression>`               |
|75  |                                   |-> |`DIVIDE <BinaryFactorExpression>`              |
|76  |                                   |-> |`REMAINDER <BinaryFactorExpression>`           |
|77  |                                   |-> |`BITWISE\_AND <BinaryFactorExpression>`        |
|78  |                                   |-> |`INVERSE\_DIVIDE <BinaryFactorExpression>`     |
|79  |                                   |-> |`\epsilon`                                     |
|80  |`<BinaryFactorExpression>`         |-> |`<UnaryExpression>`                            |
|81  |`<BinaryFactorExpression'>`        |-> |`POWER <UnaryExpression>`                      |
|82  |                                   |-> |`\epsilon`                                     |
|83  |`<If>`                             |-> |`IF <Expression> END\_OF\_INSTRUCTION`         |
|84  |`<IfEnd>`                          |-> |`ELSE\_IF <Expression> END\_OF\_INSTRUCTION`   |
|85  |                                   |-> |`ELSE <InstructionList> END`                   |
|86  |                                   |-> |`END`                                          |
|87  |`<BuiltInFunctionCall>`            |-> |`READ\_REAL LEFT\_PARENTHESIS`                 |
|88  |                                   |-> |`READ\_INTEGER LEFT\_PARENTHESIS`              |
|89  |                                   |-> |`INTEGER\_CAST LEFT\_PARENTHESIS <Expression>` |
|90  |                                   |-> |`REAL\_CAST LEFT\_PARENTHESIS <Expression>`    |
|91  |                                   |-> |`BOOLEAN\_CAST LEFT\_PARENTHESIS <Expression>` |
|92  |                                   |-> |`PRINTLN LEFT\_PARENTHESIS <Expression>`       |
|93  |`<FunctionCallTail>`               |-> |`LEFT\_PARENTHESIS <Parameter>`                |
|94  |`<Parameter>`                      |-> |`<Expression> <ParameterTail>`                 |
|95  |                                   |-> |`\epsilon`                                     |
|96  |`<ParameterTail>`                  |-> |`COMMA <Expression> <ParameterTail>`           |
|97  |                                   |-> |`\epsilon`                                     |
|98  |`<FunctionDefinition>`             |-> |`FUNCTION IDENTIFIER LEFT\_PARENTHESIS`        |
|99  |`<FunctionDefinitionEnd>`          |-> |`RETURN <Expression> END`                      |
|100 |                                   |-> |`END`                                          |
|101 |`<Argument>`                       |-> |`IDENTIFIER TYPE\_DEFINITION <Type>`           |
|102 |                                   |-> |`\epsilon`                                     |
|103 |`<ArgumentTail>`                   |-> |`COMMA IDENTIFIER TYPE\_DEFINITION <Type>`     |
|104 |                                   |-> |`ϵ`                                     |



# Ensembles First et Follow

Dans le tableau suivant, EPSILON\_VALUE ≡ *ϵ*.
| Variable | First | Follow |
|---|---|---|
| `<Program>` | FUNCTION, WHILE, READ\_REAL, EPSILON\_VALUE, IDENTIFIER, CONST, BOOLEAN\_CAST, PRINTLN, END\_OF\_INSTRUCTION, READ\_INTEGER, FOR, INTEGER\_CAST, LET, IF, REAL\_CAST |  |
| `<Instruction>` | FUNCTION, READ\_INTEGER, FOR, WHILE, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, CONST, IDENTIFIER, PRINTLN, LET, IF, REAL\_CAST | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<InstructionList>` | FUNCTION, WHILE, READ\_REAL, EPSILON\_VALUE, BOOLEAN\_CAST, IDENTIFIER, CONST, PRINTLN, END\_OF\_INSTRUCTION, READ\_INTEGER, FOR, INTEGER\_CAST, LET, IF, REAL\_CAST | END, ELSE\_IF, ELSE, RETURN |
| `<InstructionListTail>` | EPSILON\_VALUE, END\_OF\_INSTRUCTION | END, ELSE\_IF, ELSE, RETURN |
| `<IdentifierInstruction>` | IDENTIFIER | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<IdentifierInstructionTail>` | ASSIGNATION, TYPE\_DEFINITION, COMMA, LEFT\_PARENTHESIS | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<AssignationTail>` | ASSIGNATION, COMMA | END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<ConstDefinition>` | CONST | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<Block>` | LET | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<Loop>` | FOR, WHILE, IF | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<ForTail>` | END\_OF\_INSTRUCTION, TERNARY\_ELSE | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<Type>` | REAL\_TYPE, BOOLEAN\_TYPE, INTEGER\_TYPE | RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<Expression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<TernaryIfExpression>` | TERNARY\_IF, EPSILON\_VALUE | RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<TernaryElseExpression>` | TERNARY\_ELSE | RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<AtomicExpression>` | READ\_INTEGER, INTEGER, REAL, BOOLEAN, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<AtomicIdentifierExpression>` | IDENTIFIER | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<AtomicIdentifierExpressionTail>` | EPSILON\_VALUE, LEFT\_PARENTHESIS | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<UnaryExpression>` | INTEGER, REAL, BOOLEAN, NEGATION, BITWISE\_NOT, READ\_REAL, LEFT\_PARENTHESIS, IDENTIFIER, BOOLEAN\_CAST, PRINTLN, READ\_INTEGER, PLUS, MINUS, INTEGER\_CAST, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<UnaryBitwiseNotExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<UnaryMinusPlusExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, MINUS, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<UnaryAtomicExpression>` | READ\_INTEGER, INTEGER, REAL, BOOLEAN, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, LEFT\_PARENTHESIS, IDENTIFIER, PRINTLN, REAL\_CAST | TERNARY\_IF, RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<BinaryExpression'>` | LAZY\_OR, EPSILON\_VALUE | TERNARY\_IF, RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<BinaryLazyOrExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | TERNARY\_IF, LAZY\_OR, RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<BinaryLazyOrExpression'>` | LAZY\_AND, EPSILON\_VALUE | TERNARY\_IF, LAZY\_OR, RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<BinaryLazyAndExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, LEFT\_PARENTHESIS, IDENTIFIER, PRINTLN, REAL\_CAST | LAZY\_AND, TERNARY\_IF, LAZY\_OR, RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<BinaryLazyAndExpression'>` | EQUALITY, INEQUALITY, GREATER\_THAN, EPSILON\_VALUE, LESS\_OR\_EQUALS\_THAN, GREATER\_OR\_EQUALS\_THAN, LESS\_THAN | LAZY\_AND, TERNARY\_IF, LAZY\_OR, RIGHT\_PARENTHESIS, END, COMMA, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, TERNARY\_ELSE, RETURN |
| `<BinaryNumericExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | EQUALITY, TERNARY\_IF, GREATER\_THAN, RIGHT\_PARENTHESIS, COMMA, END\_OF\_INSTRUCTION, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, RETURN, LESS\_THAN, LAZY\_AND, INEQUALITY, LAZY\_OR, END, ELSE\_IF, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryNumericExpression'>` | BITWISE\_OR, PLUS, MINUS, BITWISE\_XOR, EPSILON\_VALUE | EQUALITY, TERNARY\_IF, GREATER\_THAN, RIGHT\_PARENTHESIS, COMMA, END\_OF\_INSTRUCTION, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, RETURN, LESS\_THAN, LAZY\_AND, INEQUALITY, LAZY\_OR, END, ELSE\_IF, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryTermExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, LEFT\_PARENTHESIS, IDENTIFIER, PRINTLN, REAL\_CAST | EQUALITY, TERNARY\_IF, RIGHT\_PARENTHESIS, GREATER\_THAN, BITWISE\_XOR, COMMA, END\_OF\_INSTRUCTION, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, RETURN, LESS\_THAN, LAZY\_AND, INEQUALITY, BITWISE\_OR, PLUS, LAZY\_OR, MINUS, END, ELSE\_IF, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryTermExpression'>` | ARITHMETIC\_SHIFT\_RIGHT, ARITHMETIC\_SHIFT\_LEFT, EPSILON\_VALUE | EQUALITY, TERNARY\_IF, RIGHT\_PARENTHESIS, GREATER\_THAN, BITWISE\_XOR, COMMA, END\_OF\_INSTRUCTION, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, RETURN, LESS\_THAN, LAZY\_AND, INEQUALITY, BITWISE\_OR, PLUS, LAZY\_OR, MINUS, END, ELSE\_IF, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryShiftedExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, LEFT\_PARENTHESIS, BOOLEAN\_CAST, IDENTIFIER, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, TERNARY\_IF, ARITHMETIC\_SHIFT\_LEFT, RIGHT\_PARENTHESIS, GREATER\_THAN, BITWISE\_XOR, COMMA, END\_OF\_INSTRUCTION, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, RETURN, LESS\_THAN, LAZY\_AND, INEQUALITY, PLUS, BITWISE\_OR, LAZY\_OR, MINUS, END, ELSE\_IF, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryShiftedExpression'>` | BITWISE\_AND, INVERSE\_DIVIDE, REMAINDER, TIMES, EPSILON\_VALUE, DIVIDE | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, TERNARY\_IF, ARITHMETIC\_SHIFT\_LEFT, RIGHT\_PARENTHESIS, GREATER\_THAN, BITWISE\_XOR, COMMA, END\_OF\_INSTRUCTION, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, RETURN, LESS\_THAN, LAZY\_AND, INEQUALITY, PLUS, BITWISE\_OR, LAZY\_OR, MINUS, END, ELSE\_IF, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryFactorExpression>` | READ\_INTEGER, INTEGER, REAL, PLUS, BOOLEAN, NEGATION, MINUS, BITWISE\_NOT, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, LEFT\_PARENTHESIS, IDENTIFIER, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<BinaryFactorExpression'>` | POWER, EPSILON\_VALUE | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, ARITHMETIC\_SHIFT\_LEFT, TERNARY\_IF, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, ELSE, TERNARY\_ELSE, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, BITWISE\_XOR, REMAINDER, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, END, TIMES, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<If>` | IF | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<IfEnd>` | END, ELSE\_IF, ELSE | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<BuiltInFunctionCall>` | READ\_INTEGER, READ\_REAL, INTEGER\_CAST, BOOLEAN\_CAST, PRINTLN, REAL\_CAST | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, TERNARY\_IF, ARITHMETIC\_SHIFT\_LEFT, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, TERNARY\_ELSE, ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, REMAINDER, BITWISE\_XOR, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, TIMES, END, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<FunctionCallTail>` | LEFT\_PARENTHESIS | ARITHMETIC\_SHIFT\_RIGHT, EQUALITY, BITWISE\_AND, TERNARY\_IF, ARITHMETIC\_SHIFT\_LEFT, GREATER\_THAN, LESS\_OR\_EQUALS\_THAN, TERNARY\_ELSE, ELSE, POWER, INEQUALITY, BITWISE\_OR, MINUS, RIGHT\_PARENTHESIS, REMAINDER, BITWISE\_XOR, COMMA, END\_OF\_INSTRUCTION, RETURN, LESS\_THAN, LAZY\_AND, PLUS, LAZY\_OR, INVERSE\_DIVIDE, TIMES, END, ELSE\_IF, DIVIDE, GREATER\_OR\_EQUALS\_THAN |
| `<Parameter>` | INTEGER, REAL, BOOLEAN, NEGATION, BITWISE\_NOT, READ\_REAL, EPSILON\_VALUE, BOOLEAN\_CAST, IDENTIFIER, LEFT\_PARENTHESIS, PRINTLN, READ\_INTEGER, PLUS, MINUS, INTEGER\_CAST, REAL\_CAST | RIGHT\_PARENTHESIS |
| `<ParameterTail>` | COMMA, EPSILON\_VALUE | RIGHT\_PARENTHESIS |
| `<FunctionDefinition>` | FUNCTION | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<FunctionDefinitionEnd>` | END, RETURN | END, END\_OF\_INSTRUCTION, ELSE\_IF, ELSE, RETURN |
| `<Argument>` | EPSILON\_VALUE, IDENTIFIER | RIGHT\_PARENTHESIS |
| `<ArgumentTail>` | COMMA, EPSILON\_VALUE | RIGHT\_PARENTHESIS |


# Action Table

Les lignes de “l’action table” représentent les variables et les
colonnes représentent les terminaux. Une cellule de cette table contient
le numéro d’une règle de production correspondant au numéro repris dans
la section grammaire.

Dans le tableau suivant, EPSILON\_VALUE ≡ *ϵ*.

