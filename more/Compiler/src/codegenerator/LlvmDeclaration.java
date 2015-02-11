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
        res = "%"+Integer.toString(fv.getAddress())+" = allocate ";
        switch(fv.getType()){
            case(Type.integer):
                res = res + "i32";
            case(Type.real):
                res = res + "float";
            case(Type.bool):
                res = res + "bool";
        }
        res = res + "\n";
        return res.getBytes(Charset.forName("UTF-8"));
    }
    
    
}
