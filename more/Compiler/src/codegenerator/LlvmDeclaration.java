/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import java.nio.charset.Charset;
import parser.FrameVar;
import parser.Type;

/**
 *
 * @author Simon
 */
public class LlvmDeclaration implements LlvmItem {
    private FrameVar fv;
    public LlvmDeclaration(FrameVar fv){
        this.fv = fv;
    }

    @Override
    public byte[] toByte() {
        String res;
        res = "%v"+Integer.toString(fv.getAddress())+" = allocate ";
        switch(fv.getType()){
            case integer:
                res = res + "i32";
            case real:
                res = res + "float";
            case bool:
                res = res + "i1";
        }
        res = res + "\n";
        return res.getBytes(Charset.forName("UTF-8"));
    }
    
    
}
