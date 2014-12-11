/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import datastructure.Grammar;
import datastructure.Token;
import datastructure.Variable;
import datastructure.Epsilon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class LeftRecursionRemoval {

    public LeftRecursionRemoval(Grammar grammar) {
        this.grammar = grammar;
    }

    public void process() {

    }

    private void directLeftRecursionRemoval(Variable leftPart) {
        HashMap<Variable, Set<List<Token>>> relations = this.grammar.getRelations();
        
        
        // A -> Aa1 | ... | Aar
        Set<List<Token>> leftRecursiveSet = new HashSet<>();
        // A -> b1 | ... | bs
        Set<List<Token>> notLeftRecursiveSet = new HashSet<>();
        for (List<Token> rightPart : relations.get(leftPart)) {
            if (rightPart.get(0).equals(leftPart)) {
                leftRecursiveSet.add(rightPart);
            } else {
                notLeftRecursiveSet.add(rightPart);
            }
        }

            // If there exists a left recursion for this variable, 
        // remove all the rules for this variable and replace them 
        // by new rules without left recursion.
        if (!leftRecursiveSet.isEmpty()) {
            relations.remove(leftPart);
            Variable oldVariable = leftPart;

            Variable newVariable = new Variable(oldVariable.getValue() + "'");
            this.grammar.addVariableToVariableSet(newVariable);

            // A -> b1A' | ... | bsA'
            for (List<Token> rightPart : notLeftRecursiveSet) {
                rightPart.add(newVariable);
            }
            relations.put(oldVariable, notLeftRecursiveSet);

            // A' -> a1A' | ... | arA' | e
            for (List<Token> rightPart : leftRecursiveSet) {
                // Romove left recursion
                rightPart.remove(0);
                rightPart.add(newVariable);
            }
            // Add epsilon
            List<Token> epsilonList = new ArrayList<>();
            epsilonList.add(Epsilon.getInstance());
            leftRecursiveSet.add(epsilonList);
            relations.put(newVariable, leftRecursiveSet);

        }

    }

    private Grammar grammar;

}
