/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import scanner.LexicalAnalyzer;
import scanner.Symbol;
import scanner.SyntaxErrorException;
import utils.datastructure.ActionTable;
import utils.datastructure.Epsilon;
import utils.datastructure.Terminal;
import utils.datastructure.Variable;
import utils.datastructure.ProductionRule;

/**
 *
 * @author arnaud
 */
public class RecursiveParser {

    private LexicalAnalyzer lexicalAnalyzer;
    private Symbol currentSymbol;
    private ActionTable actionTable;
    private Terminal currentTerminal;
    private Variable currentVariable;
    private ProductionRule currentProductionRule;

    private void produce(ProductionRule productionRule) {
        System.out.println("Produce " + productionRule);
    }

    private void match(Terminal terminal) throws IOException, PatternSyntaxException, SyntaxErrorException {
        System.out.println("Match " + terminal);
        currentSymbol = lexicalAnalyzer.nextToken();
        currentTerminal = Terminal.castToTerminal(currentSymbol);
        currentProductionRule = actionTable.getTable().get(currentVariable).get(currentTerminal);
    }
    
    private void matchEpsilon() {
        System.out.println("Match " + Epsilon.getInstance());
    }

    private void error(Variable variable, Terminal terminal) throws Exception {
        System.out.println("Erreur : variable(" + variable + "), terminal(" + terminal + ")");
        throw new Exception();
    }

    private void initStep(String variableName) {
        currentTerminal = Terminal.castToTerminal(currentSymbol);
        currentVariable = new Variable(variableName);

        currentProductionRule = actionTable.getTable().get(currentVariable).get(currentTerminal);

        produce(currentProductionRule);
    }

    public RecursiveParser(String sourcePath, ActionTable actionTable) throws FileNotFoundException, IOException, PatternSyntaxException, SyntaxErrorException {
        this.lexicalAnalyzer = new LexicalAnalyzer(new FileInputStream(sourcePath));
        currentSymbol = lexicalAnalyzer.nextToken();
        this.actionTable = actionTable;
    }

