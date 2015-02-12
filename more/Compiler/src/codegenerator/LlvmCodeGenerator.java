/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Stack;
import parser.Type;

/**
 *
 * @author arnaud
 */
public class LlvmCodeGenerator {
    
    public class Expression {
        public String content;
        public Type type;
        
        public Expression(String content, Type type) {
            this.content = content;
            this.type = type;
        }
    }

    private FileOutputStream outputFile;
    //sur le stack : les variables temporaires et les nombres
    private Stack<Expression> expressions;
    private int expressionCounter;

    private static String endOfLine = System.getProperty("line.separator");
    private static Charset charset = Charset.forName("UTF-8");
    private static String varPrefix = "%var";

    public LlvmCodeGenerator(String outputPath) throws FileNotFoundException {
        outputFile = new FileOutputStream(new File(outputPath));
        expressions = new Stack<>();
        expressionCounter = 0;
    }
    
    private void pushExpression(Type type) {
        expressions.push(new Expression("%expr"+String.valueOf(expressionCounter), type));
        ++expressionCounter;
    }
    
    // push un nombre sur le stack
    private void pushExpression(String value, Type type) {
        expressions.push(new Expression(value, type));
    }
    
    private Expression popExpression() {
        return expressions.pop();
    }
    
    private Expression topExpression() {
        return expressions.firstElement();
    }

    public void initialize() throws IOException {
        String res = "";

        // import functions from stdlib
        res += "declare i32 @getchar()" + endOfLine
                + "declare i32 @putchar(i32)" + endOfLine
                + endOfLine;

        // read integer from input
        res += "define i32 @readInt() {" + endOfLine
                + "entry:" + endOfLine
                + "%res = alloca i32" + endOfLine
                + "%digit = alloca i32" + endOfLine
                + "store i32 0, i32* %res" + endOfLine
                + "br label %read" + endOfLine
                + "read:" + endOfLine
                + "%0 = call i32 @getchar()" + endOfLine
                + "%1 = sub i32 %0, 48" + endOfLine
                + "store i32 %1, i32* %digit" + endOfLine
                + "%2 = icmp ne i32 %0, 10" + endOfLine
                + "br i1 %2, label %save , label %exit" + endOfLine
                + "save:" + endOfLine
                + "%3 = load i32* %res" + endOfLine
                + "%4 = load i32* %digit" + endOfLine
                + "%5 = mul i32 %3, 10" + endOfLine
                + "%6 = add i32 %5, %4" + endOfLine
                + "store i32 %6, i32* %res" + endOfLine
                + "br label %read" + endOfLine
                + "exit:" + endOfLine
                + "%7 = load i32* %res" + endOfLine
                + "ret i32 %7" + endOfLine
                + "}" + endOfLine
                + endOfLine;

        // write integer to input
        res += "define void @println(i32 %n) {" + endOfLine
                + "%digitInChar = alloca [32 x i32]" + endOfLine
                + "%number = alloca i32" + endOfLine
                + "store i32 %n, i32* %number" + endOfLine
                + "%numberOfDigits = alloca i32" + endOfLine
                + "store i32 0, i32* %numberOfDigits" + endOfLine
                + "br label %beginloop" + endOfLine
                + "beginloop:" + endOfLine
                + "%1 = load i32* %number" + endOfLine
                + "%2 = icmp ne i32 %1, 0" + endOfLine
                + "br i1 %2, label %ifloop, label %endloop" + endOfLine
                + "ifloop:" + endOfLine
                + "%temp1 = load i32* %numberOfDigits" + endOfLine
                + "%temp2 = load i32* %number" + endOfLine
                + "%divnum = udiv i32 %temp2, 10" + endOfLine
                + "%currentDigit = urem i32 %temp2, 10" + endOfLine
                + "%arrayElem = getelementptr [32 x i32]* %digitInChar, i32 0, i32 %temp1" + endOfLine
                + "store i32 %currentDigit, i32* %arrayElem" + endOfLine
                + "%temp3 = add i32 %temp1, 1" + endOfLine
                + "store i32 %temp3, i32* %numberOfDigits" + endOfLine
                + "store i32 %divnum, i32* %number" + endOfLine
                + "br label %beginloop" + endOfLine
                + "endloop:" + endOfLine
                + "%temp4 = load i32* %numberOfDigits" + endOfLine
                + "%temp41 = sub i32 %temp4, 1" + endOfLine
                + "store i32 %temp41, i32* %numberOfDigits" + endOfLine
                + "br label %beginloop2" + endOfLine
                + "beginloop2:" + endOfLine
                + "%temp5 = load i32* %numberOfDigits" + endOfLine
                + "%arrayElem2 = getelementptr [32 x i32]* %digitInChar, i32 0, i32 %temp5" + endOfLine
                + "%arrayElemValue = load i32* %arrayElem2" + endOfLine
                + "%arrayElemValue2 = add i32 %arrayElemValue, 48" + endOfLine
                + "call i32 @putchar(i32 %arrayElemValue2)" + endOfLine
                + "%temp6 = sub i32 %temp5, 1" + endOfLine
                + "store i32 %temp6, i32* %numberOfDigits" + endOfLine
                + "%temp7 = icmp sge i32 %temp6, 0" + endOfLine
                + "br i1 %temp7, label %beginloop2, label %endloop2" + endOfLine
                + "endloop2:" + endOfLine
                + "call i32 @putchar(i32 10)" + endOfLine
                + "ret void" + endOfLine
                + "}" + endOfLine
                + endOfLine;

        outputFile.write(res.getBytes(charset));
    }

