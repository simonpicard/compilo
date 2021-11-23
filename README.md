# Quickstart

```
java -jar dist/compiler.jar Main test/test.il
```

Explore results in `test/test.ll`

# Introduction

Goal of this project is to create a compilator for a programming language called Iulius.

# Regular expressions

In order to describe the words (tokens) accepted by the Iulius language, we use the extended regular expressions.

Here is the table which contains all the tokens of the Iulius language.
Each line of this table is composed of the name of the token, the type in the LexicalUnit enum associated with it and the regular expression that corresponds to corresponding to this token.

[See table here](/doc/regex_token.md)

# Implementation choices

## Number management and operations

In order to handle arithmetic expressions containing numbers and the plus and operators, we had to remove the plus and minus operators in the regular minus operators in the regular expression of integers and real numbers. For example, if we have 4+5 we will detect "4", "+" and "5" with our implementation while if we had left the plus in the regular expression of integers expression we would have obtained "4" and "+5". On the other hand an integer +2 will be detected as "+" and "2" but it will be easier to easier to interpret when building the syntax tree.

## DFA

First, we use the following notations:

- means the set of lower case letters from a to z
- means the set of upper case letters from a to z
- means the set of numbers from 0 to 9
- By extension \[d-y\] means the set of lower case letters from d to y
- means the . in regular expressions, i.e. all the characters

Then, in the right part of the initial state in the graph, that is the different keywords and identifiers, for more readability we have not included the transitions from a state that is part of a keyword to an keyword to an identifier, normally each state of a keyword should contain a transition from contain a transition from itself to the identifier state, the transition includes {\[a-z\] , \[A-Z\], \[0-9\], \_} excluding the other transitions out of this state other transitions out of this state.

# Transformation of the given grammar into LL1

## Binary and unary operators

The first step in making the grammar LL(1) was to distinguish two types of two types of operators: the binary operators which act on two expressions expressions, one on the left and the other on the right of these operators operators and unary operators which act on a single expression located on the right of of these operators. It is necessary to differentiate the two types of of operators in order not to have expressions like : &gt;&gt;4, \*2, 6\|,... There are four unary operators: ! (negation), ∼ (not bit by bit), + and -. The operators + and - are are also binary operators.

## Operator priority and associativity

In this step, we modified the grammar in order to set the priority and associativity of priority and associativity of the different operators, in order to make the grammar make the grammar less ambiguous. We noticed that the unary operators had a higher priority than binary operators and that unary operators were all associative on the right, while while the binary operators were all associative on the left. To fix the priority, we put the lowest priority operators as high as possible in the "the highest possible priority in the grammar and the highest priority as low as possible.
possible. In the grammar below where the "start symbol" is E, the operator "+" is considered as "higher" than the operator "\*" operator in the grammar because it is possible to obtain the "+" operator by using fewer production rules from the "start symbol" than to obtain the "\*" operator.

    E->E + T
     ->T
    T->T * F
     ->F
    F->ID
     ->( E )

## Deleting left-hand recursions

The next step was the removal of left-hand recursions which are incompatible with incompatible with the top-down parser because it makes this kind of parser enter an parser into an infinite loop or recursion. This step makes any associative operator to the right if nothing is done to solve this problem problem during the semantic analysis.

## Left factoring

This step gathers the production rules of a variable that have a common prefix into a single production rule that contains this common prefix and a and a new variable. This new variable has production rules to the different suffixes that were originally present in the originally present in the different production rules that have been pooled.
in common. This is necessary because the parser we are going to create is LL(1), so it must be able to choose the right so it must be able to choose the right production rule by looking at the next only looking at the next token that is in input.

## The variables &lt;Instruction&gt; and &lt;InstructionList&gt;

