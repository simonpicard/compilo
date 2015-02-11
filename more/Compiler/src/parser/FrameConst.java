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
public class FrameConst implements FrameItem{
    
    private Type t;
    private Object val;
    private static int address = 0;
    
    public FrameConst(Type t, Object v){
        this.t = t;
        val = v;
        address++;
    }

    public Type getT() {
        return t;
    }

    public void setT(Type t) {
        this.t = t;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public static int getAddress() {
        return address;
    }
    
}
