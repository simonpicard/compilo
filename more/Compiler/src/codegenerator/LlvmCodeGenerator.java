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
import parser.FrameItem;
import parser.FrameVar;
import utils.datastructure.Token;
import utils.datastructure.Variable;

/**
 *
 * @author arnaud
 */
public class LlvmCodeGenerator {

    private FileOutputStream outputFile;
    private static String endOfLine = System.getProperty("line.separator");
    private static Charset charset = Charset.forName("UTF-8");
    private Stack<String> stack = new Stack<String>();
    int id = 0;

    public LlvmCodeGenerator(String outputPath) throws FileNotFoundException {
        outputFile = new FileOutputStream(new File(outputPath));
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
        String res = "  ret i32 0" + endOfLine
                + "}" + endOfLine
                + endOfLine;
        outputFile.write(res.getBytes(charset));
    }
    
    public void generate(String variable, Token token, Object o) throws IOException{
        String res = "";
        FrameVar fv;
        
        switch(variable){
            case "<Type>":
                fv = (FrameVar) o;
                switch(token.getValue()){
                    case "BOOLEAN_TYPE":
                        res = "%v"+Integer.toString(fv.getAddress())+" = allocate i1";
                    case "REAL_TYPE":
                        res = "%v"+Integer.toString(fv.getAddress())+" = allocate float";
                    case "INTEGER_TYPE":
                        res = "%v"+Integer.toString(fv.getAddress())+" = allocate i32";
                }
            case "<AssignationTail>":
                fv = (FrameVar) o;
                switch(token.getValue()){
                    case "ASSIGNATION":
                        stack.add("%v"+Integer.toString(fv.getAddress()));
                }
            case "<UnaryExpression>":
                if (token.getValue() == "NEGATION"){
                    stack.add("NEGATION");
                }
            case "<UnaryBitwiseNotExpression>":
                if (token.getValue() == "BITWISE_NOT"){
                    stack.add("BITWISE_NOT");
                }
            case "<UnaryMinusPlusExpression>":
                if (token.getValue() == "PLUS"){
                    stack.add("PLUS");
                }
                else if (token.getValue() == "MINUS"){
                    stack.add("MINUS");
                }
            case "<AtomicExpression>":
                if (token.getValue() == "INTEGER"){
                    signal(Integer.toString((int) o));
                }
                
        }
        
        outputFile.write(res.getBytes(charset));
        
    }
    
    void signal(String s){
        String res ="";
        res = "%"+Integer.toString(id)+" = load i32 * %a";
        String first = stack.pop();
        while(!stack.empty()){
            switch(stack.pop()){
                case "NEGATION":
                    
            }
        }
    }
}
