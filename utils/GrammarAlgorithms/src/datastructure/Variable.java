/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructure;

/**
 *
 * @author arnaud
 */
public class Variable extends Token {

    public Variable(String value) {
        super(value);
    }

    @Override
    public Boolean isTerminal() {
        return false;
    }
    
    public static Variable getNewVariable() {
        ++newVariableCounter;
        return new Variable("NEW_VAR'{" + newVariableCounter + "}");
    }
    
    public static Variable getNewVariable(Variable variable) {
        ++newVariableCounter;
        return new Variable(variable.getValue() + "'{" + newVariableCounter + "}");
    }
    
    private static int newVariableCounter = 0;
    
    
}
