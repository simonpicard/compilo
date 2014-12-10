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
    
}
