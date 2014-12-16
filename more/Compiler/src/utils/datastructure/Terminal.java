/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.datastructure;

import scanner.LexicalUnit;
import scanner.Symbol;

/**
 *
 * @author arnaud
 */
public class Terminal extends Token {
    public Terminal(String value) {
        super(value);
    }
    
    public static Terminal castToTerminal(Symbol symbol) {
        return LexicalUnit.END_OF_STREAM.equals(symbol.getType()) ? Epsilon.getInstance() : new Terminal(symbol.getType().name());
    }

    @Override
    public Boolean isTerminal() {
        return true;
    }
    
}
