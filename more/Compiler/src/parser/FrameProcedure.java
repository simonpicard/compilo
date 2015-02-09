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
    private TypeEnum resultType;
    private ArrayList<TypeEnum> argsType;
    private Object procedure;
    
    public FrameProcedure(TypeEnum t, ArrayList<TypeEnum> aT, Object p){
        resultType = t;
        argsType = aT;
        procedure = p;
    }

    public TypeEnum getResultType() {
        return resultType;
    }

    public void setResultType(TypeEnum resultType) {
        this.resultType = resultType;
    }

    public ArrayList<TypeEnum> getArgsType() {
        return argsType;
    }

    public void setArgsType(ArrayList<TypeEnum> argsType) {
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
