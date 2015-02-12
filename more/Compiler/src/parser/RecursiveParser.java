/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import codegenerator.LlvmCodeGenerator;
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
    private TableOfSymbols tableOfSymbols;
    private LlvmCodeGenerator generator;

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
        this.tableOfSymbols = new TableOfSymbols();
        generator = new LlvmCodeGenerator("../../test/test.ll");
    }
    
    public void parseProgram() throws Exception {
        initStep("<Program>");
        
        generator.initialize();
        generator.main();
        tableOfSymbols.addFrame(new Frame());
        // [1] <Program> -> <InstructionList>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 1) {
            parseInstructionList();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
        tableOfSymbols.removeFrame();
        generator.mainEnd();
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 2) {
            parseIdentifierInstruction();
        } // [3] <Instruction> -> <ConstDefinition>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 3) {
            parseConstDefinition();
        } // [4] <Instruction> -> <Block>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 4) {
            parseBlock();
        } // [5] <Instruction> -> <Loop>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 5) {
            parseLoop();
        } // [6] <Instruction> -> <BuiltInFunctionCall>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 6) {
            parseBuiltInFunctionCall();
        } // [7] <Instruction> -> <FunctionDefinition>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 7) {
            parseFunctionDefinition();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseInstructionList() throws Exception {
        initStep("<InstructionList>");
        // [8] <InstructionList> -> <Instruction> <InstructionListTail>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 8) {
            parseInstruction();
            parseInstructionListTail();
        } // [9] <InstructionList> -> <InstructionListTail>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 9) {
            parseInstructionListTail();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseInstructionListTail() throws Exception {
        initStep("<InstructionListTail>");
        // [10] <InstructionListTail> -> END_OF_INSTRUCTION <InstructionList>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 10) {
            match(currentTerminal);
            parseInstructionList();
        } // [11] <InstructionListTail> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 11) {
            matchEpsilon();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseIdentifierInstruction() throws Exception {
        initStep("<IdentifierInstruction>");
        // [12] <IdentifierInstruction> -> IDENTIFIER <IdentifierInstructionTail>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 12) {
            String identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            parseIdentifierInstructionTail(identifier);
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseIdentifierInstructionTail(String identifier) throws Exception {
        initStep("<IdentifierInstructionTail>");
        // [13] <IdentifierInstructionTail> -> <AssignationTail>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 13) {
            parseAssignationTail(identifier);
        } // [14] <IdentifierInstructionTail> -> TYPE_DEFINITION <Type>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 14) {
            match(currentTerminal);
            parseType(identifier);
        } // [15] <IdentifierInstructionTail> -> <FunctionCallTail>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 15) {
            parseFunctionCallTail();
        } // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }

    private void parseAssignationTail(String identifier) throws Exception {
        initStep("<AssignationTail>");
        // [16] <AssignationTail> -> ASSIGNATION <Expression>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 16) {
            match(currentTerminal);
            parseExpression();
            FrameVar var = (FrameVar) tableOfSymbols.lookup(identifier);
            generator.assignation(var.getAddress(), var.getType());
        }

        // [17] <AssignationTail> -> COMMA IDENTIFIER <AssignationTail> COMMA <Expression>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 17) {
            match(currentTerminal);
            identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            parseAssignationTail(identifier);
            match(currentTerminal);
            parseExpression();
        }
        
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseConstDefinition() throws Exception {
        initStep("<ConstDefinition>");
        // [18] <ConstDefinition> -> CONST IDENTIFIER <AssignationTail>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 18) {
            match(currentTerminal);
            String identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            parseAssignationTail(identifier);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    // Faut peut etre modifier la grammaire pour avoir : let a::Integer = 1 et pas let a = 1
    private void parseBlock() throws Exception {
        initStep("<Block>");
        tableOfSymbols.addFrame(new Frame());
        // [19] <Block> -> LET IDENTIFIER <AssignationTail> END_OF_INSTRUCTION <InstructionList> END
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 19) {
            match(currentTerminal);
            String identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            parseAssignationTail(identifier);
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 20) {
            parseIf();
        }
        // [21] <Loop> -> WHILE <Expression> END_OF_INSTRUCTION <InstructionList> END
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 21) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            tableOfSymbols.addFrame(new Frame());
            parseInstructionList();
            match(currentTerminal);
            tableOfSymbols.removeFrame();
        }
        // [22] <Loop> -> FOR IDENTIFIER ASSIGNATION <Expression> TERNARY_ELSE <Expression> <ForTail>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 22) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 23) {
            match(currentTerminal);
            tableOfSymbols.addFrame(new Frame());
            parseInstructionList();
            match(currentTerminal);
            tableOfSymbols.removeFrame();
        }
        // [24] <ForTail> -> TERNARY_ELSE <Expresssion> END_OF_INSTRUCTION <InstructionList> END
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 24) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            tableOfSymbols.addFrame(new Frame());
            parseInstructionList();
            match(currentTerminal);
            tableOfSymbols.removeFrame();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseType(String identifier) throws Exception {
        initStep("<Type>");
        // [25] <Type> -> BOOLEAN_TYPE
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 25) {
            FrameVar fv = new FrameVar(Type.bool);
            tableOfSymbols.addNewEntry(identifier, fv);
            generator.varDeclaration(fv.getAddress(), fv.getType());
            match(currentTerminal);
        }
        // [26] <Type> -> REAL_TYPE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 26) {
            FrameVar fv = new FrameVar(Type.real);
            tableOfSymbols.addNewEntry(identifier, fv);
            generator.varDeclaration(fv.getAddress(), fv.getType());
            match(currentTerminal);
        }
        // [27] <Type> -> INTEGER_TYPE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 27) {
            FrameVar fv = new FrameVar(Type.integer);
            tableOfSymbols.addNewEntry(identifier, fv);
            generator.varDeclaration(fv.getAddress(), fv.getType());
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 28) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 29) {
            match(currentTerminal);
            parseExpression();
            parseTernaryElseExpression();
        }
        // [30] <TernaryIfExpression> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 30) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 31) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 32) {
            parseAtomicIdentifierExpression();
        }
        // [33] <AtomicExpression> -> INTEGER
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 33) {
            generator.number(currentSymbol.getValue().toString(), Type.integer);
            match(currentTerminal);
        }
        // [34] <AtomicExpression> -> REAL
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 34) {
            match(currentTerminal);
        }
        // [35] <AtomicExpression> -> BOOLEAN
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 35) {
            generator.number(currentSymbol.getValue().toString(), Type.bool);
            match(currentTerminal);
        }
        // [36] <AtomicExpression> -> <BuiltInFunctionCall>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 36) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 37) {
            String identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            parseAtomicIdentifierExpressionTail(identifier);
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseAtomicIdentifierExpressionTail(String identifier) throws Exception {
        initStep("<AtomicIdentifierExpressionTail>");
        // [38] <AtomicIdentifierExpressionTail> -> <FunctionCallTail>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 38) {
            parseFunctionCallTail();
        }
        // [39] <AtomicIdentifierExpressionTail> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 39) {
            FrameVar var = (FrameVar) tableOfSymbols.lookup(identifier);
            generator.valueOfVariable(var.getAddress(), var.getType());
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 40) {
            match(currentTerminal);
            parseUnaryExpression();
        }
        // [41] <UnaryExpression> -> <UnaryBitwiseNotExpression>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 41) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 42) {
            match(currentTerminal);
            parseUnaryBitwiseNotExpression();
        }
        // [43] <UnaryBitwiseNotExpression> -> <UnaryMinusPlusExpression>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 43) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 44) {
            match(currentTerminal);
            parseUnaryMinusPlusExpression();
        }
        // [45] <UnaryMinusPlusExpression> -> PLUS <UnaryMinusPlusExpression>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 45) {
            match(currentTerminal);
            parseUnaryMinusPlusExpression();
        }
        // [46] <UnaryMinusPlusExpression> -> <UnaryAtomicExpression>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 46) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 47) {
            parseAtomicExpression();
        }
        // [48] <UnaryAtomicExpression> -> LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 48) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 49) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 50) {
            match(currentTerminal);
            parseBinaryLazyOrExpression();
            parseBinaryExpressionPrim();
        }
        // [51] <BinaryExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 51) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 52) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 53) {
            match(currentTerminal);
            parseBinaryLazyAndExpression();
            parseBinaryLazyOrExpressionPrim();
        }
        // [54] <BinaryExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 54) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 55) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 56) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            generator.greaterThan();
            parseBinaryLazyAndExpressionPrim();
        }
        // [57] <BinaryLazyAndExpression'> -> LESS_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 57) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            generator.lessThan();
            parseBinaryLazyAndExpressionPrim();
        }
        // [58] <BinaryLazyAndExpression'> -> GREATER_OR_EQUALS_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 58) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            generator.greaterOrEqualsThan();
            parseBinaryLazyAndExpressionPrim();
        }
        // [59] <BinaryLazyAndExpression'> -> LESS_OR_EQUALS_THAN <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 59) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            generator.lessOrEqualsThan();
            parseBinaryLazyAndExpressionPrim();
        }
        // [60] <BinaryLazyAndExpression'> -> EQUALITY <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 60) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            generator.equalsThan();
            parseBinaryLazyAndExpressionPrim();
        }
        // [61] <BinaryLazyAndExpression'> -> INEQUALITY <BinaryNumericExpression> <BinaryLazyAndExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 61) {
            match(currentTerminal);
            parseBinaryNumericExpression();
            generator.notEqualsThan();
            parseBinaryLazyAndExpressionPrim();
        }
        // [62] <BinaryLazyAndExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 62) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 63) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 64) {
            match(currentTerminal);
            parseBinaryTermExpression();
            generator.plus();
            parseBinaryNumericExpressionPrim();
        }
        // [65] <BinaryNumericExpression'> -> MINUS <BinaryTermExpression> <BinaryNumericExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 65) {
            match(currentTerminal);
            parseBinaryTermExpression();
            generator.minus();
            parseBinaryNumericExpressionPrim();
        }
        // [66] <BinaryNumericExpression'> -> BITWISE_OR <BinaryTermExpression> <BinaryNumericExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 66) {
            match(currentTerminal);
            parseBinaryTermExpression();
            generator.bitwiseOr();
            parseBinaryNumericExpressionPrim();
        }
        // [67] <BinaryNumericExpression'> -> BITWISE_XOR <BinaryTermExpression> <BinaryNumericExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 67) {
            match(currentTerminal);
            parseBinaryTermExpression();
            generator.bitwiseXor();
            parseBinaryNumericExpressionPrim();
        }
        // [68] <BinaryNumericExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 68) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 69) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 70) {
            match(currentTerminal);
            parseBinaryShiftedExpression();
            generator.leftShift();
            parseBinaryTermExpressionPrim();
        }
        // [71] <BinaryTermExpression'> -> ARITHMETIC_SHIFT_RIGHT <BinaryShiftedExpression> <BinaryTermExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 71) {
            match(currentTerminal);
            parseBinaryShiftedExpression();
            generator.rightShift();
            parseBinaryTermExpressionPrim();
        }
        // [72] <BinaryTermExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 72) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 73) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 74) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            generator.times();
            parseBinaryShiftedExpressionPrim();
        }
        // [75] <BinaryShiftedExpression'> -> DIVIDE <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 75) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            generator.divide();
            parseBinaryShiftedExpressionPrim();
        }
        // [76] <BinaryShiftedExpression'> -> REMAINDER <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 76) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            generator.remainder();
            parseBinaryShiftedExpressionPrim();
        }
        // [77] <BinaryShiftedExpression'> -> BITWISE_AND <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 77) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            generator.bitwiseAnd();
            parseBinaryShiftedExpressionPrim();
        }
        // [78] <BinaryShiftedExpression'> -> INVERSE_DIVIDE <BinaryFactorExpression> <BinaryShiftedExpression'>
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 78) {
            match(currentTerminal);
            parseBinaryFactorExpression();
            parseBinaryShiftedExpressionPrim();
        }
        // [79] <BinaryShiftedExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 79) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 80) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 81) {
            match(currentTerminal);
            parseUnaryExpression();
            parseBinaryFactorExpressionPrim();
        }
        // [82] <BinaryFactorExpression'> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 82) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 83) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            generator.ifOperation();
            tableOfSymbols.addFrame(new Frame());
            parseInstructionList();
            parseIfEnd();
            generator.endIfBlock();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseIfEnd() throws Exception {
        initStep("<IfEnd>");
        tableOfSymbols.removeFrame();
        generator.endIf();
        // [84] <IfEnd> -> ELSE_IF <Expression> END_OF_INSTRUCTION <InstructionList> <IfEnd>
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 84) {
            match(currentTerminal);
            generator.elseOperation();
            parseExpression();
            match(currentTerminal);
            generator.elseIfOperation();
            tableOfSymbols.addFrame(new Frame());
            parseInstructionList();
            parseIfEnd();
        }
        // [85] <IfEnd> -> ELSE <InstructionList> END
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 85) {
            match(currentTerminal);
            generator.elseOperation();
            tableOfSymbols.addFrame(new Frame());
            parseInstructionList();
            match(currentTerminal);
            tableOfSymbols.removeFrame();
        }
        // [86] <IfEnd> -> END
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 86) {
            match(currentTerminal);
            generator.elseOperation();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseBuiltInFunctionCall() throws Exception {
        initStep("<BuiltInFunctionCall>");
        // [87] <BuiltInFunctionCall> -> READ_REAL LEFT_PARENTHESIS RIGHT_PARENTHESIS
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 87) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
        }
        // [88] <BuiltInFunctionCall> -> READ_INTEGER LEFT_PARENTHESIS RIGHT_PARENTHESIS
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 88) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
        }
        // [89] <BuiltInFunctionCall> -> INTEGER_CAST LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 89) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [90] <BuiltInFunctionCall> -> REAL_CAST LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 90) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [91] <BuiltInFunctionCall> -> BOOLEAN_CAST LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 91) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [92] <BuiltInFunctionCall> -> PRINTLN LEFT_PARENTHESIS <Expression> RIGHT_PARENTHESIS
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 92) {
            match(currentTerminal);
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
            generator.println();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseFunctionCallTail() throws Exception {
        initStep("<FunctionCallTail>");
        // [93] <FunctionCallTail> -> LEFT_PARENTHESIS <Parameter> RIGHT_PARENTHESIS
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 93) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 94) {
            parseExpression();
            parseParameterTail();
        }
        // [95] <Parameter> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 95) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 96) {
            match(currentTerminal);
            parseExpression();
            parseParameterTail();
        }
        // [97] <ParameterTail> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 97) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 98) {
            match(currentTerminal);
            match(currentTerminal);
            match(currentTerminal);
            tableOfSymbols.addFrame(new Frame());
            parseArgument();
            match(currentTerminal);
            parseInstructionList();
            parseFunctionDefinitionEnd();
            tableOfSymbols.removeFrame();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
    
    private void parseFunctionDefinitionEnd() throws Exception {
        initStep("<FunctionDefinitionEnd>");
        // [99] <FunctionDefinitionEnd> -> RETURN <Expression> END
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 99) {
            match(currentTerminal);
            parseExpression();
            match(currentTerminal);
        }
        // [100] <FunctionDefinitionEnd> -> END
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 100) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 101) {
            String identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            match(currentTerminal);
            parseType(identifier);
            parseArgumentTail();
        }
        // [102] <Argument> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 102) {
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
        if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 103) {
            match(currentTerminal);
            String identifier = currentSymbol.getValue().toString();
            match(currentTerminal);
            match(currentTerminal);
            parseType(identifier);
            parseArgumentTail();
        }
        // [104] <ArgumentTail> -> EPSILON_VALUE
        else if (actionTable.getRuleNo(currentProductionRule, currentTerminal) == 104) {
            matchEpsilon();
        }
        // Error
        else {
            error(currentVariable, currentTerminal);
        }
    }
}
