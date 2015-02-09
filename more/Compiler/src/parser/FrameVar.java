/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author Simon
 */
public class FrameVar implements FrameItem{
    private TypeEnum t;
    private static int address = 0;
    private Object val;
    
    public FrameVar(TypeEnum t, Object v){
        this.t = t;
        val = v;
        address++;
    }

    public TypeEnum getType() {
        return t;
    }

    public void setType(TypeEnum t) {
        this.t = t;
    }

    public int getAddress() {
        return address;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }
    
    
}
