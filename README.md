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


[See table here](/doc/grammar.md)



# Ensembles First et Follow

Dans le tableau suivant, EPSILON\_VALUE ≡ *ϵ*.

[See table here](/doc/first_follow.md)


# Action Table

Les lignes de “l’action table” représentent les variables et les
colonnes représentent les terminaux. Une cellule de cette table contient
le numéro d’une règle de production correspondant au numéro repris dans
la section grammaire.

Dans le tableau suivant, EPSILON\_VALUE ≡ *ϵ*.


[See action table here](/doc/action-table.pdf)

