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
public class LeftRecursionRemoval implements GrammarAlgorithm {

    public LeftRecursionRemoval(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public void process() {
        leftRecursionRemoval();
    }

    private void leftRecursionRemoval() {
        HashMap<Variable, List<List<Token>>> relations = this.grammar.getRelations();
        List<Variable> orderedVariables = new ArrayList<>(this.grammar.getVariables());
        

        for (int i = 0; i < orderedVariables.size(); ++i) {
            List<List<Token>> newRightAiParts = new ArrayList<>(relations.get(orderedVariables.get(i)));
            for (int j = 0; j < i; ++j) {
                
                for(List<Token> rightPartAi : relations.get(orderedVariables.get(i))) {
                    // Ai -> Aja
                    if(rightPartAi.get(0).equals(orderedVariables.get(j))) {
                        // Remove Ai -> Aja
                        newRightAiParts.remove(rightPartAi);
                        for(List<Token> rightPartAj : relations.get(orderedVariables.get(j))) {
                            List<Token> newRightPartAi = new ArrayList<>(rightPartAi);
                            // Replace Aj by the value of the relation
                            newRightPartAi.remove(0);
                            newRightPartAi.addAll(0, rightPartAj);
                            
                            newRightAiParts.add(newRightPartAi);
                        }
                    }
                }
                
            }
            relations.remove(orderedVariables.get(i));
            relations.put(orderedVariables.get(i), newRightAiParts);
            directLeftRecursionRemoval(orderedVariables.get(i));
        }
    }

    private void directLeftRecursionRemoval(Variable leftPart) {
        HashMap<Variable, List<List<Token>>> relations = this.grammar.getRelations();

        // A -> Aa1 | ... | Aar
        List<List<Token>> leftRecursiveSet = new ArrayList<>();
        // A -> b1 | ... | bs
        List<List<Token>> notLeftRecursiveSet = new ArrayList<>();
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

            Variable newVariable = Variable.getNewVariable(oldVariable);
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
            this.grammar.addEpsilonToTerminalSet();
            leftRecursiveSet.add(epsilonList);
            relations.put(newVariable, leftRecursiveSet);

        }

    }

    private Grammar grammar;

}
