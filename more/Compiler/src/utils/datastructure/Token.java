/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author arnaud
 */
public abstract class Token {

    public Token(String value) {
        this.value = value;
    }

    public abstract Boolean isTerminal();
    
    public static List<Variable> keepVariables(List<Token> set) {
        List<Variable> variables = new ArrayList<>();
        for (Token token : set) {
            if (!token.isTerminal()) {
                variables.add((Variable) token);
            }
        }
        return variables;
    }
    
    public String toLatex() {
        String res = "";
        for(int i=0; i<this.getValue().length(); ++i) {
            char aChar = this.getValue().charAt(i);
            switch(aChar) {
                case '_':
                    res += "\\_";
                    break;
                default:
                    res += aChar;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return value;
    }

    private final String value;
}
