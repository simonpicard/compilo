/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.datastructure;

/**
 *
 * @author arnaud
 */
public class Terminal extends Token {
    public Terminal(String value) {
        super(value);
    }

    @Override
    public Boolean isTerminal() {
        return true;
    }
    
}
