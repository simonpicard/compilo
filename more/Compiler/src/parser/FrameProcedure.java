/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;

/**
 *
 * @author Simon
 */
public class FrameProcedure implements FrameItem {
    private Type resultType;
    private ArrayList<Type> argsType;
    private Object procedure;
    
    public FrameProcedure(Type t, ArrayList<Type> aT, Object p){
        resultType = t;
        argsType = aT;
        procedure = p;
    }

    public Type getResultType() {
        return resultType;
    }

    public void setResultType(Type resultType) {
        this.resultType = resultType;
    }

    public ArrayList<Type> getArgsType() {
        return argsType;
    }

    public void setArgsType(ArrayList<Type> argsType) {
        this.argsType = argsType;
    }

    public Object getProcedure() {
        return procedure;
    }

    public void setProcedure(Object procedure) {
        this.procedure = procedure;
    }
    
    public int getNbArgs(){
        return argsType.size();
    }
}
