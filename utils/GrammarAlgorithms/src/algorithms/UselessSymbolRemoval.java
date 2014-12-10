/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import datastructure.Grammar;
import datastructure.Terminal;
import datastructure.Token;
import datastructure.Variable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author arnaud
 */
public class UselessSymbolRemoval {

    public UselessSymbolRemoval(Grammar grammar) {
        this.grammar = grammar;
    }

    public void process() {

    }

    // Implements the aglgorithm of unproductive symbol removal.
    // Returns true if the grammar is changed (if some symbols are unproductive).
    private Boolean removeUnproductiveSymbols() {
        Boolean isGrammarChanged = false;

        HashSet<Token> currentProductiveSymbols;
        currentProductiveSymbols = new HashSet<Token>();

        // g(G) <- T
        for (Terminal terminal : this.grammar.getTerminals()) {
            currentProductiveSymbols.add(terminal);
        }

        // P
        HashMap<Variable, List<List<Token>>> relations = this.grammar.getRelations();
        // X
        for (Variable leftPart : relations.keySet()) {
            // a belongs to P
            for (List<Token> rightPart : relations.get(leftPart)) {
                // a belongs to (g(G))*
                Boolean isWholeRightPartInCurrentProductiveSymbols = true;
                for(Token token : rightPart) {
                    if(!currentProductiveSymbols.contains(token)) {
                        isWholeRightPartInCurrentProductiveSymbols = false;
                        break;
                    }
                }
                
                // g(G) U {X}
                if(isWholeRightPartInCurrentProductiveSymbols) {
                    currentProductiveSymbols.add(leftPart);
                    break;
                }
            }
        }
        
        

        return isGrammarChanged;
    }

    private Grammar grammar;
}
