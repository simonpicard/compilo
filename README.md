# Quickstart

Compile and test the compiler
```
cd more/Compiler/src
make
make test
```
Explore results in `more/Compiler/src/test/test.ll`

Usage
```
java core/Main Main <file_to_compile>
```
E.g.
```
java core/Main Main test/Euclide.il
```
Explore results in `more/Compiler/src/test/Euclide.ll`

# Introduction

The goal of this project is to create a compilator for a programming language called Iulius.

# Regular expressions

To describe the words (tokens) accepted by the Iulius language, we use extended regular expressions.

[Here is the table which contains all the tokens of the Iulius language.](/doc/regex_token.md)
Each line of this table is composed of the name of the token, the type in the `LexicalUnit` enum associated with it, and the regular expression that corresponds to this token.


# Implementation choices

## Number management and operations

To handle arithmetic expressions containing numbers and the plus and minus operators, we had to remove the plus and minus operators in the regular expression of integers and real numbers. For example, if we have `4+5` we will detect  `4`,  `+`, and  `5` with our implementation while if we had left the plus in the regular expression of integers expression we would have obtained  `4` and  `+5`. On the other hand, an integer `+2` will be detected as  `+` and  `2` but it will be easier to interpret when building the syntax tree.

## DFA

[See the DFA here.](/dfa/dfa.pdf)

First, we use the following notations:

- `[a-z]` means the set of lower case letters from a to z
- `[A-Z]`means the set of upper case letters from a to z
- `[0-9]`means the set of numbers from 0 to 9
- By extension `\[d-y\]` means the set of lower case letters from d to y
- `[.]`means the `.` in regular expressions, i.e. all the characters

Then, in the right part of the initial state in the graph, i.e. the different keywords and identifiers, for more readability, we have not included the transitions from a state that is part of a keyword to an identifier, normally each state of a keyword should contain a transition from itself to the identifier state, the transition includes `{\[a-z\], \[A-Z\], \[0-9\], \_}` excluding the other transitions out of this state.