    public void main() throws IOException {
        String res = "define i32 @main() {" + endOfLine;
        outputFile.write(res.getBytes(charset));
    }

    public void mainEnd() throws IOException {
        String res = "ret i32 0" + endOfLine
                + "}" + endOfLine
                + endOfLine;
        outputFile.write(res.getBytes(charset));
    }
    
    public void number(String number, Type type) {
        pushExpression(number, type);
    }
    
    public void plus() throws IOException, CodeGeneratorException {
        String res = "add ";
        Expression top = topExpression();
        switch (top.type) {
            case integer:
                res += "i32 ";
                break;
            default:
                throw new UnsupportedTypeException(top.type, "plus");
        }
        Expression operand2 = popExpression();
        Expression operand1 = popExpression();
        if (operand1.type != operand2.type) {
            throw new IncompatibleTypeException(operand1.type, operand2.type, "add");
        }
        res += operand1.content + ", " + operand2.content + endOfLine;
        pushExpression(operand1.type);
        res = topExpression().content + " = " + res;
        outputFile.write(res.getBytes(charset));
    }
    
    public void varDeclaration(int varAddress, Type type) throws CodeGeneratorException, IOException {
        String res = varPrefix + varAddress + " = alloca ";
        switch (type) {
            case integer:
                res += "i32";
                break;
            default:
                throw new UnsupportedTypeException(type, "varDeclaration");
        }
        res += endOfLine;
        outputFile.write(res.getBytes(charset));
    }
    
    public void assignation(int varAddress, Type type) throws CodeGeneratorException, IOException {
        String res = "store ";
        String llvmType = "";
        switch(type) {
            case integer:
                llvmType = "i32";
                break;
            default:
                throw new UnsupportedTypeException(type, "assignation");
        }
        Expression exp = expressions.pop();
        res += llvmType + " " + exp.content + ", " + llvmType + "* " + varPrefix + varAddress + endOfLine;
        outputFile.write(res.getBytes(charset));
    }
    
    public void println() throws CodeGeneratorException, IOException {
        String res = "call void @println(";
        Expression exp = expressions.pop();
        switch(exp.type) {
            case integer:
                res += "i32";
                break;
            default:
                throw new UnsupportedTypeException(exp.type, "println");
        }
        res += " " + exp.content + ")" + endOfLine;
        outputFile.write(res.getBytes(charset));
    }
    
    // Charge le contenu d'une variable
    public void valueOfVariable(int varAddress, Type type) throws UnsupportedTypeException, IOException {
        pushExpression(type);
        String res = topExpression().content + " = load ";
        switch(type) {
            case integer:
                res += "i32*";
                break;
            default:
                throw new UnsupportedTypeException(type, "valueOfVariable");
        }
        res += varPrefix + varAddress + endOfLine;
        outputFile.write(res.getBytes(charset));
    }
}
