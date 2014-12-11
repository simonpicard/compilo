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
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class UselessSymbolRemoval implements GrammarAlgorithm {

    public UselessSymbolRemoval(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public void process() {
        Boolean isGrammarChanged;
        do {
            isGrammarChanged = false;
            Boolean b1 = this.removeUnproductiveSymbols();
            Boolean b2 = this.removeUnreachableSymbols();
            isGrammarChanged = b1 || b2;
        } while (isGrammarChanged);
    }

    // Implements the aglgorithm of unproductive symbol removal.
    // Returns true if the grammar is changed (if some symbols are unproductive).
    private Boolean removeUnproductiveSymbols() {
        Boolean isGrammarChanged = false;

        HashSet<Token> currentProductiveSymbols;
        currentProductiveSymbols = new HashSet<>();

        // g(G) <- T
        for (Terminal terminal : this.grammar.getTerminals()) {
            currentProductiveSymbols.add(terminal);
        }

        // P
        HashMap<Variable, Set<List<Token>>> relations = this.grammar.getRelations();

        // Repeat while it finds new productive symbols
        Boolean isNewProductiveSymbolAdded;
        do {
            isNewProductiveSymbolAdded = false;
            // X
            for (Variable leftPart : relations.keySet()) {
                // a belongs to P
                for (List<Token> rightPart : relations.get(leftPart)) {
                    // a belongs to (g(G))*
                    Boolean isWholeRightPartInCurrentProductiveSymbols = true;
                    for (Token token : rightPart) {
                        if (!currentProductiveSymbols.contains(token)) {
                            isWholeRightPartInCurrentProductiveSymbols = false;
                            break;
                        }
                    }

                    // g(G) U {X}
                    if (isWholeRightPartInCurrentProductiveSymbols && !currentProductiveSymbols.contains(leftPart)) {
                        currentProductiveSymbols.add(leftPart);
                        isNewProductiveSymbolAdded = true;
                        break;
                    }

                }

            }
        } while (isNewProductiveSymbolAdded);

        HashSet<Variable> productiveVariables = Token.keepVariables(currentProductiveSymbols);
        // Suppress unproductive variables
        if (!productiveVariables.equals(this.grammar.getVariables())) {
            isGrammarChanged = true;
            this.grammar.assignNewVariableSet(productiveVariables);
        }

        return isGrammarChanged;
    }

    

    

    private Boolean removeUnreachableSymbols() {
        Boolean isGrammarChanged = false;

        HashSet<Variable> currentReachableVariables = new HashSet<>();
        // r(G) <- S
        currentReachableVariables.add(this.grammar.getStart());

        // P
        HashMap<Variable, Set<List<Token>>> relations = this.grammar.getRelations();

        Boolean isNewReachableVariableFound;
        do {
            isNewReachableVariableFound = false;
            for (Variable leftPart : currentReachableVariables) {

                for (List<Token> rightPart : relations.get(leftPart)) {
                    for (Token token : rightPart) {
                        if (!token.isTerminal() && !currentReachableVariables.contains((Variable) token)) {
                            isNewReachableVariableFound = true;
                            currentReachableVariables.add((Variable) token);
                        }
                    }
                }
            }
        } while (isNewReachableVariableFound);

        // Suppress unreachable variables
        if (!currentReachableVariables.equals(this.grammar.getVariables())) {
            isGrammarChanged = true;
            this.grammar.assignNewVariableSet(currentReachableVariables);
        }

        return isGrammarChanged;
    }

    private Grammar grammar;
}