    public void parseProgram() throws Exception {
        initStep("<Program>");
        // [1] <Program> -> <InstructionList>
        if (ProductionRule.productionRules.get(1).equals(currentProductionRule)) {
            parseInstructionList();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
        if (currentTerminal.equals(Epsilon.getInstance())) {
            System.out.println("Accept");
        }
        else {
            System.out.println("Error : remaining input");
        }
    }

    private void parseInstruction() throws Exception {
        initStep("<Instruction>");
        // [2] <Instruction> -> <IdentifierInstruction>
        if (ProductionRule.productionRules.get(2).equals(currentProductionRule)) {
            parseIdentifierInstruction();
        } // [3] <Instruction> -> <ConstDefinition>
        else if (ProductionRule.productionRules.get(3).equals(currentProductionRule)) {
            parseConstDefinition();
        } // [4] <Instruction> -> <Block>
        else if (ProductionRule.productionRules.get(4).equals(currentProductionRule)) {
            parseBlock();
        } // [5] <Instruction> -> <Loop>
        else if (ProductionRule.productionRules.get(5).equals(currentProductionRule)) {
            parseLoop();
        } // [6] <Instruction> -> <BuiltInFunctionCall>
        else if (ProductionRule.productionRules.get(6).equals(currentProductionRule)) {
            parseBuiltInFunctionCall();
        } // [7] <Instruction> -> <FunctionDefinition>
        else if (ProductionRule.productionRules.get(7).equals(currentProductionRule)) {
            parseFunctionDefinition();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseInstructionList() throws Exception {
        initStep("<InstructionList>");
        // [8] <InstructionList> -> <Instruction> <InstructionListTail>
        if (ProductionRule.productionRules.get(8).equals(currentProductionRule)) {
            parseInstruction();
            parseInstructionListTail();
        } // [9] <InstructionList> -> <InstructionListTail>
        else if (ProductionRule.productionRules.get(9).equals(currentProductionRule)) {
            parseInstructionListTail();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseInstructionListTail() throws Exception {
        initStep("<InstructionListTail>");
        // [10] <InstructionListTail> -> END_OF_INSTRUCTION <InstructionList>
        if (ProductionRule.productionRules.get(10).equals(currentProductionRule)) {
            match(currentTerminal);
            parseInstructionList();
        } // [11] <InstructionListTail> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(11).equals(currentProductionRule)) {
            matchEpsilon();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseIdentifierInstruction() throws Exception {
        initStep("<IdentifierInstruction>");
        // [12] <IdentifierInstruction> -> IDENTIFIER <IdentifierInstructionTail>
        if (ProductionRule.productionRules.get(12).equals(currentProductionRule)) {
            match(currentTerminal);
            parseIdentifierInstructionTail();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseIdentifierInstructionTail() throws Exception {
        initStep("<IdentifierInstructionTail>");
        // [13] <IdentifierInstructionTail> -> <AssignationTail>
        if (ProductionRule.productionRules.get(13).equals(currentProductionRule)) {
            parseAssignationTail();
        } // [14] <IdentifierInstructionTail> -> TYPE_DEFINITION <Type>
        else if (ProductionRule.productionRules.get(14).equals(currentProductionRule)) {
            match(currentTerminal);
            parseType();
        } // [15] <IdentifierInstructionTail> -> <FunctionCallTail>
        else if (ProductionRule.productionRules.get(15).equals(currentProductionRule)) {
            parseFunctionCallTail();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseAssignationTail() throws Exception {
        initStep("<AssignationTail>");
        // [16] <AssignationTail> -> ASSIGNATION <Expression>
        if (ProductionRule.productionRules.get(16).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
        }

        // [17] <AssignationTail> -> COMMA IDENTIFIER <AssignationTail> COMMA <Expression>
        if (ProductionRule.productionRules.get(17).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseAssignationTail();
            match(currentTerminal);
            parseExpression();
        }
    }
    
    private void parseConstDefinition() throws Exception {
        initStep("<ConstDefinition>");
        // [18] <ConstDefinition> -> CONST IDENTIFIER <AssignationTail>
        if (ProductionRule.productionRules.get(18).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseAssignationTail();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBlock() throws Exception {
        initStep("<Block>");
        // [19] <Block> -> LET IDENTIFIER <AssignationTail> END_OF_INSTRUCTION <InstructionList> END
        if (ProductionRule.productionRules.get(19).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseAssignationTail();
            match(currentTerminal);
            parseInstructionList();
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseLoop() throws Exception {
        initStep("<Loop>");
        // [20] <Loop> -> <If>
        if (ProductionRule.productionRules.get(20).equals(currentProductionRule)) {
            parseIf();
        }
        // [21] <Loop> -> WHILE <Expression> END_OF_INSTRUCTION <InstructionList> END
        else if (ProductionRule.productionRules.get(21).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            parseInstructionList();
            match(currentTerminal);
        }
        // [22] <Loop> -> FOR IDENTIFIER ASSIGNATION <Expression> TERNARY_ELSE <Expression> <ForTail>
        else if (ProductionRule.productionRules.get(22).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            parseExpression();
            parseForTail();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseForTail() throws Exception {
        initStep("<ForTail>");
        // [23] <ForTail> -> END_OF_INSTRUCTION <InstructionList> END
        if (ProductionRule.productionRules.get(23).equals(currentProductionRule)) {
            match(currentTerminal);
            parseInstructionList();
            match(currentTerminal);
        }
        // [24] <ForTail> -> TERNARY_ELSE <Expresssion> END_OF_INSTRUCTION <InstructionList> END
        else if (ProductionRule.productionRules.get(24).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            parseInstructionList();
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseType() throws Exception {
        initStep("<Type>");
        // [25] <Type> -> BOOLEAN_TYPE
        if (ProductionRule.productionRules.get(25).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // [26] <Type> -> REAL_TYPE
        else if (ProductionRule.productionRules.get(26).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // [27] <Type> -> INTEGER_TYPE
        else if (ProductionRule.productionRules.get(27).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseExpression() throws Exception {
        initStep("<Expression>");
        // [28] <Expression> -> <BinaryExpression> <TernaryIfExpression>
        if (ProductionRule.productionRules.get(28).equals(currentProductionRule)) {
            parseBinaryExpression();
            parseTernaryIfExpression();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseTernaryIfExpression() throws Exception {
        initStep("<TernaryIfExpression>");
        // [29] <TernaryIfExpression> -> TERNARY_IF <Expression> <TernaryElseExpression>
        if (ProductionRule.productionRules.get(29).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            parseTernaryElseExpression();
        }
        // [30] <TernaryIfExpression> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(30).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseTernaryElseExpression() throws Exception {
        initStep("<TernaryElseExpression>");
        // [31] <TernaryElseExpression> -> TERNARY_ELSE <Expression>
        if (ProductionRule.productionRules.get(31).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseAtomicExpression() throws Exception {
        initStep("<AtomicExpression>");
        // [32] <AtomicExpression> -> <AtomicIdentifierExpression>
        if (ProductionRule.productionRules.get(32).equals(currentProductionRule)) {
            parseAtomicIdentifierExpression();
        }
        // [33] <AtomicExpression> -> INTEGER
        else if (ProductionRule.productionRules.get(33).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // [34] <AtomicExpression> -> REAL
        else if (ProductionRule.productionRules.get(34).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // [35] <AtomicExpression> -> BOOLEAN
        else if (ProductionRule.productionRules.get(35).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // [36] <AtomicExpression> -> <BuiltInFunctionCall>
        else if (ProductionRule.productionRules.get(36).equals(currentProductionRule)) {
            parseBuiltInFunctionCall();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseAtomicIdentifierExpression() throws Exception {
        initStep("<AtomicIdentifierExpression>");
        // [37] <AtomicIdentifierExpression> -> IDENTIFIER <AtomicIdentifierExpressionTail>
        if (ProductionRule.productionRules.get(37).equals(currentProductionRule)) {
            match(currentTerminal);
            parseAtomicIdentifierExpressionTail();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseAtomicIdentifierExpressionTail() throws Exception {
        initStep("<AtomicIdentifierExpressionTail>");
        // [38] <AtomicIdentifierExpressionTail> -> <FunctionCallTail>
        if (ProductionRule.productionRules.get(38).equals(currentProductionRule)) {
            parseFunctionCallTail();
        }
        // [39] <AtomicIdentifierExpressionTail> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(39).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseUnaryExpression() throws Exception {
        initStep("<UnaryExpression>");
        // [40] <UnaryExpression> -> NEGATION <UnaryExpression>
        if (ProductionRule.productionRules.get(40).equals(currentProductionRule)) {
            match(currentTerminal);
            parseUnaryExpression();
        }
        // [41] <UnaryExpression> -> <UnaryBitwiseNotExpression>
        else if (ProductionRule.productionRules.get(41).equals(currentProductionRule)) {
            parseUnaryBitwiseNotExpression();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseUnaryBitwiseNotExpression() throws Exception {
        initStep("<UnaryBitwiseNotExpression>");
        // [42] <UnaryBitwiseNotExpression> -> BITWISE_NOT <UnaryBitwiseNotExpression>
        if (ProductionRule.productionRules.get(42).equals(currentProductionRule)) {
            match(currentTerminal);
            parseUnaryBitwiseNotExpression();
        }
        // [43] <UnaryBitwiseNotExpression> -> <UnaryMinusPlusExpression>
        else if (ProductionRule.productionRules.get(43).equals(currentProductionRule)) {
            parseUnaryMinusPlusExpression();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseUnaryMinusPlusExpression() throws Exception {
        initStep("<UnaryMinusPlusExpression>");
        // [44] <UnaryMinusPlusExpression> -> MINUS <UnaryMinusPlusExpression>
        if (ProductionRule.productionRules.get(44).equals(currentProductionRule)) {
            match(currentTerminal);
            parseUnaryMinusPlusExpression();
        }
        // [45] <UnaryMinusPlusExpression> -> PLUS <UnaryMinusPlusExpression>
        else if (ProductionRule.productionRules.get(45).equals(currentProductionRule)) {
            match(currentTerminal);
            parseUnaryMinusPlusExpression();
        }
        // [46] <UnaryMinusPlusExpression> -> <UnaryAtomicExpression>
        else if (ProductionRule.productionRules.get(46).equals(currentProductionRule)) {
            parseUnaryAtomicExpression();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseUnaryAtomicExpression() throws Exception {
        initStep("<UnaryAtomicExpression>");
        // [47] <UnaryAtomicExpression> -> <AtomicExpression>
        if (ProductionRule.productionRules.get(47).equals(currentProductionRule)) {
            parseAtomicExpression();
        }
        // [48] <UnaryAtomicExpression> -> LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (ProductionRule.productionRules.get(48).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryExpression() throws Exception {
        initStep("<BinaryExpression>");
        // [49] <BinaryExpression>-><BinaryLazyOrExpression> <BinaryExpression'>
        if (ProductionRule.productionRules.get(49).equals(currentProductionRule)) {
            parseBinaryLazyOrExpression();
            parseBinaryExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryExpressionPrim() throws Exception {
        initStep("<BinaryExpression'>");
        // [50] <BinaryExpression'> -> LAZY_OR <BinaryLazyOrExpression> <BinaryExpression'>
        if (ProductionRule.productionRules.get(50).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryLazyOrExpression();
            parseBinaryExpressionPrim();
        }
        // [51] <BinaryExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(51).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryLazyOrExpression() throws Exception {
        initStep("<BinaryLazyOrExpression>");
        // [52] <BinaryLazyOrExpression> -> <BinaryLazyAndExpression> <BinaryLazyOrExpression'>
        if (ProductionRule.productionRules.get(52).equals(currentProductionRule)) {
            parseBinaryLazyAndExpression();
            parseBinaryLazyOrExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryLazyOrExpressionPrim() throws Exception {
        initStep("<BinaryLazyOrExpression'>");
        // [53] <BinaryLazyOrExpression'> -> LAZY_AND <BinaryLazyAndExpression> <BinaryLazyOrExpression'>
        if (ProductionRule.productionRules.get(53).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryLazyAndExpression();
            parseBinaryLazyOrExpressionPrim();
        }
        // [54] <BinaryExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(54).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryLazyAndExpression() throws Exception {
        initStep("<BinaryLazyAndExpression>");
        // [55] <BinaryLazyAndExpression> -> <BinaryNumericExpression> <BinaryLazyAndExpression'>
        if (ProductionRule.productionRules.get(55).equals(currentProductionRule)) {
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryLazyAndExpressionPrim() throws Exception {
        initStep("<BinaryLazyAndExpression'>");
        // [56] <BinaryLazyAndExpression'> -> GREATER_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        if (ProductionRule.productionRules.get(56).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // [57] <BinaryLazyAndExpression'> -> LESS_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (ProductionRule.productionRules.get(57).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // [58] <BinaryLazyAndExpression'> -> GREATER_OR_EQUALS_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (ProductionRule.productionRules.get(58).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // [59] <BinaryLazyAndExpression'> -> LESS_OR_EQUALS_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (ProductionRule.productionRules.get(59).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // [60] <BinaryLazyAndExpression'> -> EQUALITY <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (ProductionRule.productionRules.get(60).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // [61] <BinaryLazyAndExpression'> -> INEQUALITY <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (ProductionRule.productionRules.get(61).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            parseBinaryLazyAndExpressionPrim();
        }
        // [62] <BinaryLazyAndExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(62).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryNumericExpression() throws Exception {
        initStep("<BinaryNumericExpression>");
        // [63] <BinaryNumericExpression> -> <BinaryTermExpression> <BinaryNumericExpression'>
        if (ProductionRule.productionRules.get(63).equals(currentProductionRule)) {
            parseBinaryTermExpression();
            parseBinaryNumericExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryNumericExpressionPrim() throws Exception {
        initStep("<BinaryNumericExpression'>");
        // [64] <BinaryNumericExpression'> -> PLUS <BinaryTermExpression> <BinaryNumericExpression'>
        if (ProductionRule.productionRules.get(64).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryTermExpression();
            parseBinaryNumericExpressionPrim();
        }
        // [65] <BinaryNumericExpression'> -> MINUS <BinaryTermExpression> <BinaryNumericExpression'>
        else if (ProductionRule.productionRules.get(65).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryTermExpression();
            parseBinaryNumericExpressionPrim();
        }
        // [66] <BinaryNumericExpression'> -> BITWISE_OR <BinaryTermExpression> <BinaryNumericExpression'>
        else if (ProductionRule.productionRules.get(66).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryTermExpression();
            parseBinaryNumericExpressionPrim();
        }
        // [67] <BinaryNumericExpression'> -> BITWISE_XOR <BinaryTermExpression> <BinaryNumericExpression'>
        else if (ProductionRule.productionRules.get(67).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryTermExpression();
            parseBinaryNumericExpressionPrim();
        }
        // [68] <BinaryNumericExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(68).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryTermExpression() throws Exception {
        initStep("<BinaryTermExpression>");
        // [69] <BinaryTermExpression> -> <BinaryShiftedExpression> <BinaryTermExpression'>
        if (ProductionRule.productionRules.get(69).equals(currentProductionRule)) {
            parseBinaryShiftedExpression();
            parseBinaryTermExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryTermExpressionPrim() throws Exception {
        initStep("<BinaryTermExpression'>");
        // [70] <BinaryTermExpression'> -> ARITHMETIC_SHIFT_LEFT <BinaryShiftedExpression> <BinaryTermExpression'>
        if (ProductionRule.productionRules.get(70).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryShiftedExpression();
            parseBinaryTermExpressionPrim();
        }
        // [71] <BinaryTermExpression'> -> ARITHMETIC_SHIFT_RIGHT <BinaryShiftedExpression> <BinaryTermExpression'>
        else if (ProductionRule.productionRules.get(71).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryShiftedExpression();
            parseBinaryTermExpressionPrim();
        }
        // [72] <BinaryTermExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(72).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryShiftedExpression() throws Exception {
        initStep("<BinaryShiftedExpression>");
        // [73] <BinaryShiftedExpression> -> <BinaryFactorExpression> <BinaryShiftedExpression'>
        if (ProductionRule.productionRules.get(73).equals(currentProductionRule)) {
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryShiftedExpressionPrim() throws Exception {
        initStep("<BinaryShiftedExpression'>");
        // [74] <BinaryShiftedExpression'> -> TIMES <BinaryFactorExpression> <BinaryShiftedExpression'>
        if (ProductionRule.productionRules.get(74).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // [75] <BinaryShiftedExpression'> -> DIVIDE <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (ProductionRule.productionRules.get(75).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // [76] <BinaryShiftedExpression'> -> REMAINDER <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (ProductionRule.productionRules.get(76).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // [77] <BinaryShiftedExpression'> -> BITWISE_AND <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (ProductionRule.productionRules.get(77).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // [78] <BinaryShiftedExpression'> -> INVERSE_DIVIDE <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (ProductionRule.productionRules.get(78).equals(currentProductionRule)) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // [79] <BinaryShiftedExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(79).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryFactorExpression() throws Exception {
        initStep("<BinaryFactorExpression>");
        // [80] <BinaryFactorExpression> -> <UnaryExpression> <BinaryFactorExpression'>
        if (ProductionRule.productionRules.get(80).equals(currentProductionRule)) {
            parseUnaryExpression();
            parseBinaryFactorExpressionPrim();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBinaryFactorExpressionPrim() throws Exception {
        initStep("<BinaryFactorExpression'>");
        // [81] <BinaryFactorExpression'> -> POWER <UnaryExpression> <BinaryFactorExpression'>
        if (ProductionRule.productionRules.get(81).equals(currentProductionRule)) {
            match(currentTerminal);
            parseUnaryExpression();
            parseBinaryFactorExpressionPrim();
        }
        // [82] <BinaryFactorExpression'> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(82).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseIf() throws Exception {
        initStep("<If>");
        // [83] <If> -> IF <Expression> END_OF_INSTRUCTION <InstructionList> <IfEnd>
        if (ProductionRule.productionRules.get(83).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            parseInstructionList();
            parseIfEnd();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseIfEnd() throws Exception {
        initStep("<IfEnd>");
        // [84] <IfEnd> -> ELSE_IF <Expression> END_OF_INSTRUCTION <InstructionList> <IfEnd>
        if (ProductionRule.productionRules.get(84).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            parseInstructionList();
            parseIfEnd();
        }
        // [85] <IfEnd> -> ELSE <InstructionList> END
        else if (ProductionRule.productionRules.get(85).equals(currentProductionRule)) {
            match(currentTerminal);
            parseInstructionList();
            match(currentTerminal);
        }
        // [86] <IfEnd> -> END
        else if (ProductionRule.productionRules.get(86).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBuiltInFunctionCall() throws Exception {
        initStep("<BuiltInFunctionCall>");
        // [87] <BuiltInFunctionCall> -> READ_REAL LEFT_PARENTHESIS RIGHT_PARENTHESIS
        if (ProductionRule.productionRules.get(87).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
        }
        // [88] <BuiltInFunctionCall> -> READ_INTEGER LEFT_PARENTHESIS RIGHT_PARENTHESIS
        else if (ProductionRule.productionRules.get(88).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
        }
        // [89] <BuiltInFunctionCall> -> INTEGER_CAST LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (ProductionRule.productionRules.get(89).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [90] <BuiltInFunctionCall> -> REAL_CAST LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (ProductionRule.productionRules.get(90).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [91] <BuiltInFunctionCall> -> BOOLEAN_CAST LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (ProductionRule.productionRules.get(91).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [92] <BuiltInFunctionCall> -> PRINTLN LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (ProductionRule.productionRules.get(92).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseFunctionCallTail() throws Exception {
        initStep("<FunctionCallTail>");
        // [93] <FunctionCallTail> -> LEFT_PARENTHESIS <Parameter> RIGHT_PARENTHESIS
        if (ProductionRule.productionRules.get(93).equals(currentProductionRule)) {
            match(currentTerminal);
            parseParameter();
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseParameter() throws Exception {
        initStep("<Parameter>");
        // [94] <Parameter> -> <Expression> <ParameterTail>
        if (ProductionRule.productionRules.get(94).equals(currentProductionRule)) {
            parseExpression();
            parseParameterTail();
        }
        // [95] <Parameter> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(95).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseParameterTail() throws Exception {
        initStep("<ParameterTail>");
        // [96] <ParameterTail> -> COMMA <Expression> <ParameterTail>
        if (ProductionRule.productionRules.get(96).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            parseParameterTail();
        }
        // [97] <ParameterTail> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(97).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseFunctionDefinition() throws Exception {
        initStep("<FunctionDefinition>");
        // [98] <FunctionDefinition> -> FUNCTION IDENTIFIER LEFT_PARENTHESIS <Argument> RIGHT_PARENTHESIS <InstructionList> <FunctionDefinitionEnd>
        if (ProductionRule.productionRules.get(98).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
            parseArgument();
            match(currentTerminal);
            parseInstructionList();
            parseFunctionDefinitionEnd();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseFunctionDefinitionEnd() throws Exception {
        initStep("<FunctionDefinitionEnd>");
        // [99] <FunctionDefinitionEnd> -> RETURN <Expression> END
        if (ProductionRule.productionRules.get(99).equals(currentProductionRule)) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [100] <FunctionDefinitionEnd> -> END
        else if (ProductionRule.productionRules.get(100).equals(currentProductionRule)) {
            match(currentTerminal);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseArgument() throws Exception {
        initStep("<Argument>");
        // [101] <Argument> -> IDENTIFIER TYPE_DEFINITION <Type> <ArgumentTail>
        if (ProductionRule.productionRules.get(101).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            parseType();
            parseArgumentTail();
        }
        // [102] <Argument> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(102).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseArgumentTail() throws Exception {
        initStep("<ArgumentTail>");
        // [103] <ArgumentTail> -> COMMA IDENTIFIER TYPE_DEFINITION <Type> <ArgumentTail>
        if (ProductionRule.productionRules.get(103).equals(currentProductionRule)) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
            parseType();
            parseArgumentTail();
        }
        // [104] <ArgumentTail> -> EPSILON_VALUE
        else if (ProductionRule.productionRules.get(104).equals(currentProductionRule)) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
}