You may edit the DFA using the tool JFLAP 6.4, [available here](https://www.jflap.org/jflaptmp/).

# Transformation of the given grammar into LL1

In this part of the project, we are modifying the grammar in order to make it LL(1). We have to apply a whole set of rules and algorithms to make this grammar less ambiguous and LL(1), in order to build the "action table" from the sets first and follow. The inclusion of functions in the grammar is part of a bonus that we decided to realize.

## Binary and unary operators

The first step in making the grammar LL(1) was to distinguish two types of operators: the binary operators which act on two expressions, one on the left and the other on the right of these operators, and unary operators which act on a single expression located on the right of these operators. It is necessary to differentiate the two types of operators in order not to have expressions like: `\>\>4`, `\*2`, `6\|`,... There are four unary operators: `!` (negation), `∼` (not bit by bit), `+` and `-`. The operators `+` and `-` are also binary operators.

## Operator priority and associativity

In this step, we modified the grammar to set the priority and associativity of the different operators, to make the grammar less ambiguous. We noticed that the unary operators had a higher priority than binary operators and that unary operators were all associative on the right, while the binary operators were all associative on the left. To fix the priority, we put the lowest priority operators as high as possible and the highest priority as low as possible.

In the grammar below where the  `start symbol` is `E`, the operator  `+` is considered as "higher" than the operator  `\*` operator in the grammar because it is possible to obtain the  `+` operator by using fewer production rules from the  `start symbol` than obtaining the  `\*` operator.

```
E->E + T
 ->T
T->T * F
 ->F
F->ID
 ->( E )
```

## Deleting left-hand recursions

The next step was the removal of left-hand recursions which are incompatible with the "top-down parser" because it makes this kind of parser enter into an infinite loop or recursion. This step makes any associative operator to the right if nothing is done to solve this problem during the semantic analysis.

## Left factoring

This step gathers the production rules of a variable that has a common prefix into a single production rule that contains this common prefix and a new variable. This new variable has production rules to the different suffixes that were originally present in the different production rules that have been pooled.

This is necessary because the parser we are going to create is LL(1), so it must be able to choose the right production rule by only looking at the next token that is in input.

## The variables `\<Instruction\>` and `\<InstructionList\>`

In the given grammar, whenever the variable `\<Instruction\>` was in the right part of a production rule except when the left part of the except when the left part of the rule was `\<InstructionList\>`, there was another production rule for this same left this same left part where `\<Instruction\>` was replaced by `\<InstructionList\>`. For example, `\<If\> → \<Expression\> \<Empty\> \<InstructionList\> \<IfEnd\>` and `\<If\> → <Expresion\> \<Empty\> \<Instruction\> \<IfEnd\>`. This makes it possible not to put an `END_OF_INSTRUCTION` at the end of a `\<Instruction\>` when the body of a block (here, the body of the  `if`) contained only one `\<Instruction\>`. The problem posed by the systematic doubling of production rules containing instructions (one rule for `\<Instruction\>` and another one for `\<InstructionList\>`) is that it is not factorized on the left and that the `first` of `\<InstructionList\>` can be `\<Instruction\>`. In order to solve this problem, we decided to only use the variable `\<InstructionList\>` in the other production rules. Thus, we avoid the duplication of the production rules. An `\<InstructionList\>` is a list of `\<Instruction\>` separated by `END_OF_INSTRUCTION`, with the last `\<Instruction\>` not necessarily followed by an `END_OF_INSTRUCTION`. This makes it possible to have a program like : `if(a\>b);a=10 end;` So only a `\<InstructionList\>` allows to produce `\<Instruction\>`. Moreover, since a `\<InstructionList\>` may contain empty instructions (an instruction containing only `END_OF_INSTRUCTION`) the variable `\<Empty\>` variable is no longer necessary because it was only used to indicate that at least one `END_OF_INSTRUCTION` terminal was required.

## Functions

Functions provide two additional types of instructions: function definitions and function calls. The calls are considered atomic expressions to be able to put function calls within an expression. For example, `a=foo()+5;`. In addition, a function must be able to be called without the need to make an assignment or another type of instruction because a function can return nothing and simply have an edge effect. For example, the `println` function is a function that returns nothing but produces an edge effect which is the display in the terminal. It is necessary to therefore be able to do, for example, `println(a)`. That's why a function call must be a statement in its own.

## Instructions starting with an identifier

Several instructions start with an identifier. These are assignments, variable declarations, and function calls. For the grammar to be LL(1) we need to factor in these statements. This is why the variable `\<IdfierInstruction\>` was created. This variable represents an instruction that starts with an identifier. The variable `\<IdentifierInstructionTail\>` has also been created to distinguish the different types of instructions that start with an identifier. The distinction is then easily made because when the symbol following the identifier is  `=`, we know that it is an assignment. When this symbol is  `::`, we know that it is a variable declaration. And when this symbol is `(`, we know that it is a function call.

# Grammar

Here is the grammar given in the statement and transformed into LL(1). Each production rule corresponds to a number that identifies that rule.

[See table here](/doc/grammar.md)

# First and Follow sets

In the following table, EPSILON_VALUE ≡ _ϵ_.

[See table here](/doc/first_follow.md)

# Action Table

The rows of the  `action table` represent the variables and the columns represent the terminals. A cell of this table contains the number of a production rule corresponding to the number in the grammar section.

In the following table, EPSILON_VALUE ≡ _ϵ_.

[See action table here](/doc/action-table.pdf)

# Code generation

## Introduction

In the code generation, the support of the real type and the function creation has not been done because it is not required.

For the code generation, we used an ordinary recursive parser, which calls methods of the class _LlvmCodeGenerate_ which generates the code. _LlvmCodeGenerate_ has a global stack that allows storing expressions, i.e. variables, integer, or boolean values. This stack is indispensable because any combined instruction must be divided into a series of binary actions, for example, `a = 1 + 1` You have to start by evaluating `1 + 1`, then store the result in a temporary variable and then assign this temporary variable to `a`. The stack is used to transfer the temporary variable.

## Conditional structures

The _if_ _else_ is already implemented in _LLVM_ by a function that allows jumping to an anchor or another one according to the value of a boolean expression. Example:

```
def i32 compareTo ( i32 %a, i32 %b){
    entry :
        % cond = icmp slt i32 %a ,%b
        br i1 %cond , label %lower , label % greaterORequals
    lower :
        ret i32 -1
    greaterORequals :
        %1 = sub i32 %a ,%b
        ret i32 %1
```

The _else_ _if_ is a cascade of _if_ _else_ where the _else_ is an anchor to the next _if_.

The ternary if is just another way to write an if.

## Loops

The _while_ loop works by using three anchors, one for the condition test, one for the heart of the loop, and one for exiting the loop. We start by testing the condition, if it is false, we jump to the end of the loop, if it is true, we jump to the heart of the loop, once this one is finished, we jump to the test of the condition.

The _for_ loop is another way to write a _while_ loop by defining a variable with a base value that will define the condition of the loop, this one is that this base value must be less than a maximum defined by the user. Finally, after having executed the heart of the loop, you have to increment the value by a number also defined by the user and then jump to the test of the condition of the loop.

## Implemented language features

As mentioned above, grammar contains all the elements present in the language, including the functions. As a result, the parser can recognize the syntax of all elements of the Iulius language.

For semantic analysis and code generation, only two data types can be manipulated: booleans and integers.
Functions are also not available for code generation. The rest of the functionalities are available during the code generation. These features are, for example, the declaration of variables, constants (check that there is no assignment after the original assignment), assignment (and multiple assignments), conversion, basic arithmetic operations (with verification of the types of each operand to check that they are compatible), bit by bit operations, loops (while and for), conditional branches (if, elseif, else), display, retrieval of characters from the input, creation of blocks. The management of the scope of variables is also implemented.
