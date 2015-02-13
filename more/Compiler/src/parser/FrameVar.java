/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 * Variable in a Frame.
 * @author Simon
 */
public class FrameVar implements FrameItem {

    private Type t;
    private int address;
    private static int addressCounter = 0;

    public FrameVar(Type t) {
        this.t = t;
        this.address = addressCounter;
        ++addressCounter;
    }

    public Type getType() {
        return t;
    }

    public void setType(Type t) {
        this.t = t;
    }

    public int getAddress() {
        return address;
    }
    
    @Override
    public String toString() {
        String res = "type(variable), address(" + address + "), variableType(" + t + ")";
        return res;
    }

}
