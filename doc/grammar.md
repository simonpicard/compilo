
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
|11  |                                   |-> |`ϵ`                                     |
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
|30  |                                   |-> |`ϵ`                                     |
|31  |`<TernaryElseExpression>`          |-> |`TERNARY\_ELSE <Expression>`                   |
|32  |`<AtomicExpression>`               |-> |`<AtomicIdentifierExpression>`                 |
|33  |                                   |-> |`INTEGER`                                      |
|34  |                                   |-> |`REAL`                                         |
|35  |                                   |-> |`BOOLEAN`                                      |
|36  |                                   |-> |`<BuiltInFunctionCall>`                        |
|37  |`<AtomicIdentifierExpression>`     |-> |`IDENTIFIER`                                   |
|38  |`<AtomicIdentifierExpressionTail>` |-> |`<FunctionCallTail>`                           |
|39  |                                   |-> |`ϵ`                                     |
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
|51  |                                   |-> |`ϵ`                                     |
|52  |`<BinaryLazyOrExpression>`         |-> |`<BinaryLazyAndExpression>`                    |
|53  |`<BinaryLazyOrExpression'>`        |-> |`LAZY\_AND <BinaryLazyAndExpression>`          |
|54  |                                   |-> |`ϵ`                                     |
|55  |`<BinaryLazyAndExpression>`        |-> |`<BinaryNumericExpression>`                    |
|56  |`<BinaryLazyAndExpression'>`       |-> |`GREATER\_THAN <BinaryNumericExpression>`      |
|57  |                                   |-> |`LESS\_THAN <BinaryNumericExpression>`         |
|58  |                                   |-> |`GREATER\_OR\_EQUALS\_THAN`                    |
|59  |                                   |-> |`LESS\_OR\_EQUALS\_THAN`                       |
|60  |                                   |-> |`EQUALITY <BinaryNumericExpression>`           |
|61  |                                   |-> |`INEQUALITY <BinaryNumericExpression>`         |
|62  |                                   |-> |`ϵ`                                     |
|63  |`<BinaryNumericExpression>`        |-> |`<BinaryTermExpression>`                       |
|64  |`<BinaryNumericExpression'>`       |-> |`PLUS <BinaryTermExpression>`                  |
|65  |                                   |-> |`MINUS <BinaryTermExpression>`                 |
|66  |                                   |-> |`BITWISE\_OR <BinaryTermExpression>`           |
|67  |                                   |-> |`BITWISE\_XOR <BinaryTermExpression>`          |
|68  |                                   |-> |`ϵ`                                     |
|69  |`<BinaryTermExpression>`           |-> |`<BinaryShiftedExpression>`                    |
|70  |`<BinaryTermExpression'>`          |-> |`ARITHMETIC\_SHIFT\_LEFT`                      |
|71  |                                   |-> |`ARITHMETIC\_SHIFT\_RIGHT`                     |
|72  |                                   |-> |`ϵ`                                     |
|73  |`<BinaryShiftedExpression>`        |-> |`<BinaryFactorExpression>`                     |
|74  |`<BinaryShiftedExpression'>`       |-> |`TIMES <BinaryFactorExpression>`               |
|75  |                                   |-> |`DIVIDE <BinaryFactorExpression>`              |
|76  |                                   |-> |`REMAINDER <BinaryFactorExpression>`           |
|77  |                                   |-> |`BITWISE\_AND <BinaryFactorExpression>`        |
|78  |                                   |-> |`INVERSE\_DIVIDE <BinaryFactorExpression>`     |
|79  |                                   |-> |`ϵ`                                     |
|80  |`<BinaryFactorExpression>`         |-> |`<UnaryExpression>`                            |
|81  |`<BinaryFactorExpression'>`        |-> |`POWER <UnaryExpression>`                      |
|82  |                                   |-> |`ϵ`                                     |
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
|95  |                                   |-> |`ϵ`                                     |
|96  |`<ParameterTail>`                  |-> |`COMMA <Expression> <ParameterTail>`           |
|97  |                                   |-> |`ϵ`                                     |
|98  |`<FunctionDefinition>`             |-> |`FUNCTION IDENTIFIER LEFT\_PARENTHESIS`        |
|99  |`<FunctionDefinitionEnd>`          |-> |`RETURN <Expression> END`                      |
|100 |                                   |-> |`END`                                          |
|101 |`<Argument>`                       |-> |`IDENTIFIER TYPE\_DEFINITION <Type>`           |
|102 |                                   |-> |`ϵ`                                     |
|103 |`<ArgumentTail>`                   |-> |`COMMA IDENTIFIER TYPE\_DEFINITION <Type>`     |
|104 |                                   |-> |`ϵ`                                     |