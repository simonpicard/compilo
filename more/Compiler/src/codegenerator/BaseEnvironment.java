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
        res += "define i32 @readInt() {"+LlvmCodeGenerator.endOfline
                + "entry:"+LlvmCodeGenerator.endOfline
                + "%res = alloca i32"+LlvmCodeGenerator.endOfline
                + "%digit = alloca i32"+LlvmCodeGenerator.endOfline
                + "store i32 0, i32* %res"+LlvmCodeGenerator.endOfline
                + "br label %read"+LlvmCodeGenerator.endOfline
                + "read:"+LlvmCodeGenerator.endOfline
                + "%0 = call i32 @getchar()"+LlvmCodeGenerator.endOfline
                + "%1 = sub i32 %0, 48"+LlvmCodeGenerator.endOfline
                + "store i32 %1, i32* %digit"+LlvmCodeGenerator.endOfline
                + "%2 = icmp ne i32 %0, 10"+LlvmCodeGenerator.endOfline
                + "br i1 %2, label %save , label %exit"+LlvmCodeGenerator.endOfline
                + "save:"+LlvmCodeGenerator.endOfline
                + "%3 = load i32* %res"+LlvmCodeGenerator.endOfline
                + "%4 = load i32* %digit"+LlvmCodeGenerator.endOfline
                + "%5 = mul i32 %3, 10"+LlvmCodeGenerator.endOfline
                + "%6 = add i32 %5, %4"+LlvmCodeGenerator.endOfline
                + "store i32 %6, i32* %res"+LlvmCodeGenerator.endOfline
                + "br label %read"+LlvmCodeGenerator.endOfline
                + "exit:"+LlvmCodeGenerator.endOfline
                + "%7 = load i32* %res"+LlvmCodeGenerator.endOfline
                + "ret i32 %7"+LlvmCodeGenerator.endOfline
                + "}"+LlvmCodeGenerator.endOfline;
        return res.getBytes(Charset.forName("UTF-8"));
    }

}
