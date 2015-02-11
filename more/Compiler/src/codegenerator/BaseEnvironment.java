/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import java.nio.charset.Charset;

/**
 *
 * @author arnaud
 */
public class BaseEnvironment implements LlvmItem {

    @Override
    public byte[] toByte() {
        String res = "";

        // import functions from stdlib
        res += "declare i32 @getchar" + LlvmCodeGenerator.endOfline
                + "declare i32 @putchar(i32)" + LlvmCodeGenerator.endOfline
                + LlvmCodeGenerator.endOfline;

        res += "define i32 @readInt() {" + LlvmCodeGenerator.endOfline
                + "entry:" + LlvmCodeGenerator.endOfline
                + "%res = alloca i32" + LlvmCodeGenerator.endOfline
                + "%digit = alloca i32" + LlvmCodeGenerator.endOfline
                + "store i32 0, i32* %res" + LlvmCodeGenerator.endOfline
                + "br label %read" + LlvmCodeGenerator.endOfline
                + "read:" + LlvmCodeGenerator.endOfline
                + "%0 = call i32 @getchar()" + LlvmCodeGenerator.endOfline
                + "%1 = sub i32 %0, 48" + LlvmCodeGenerator.endOfline
                + "store i32 %1, i32* %digit" + LlvmCodeGenerator.endOfline
                + "%2 = icmp ne i32 %0, 10" + LlvmCodeGenerator.endOfline
                + "br i1 %2, label %save , label %exit" + LlvmCodeGenerator.endOfline
                + "save:" + LlvmCodeGenerator.endOfline
                + "%3 = load i32* %res" + LlvmCodeGenerator.endOfline
                + "%4 = load i32* %digit" + LlvmCodeGenerator.endOfline
                + "%5 = mul i32 %3, 10" + LlvmCodeGenerator.endOfline
                + "%6 = add i32 %5, %4" + LlvmCodeGenerator.endOfline
                + "store i32 %6, i32* %res" + LlvmCodeGenerator.endOfline
                + "br label %read" + LlvmCodeGenerator.endOfline
                + "exit:" + LlvmCodeGenerator.endOfline
                + "%7 = load i32* %res" + LlvmCodeGenerator.endOfline
                + "ret i32 %7" + LlvmCodeGenerator.endOfline
                + "}" + LlvmCodeGenerator.endOfline
                + LlvmCodeGenerator.endOfline;

        res += "define void @println(i32 %n) {" + LlvmCodeGenerator.endOfline
                + "%digitInChar = alloca [32 x i32]" + LlvmCodeGenerator.endOfline
                + "%number = alloca i32" + LlvmCodeGenerator.endOfline
                + "store i32 %n, i32* %number" + LlvmCodeGenerator.endOfline
                + "%numberOfDigits = alloca i32" + LlvmCodeGenerator.endOfline
                + "store i32 0, i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "br label %beginloop" + LlvmCodeGenerator.endOfline
                + "beginloop:" + LlvmCodeGenerator.endOfline
                + "%1 = load i32* %number" + LlvmCodeGenerator.endOfline
                + "%2 = icmp ne i32 %1, 0" + LlvmCodeGenerator.endOfline
                + "br i1 %2, label %ifloop, label %endloop" + LlvmCodeGenerator.endOfline
                + "ifloop:" + LlvmCodeGenerator.endOfline
                + "%temp1 = load i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "%temp2 = load i32* %number" + LlvmCodeGenerator.endOfline
                + "%divnum = udiv i32 %temp2, 10" + LlvmCodeGenerator.endOfline
                + "%currentDigit = urem i32 %temp2, 10" + LlvmCodeGenerator.endOfline
                + "%arrayElem = getelementptr [32 x i32]* %digitInChar, i32 0, i32 %temp1" + LlvmCodeGenerator.endOfline
                + "store i32 %currentDigit, i32* %arrayElem" + LlvmCodeGenerator.endOfline
                + "%temp3 = add i32 %temp1, 1" + LlvmCodeGenerator.endOfline
                + "store i32 %temp3, i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "store i32 %divnum, i32* %number" + LlvmCodeGenerator.endOfline
                + "br label %beginloop" + LlvmCodeGenerator.endOfline
                + "endloop:" + LlvmCodeGenerator.endOfline
                + "%temp4 = load i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "%temp41 = sub i32 %temp4, 1" + LlvmCodeGenerator.endOfline
                + "store i32 %temp41, i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "br label %beginloop2" + LlvmCodeGenerator.endOfline
                + "beginloop2:" + LlvmCodeGenerator.endOfline
                + "%temp5 = load i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "%arrayElem2 = getelementptr [32 x i32]* %digitInChar, i32 0, i32 %temp5" + LlvmCodeGenerator.endOfline
                + "%arrayElemValue = load i32* %arrayElem2" + LlvmCodeGenerator.endOfline
                + "%arrayElemValue2 = add i32 %arrayElemValue, 48" + LlvmCodeGenerator.endOfline
                + "call i32 @putchar(i32 %arrayElemValue2)" + LlvmCodeGenerator.endOfline
                + "%temp6 = sub i32 %temp5, 1" + LlvmCodeGenerator.endOfline
                + "store i32 %temp6, i32* %numberOfDigits" + LlvmCodeGenerator.endOfline
                + "%temp7 = icmp sge i32 %temp6, 0" + LlvmCodeGenerator.endOfline
                + "br i1 %temp7, label %beginloop2, label %endloop2" + LlvmCodeGenerator.endOfline
                + "endloop2:" + LlvmCodeGenerator.endOfline
                + "call i32 @putchar(i32 10)" + LlvmCodeGenerator.endOfline
                + "ret void" + LlvmCodeGenerator.endOfline
                + "}" + LlvmCodeGenerator.endOfline
                + LlvmCodeGenerator.endOfline;
        return res.getBytes(Charset.forName("UTF-8"));
    }

}
