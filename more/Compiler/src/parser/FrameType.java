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
public class FrameType implements FrameItem{
    private String typeLLVM;
    private Type id;
    
    public FrameType(String t, Type i){
        typeLLVM = t;
        id = i;
    }

    public String getTypeLLVM() {
        return typeLLVM;
    }

    public void setTypeLLVM(String typeLLVM) {
        this.typeLLVM = typeLLVM;
    }

    public Type getId() {
        return id;
    }

    public void setId(Type id) {
        this.id = id;
    }
    
    
    
}