In the given grammar, whenever the variable &lt;Instruction&gt; was in the right part of a production rule except when the left part of the except when the left part of the rule was &lt;InstructionList&gt;, there was another production rule for this same left this same left part where &lt;Instruction&gt; was replaced by &lt;InstructionList&gt;. For example, &lt;If&gt;→&lt;Expression&gt; &lt;Empty&gt; &lt;InstructionList&gt; &lt;IfEnd&gt; and &lt;If&gt;→&lt;Expression&gt; &lt;Empty&gt; &lt;Instruction&gt; &lt;IfEnd&gt;. This makes it possible not to put an END_OF_INSTRUCTION at the end of a &lt;Instruction&gt; when the body of a block (here, the body of a block (here, the body of the "if") contained only one &lt;Instruction&gt;. The problem posed by the systematic doubling of production rules containing instructions containing instructions (one rule for &lt;Instruction&gt; and another one for another for &lt;InstructionList&gt;) is that it is not factorized on the left and that the first and that the first of &lt;InstructionList&gt; can be &lt;Instruction&gt;. In order to solve this problem, we decided to only use the variable &lt;InstructionList&gt; in the other production rules. Thus, we avoid the duplication of the rules of production rules. An &lt;InstructionList&gt; is a list of &lt;Instruction&gt; separated by ENDs, with the last &lt;Instruction&gt; being the last &lt;Instruction&gt; not necessarily followed by an END_OF_INSTRUCTION. This makes it possible to have a program like : if(a&gt;b);a=10 end; So only a &lt;InstructionList&gt; allows to produce &lt;Instruction&gt;. Moreover, since a InstructionList&gt; can contain empty instructions (an instruction containing only END_OF_INSTRUCTION) the variable &lt;Empty&gt; variable is no longer necessary because it was only used to to indicate that at least one END terminal was required.

## Functions

Functions provide two additional types of instructions: function definitions and function definitions and function calls. The calls are considered as atomic expressions in order to be able to put function calls function calls within an expression. For example, a=foo()+5; In addition, a Moreover, a function must be able to be called without the need to to make an assignment or another type of instruction because a function can return nothing and simply have an edge effect. For For example, the println function is a function that returns nothing but produces an edge effect which is the display in the terminal. It is necessary to therefore be able to do, for example, println(a). That's why it is necessary a function call to be a statement in its own right.

## Instructions starting with an identifier

Several instructions start with an identifier. These are assignments, variable declarations and function calls.
In order for the grammar to be LL(1) we need to factor these statements.
This is why the variable &lt;IdentifierInstruction&gt; was created. This variable represents an instruction that starts with an identifier. The variable &lt;IdentifierInstructionTail&gt; has also been also created in order to distinguish the different types of instructions that start with an identifier. The distinction is then easily made because when the symbol following the identifier is "=", we know that it is an assignment, when this symbol is "::", we know that it is a a variable declaration and when this symbol is "(", we know that it is a function that it is a function call.

# Grammar

Here is the grammar given in the statement and transformed into LL(1). To each production rule corresponds a number which identifies that rule.

[See table here](/doc/grammar.md)

# First and Follow sets

In the following table, EPSILON_VALUE ≡ _ϵ_.

[See table here](/doc/first_follow.md)

# Action Table

The rows of the "action table" represent the variables and the columns represent the columns represent the terminals. A cell of this table contains the number of a production rule corresponding to the number in the the grammar section.

In the following table, EPSILON_VALUE ≡ _ϵ_.

[See action table here](/doc/action-table.pdf)

# Code generation

## Introduction

In the code generation, the support of the real type and of the function function creation has not been done because it is not required.

For the code generation we used an ordinary recursive parser parser, which calls methods of the class class _LlvmCodeGenerate_ which generate the code. _LlvmCodeGenerate_ has a global stack which allows to store expressions, so variables, integer or boolean values.
integer or boolean values. This stack is indispensable because any combined instruction must be divided into a series of binary actions, for example : _a_ = 1 + 1 You have to start by evaluating 1 + 1, sotcker the result in a temporary temporary variable and then assign this temporary variable to _a_. The stack is used to transfer the temporary variable.

## Conditional structures

The _if_ _else_ is already implemented in LLVM by a function which allows to jumper to an anchor or another one according to the value of a boolean of a boolean expression. Example:

    def i32 compareTo ( i32 %a, i32 %b){
        entry :
            % cond = icmp slt i32 %a ,%b
            br i1 %cond , label %lower , label % greaterORequals
        lower :
            ret i32 -1
        greaterORequals :
            %1 = sub i32 %a ,%b
            ret i32 %1

The _else_ _if_ is a cascade of _if_ _else_ where the _else_ is an anchor to the next _if_.

The ternary if is just another way to write an if.

## Loops

The _while_ loop works by using three anchors, one for the condition test, one for the heart of the loop and one for exit the loop. We start by testing the condition, if it is false, we jump to the end of the the end of the loop, if it is true, we jump to the heart of the loop, once once this one is finished, we jump to the test of the condition.

The _for_ loop is another way to write a _while_ loop by defining a variable with a base value that will define the condition of the define the condition of the loop, this one is that this base value must be value must be less than a maximum defined by the user.
defined by the user. Finally, after having executed the heart of the loop, you have to increment the value by a number also defined by the user and then jump to the test of the jump to the test of the condition of the loop.

## Implemented language features

As mentioned above, the grammar contains all the elements present in the language, including the functions. As a result, the parser is able to recognize the syntax of all elements of the Iulius language.

For semantic analysis and code generation, only two data types can be manipulated: booleans and integers.
Functions are also not available for code generation. The rest of the functionalities are available during the code generation. These features are, for example: the declaration of variables, constants (check that there is no assignment after the original assignment), assignment (and multiple assignment), conversion, basic arithmetic operations (with verification of operations (with verification of the types of each operand in order to check that they are that they are compatible), bit by bit operations, loops (while and for), conditional and for), conditional branches (if, elseif, else), display, retrieval of characters from the input, creation of blocks. The management of the scope of variables is also realized.
