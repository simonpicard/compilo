/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.AST;

import parser.Type;

/**
 *
 * @author arnaud
 */
public class VariableASTNode implements ASTNode {
    private int address;
    private Type type;
    
    public VariableASTNode(int adress, Type type) {
        this.address = adress;
        this.type = type;
    }

    @Override
    public void addChild(ASTNode child) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "Variable node : " + address + ", " + type;
    }
}
