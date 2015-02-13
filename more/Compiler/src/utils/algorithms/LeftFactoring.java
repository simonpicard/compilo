/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.algorithms;

import utils.datastructure.Epsilon;
import utils.datastructure.Grammar;
import utils.datastructure.Token;
import utils.datastructure.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author arnaud
 */
public class LeftFactoring implements GrammarAlgorithm {

    public LeftFactoring(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public void process() throws Exception {
        this.leftFactoring();
    }

    private void leftFactoring() throws Exception {
        HashMap<Variable, List<List<Token>>> relations = this.grammar.getRelations();
        List<Variable> orderedLeftParts = new ArrayList<>(relations.keySet());

        int leftPartIndex = 0;
        while (leftPartIndex < orderedLeftParts.size()) {
            Boolean isCommonPrefixFound = false;
            Variable leftPart = orderedLeftParts.get(leftPartIndex);
            List<List<Token>> orderedRightParts = new ArrayList<>(relations.get(leftPart));

            int i = 0;
            while (i < orderedRightParts.size()) {
                List<Integer> indexesOfMatches = new ArrayList<>();
                int sizeOfSubstring = orderedRightParts.get(i).size();
                while (sizeOfSubstring > 0) {
                    for (int j = 0; j < orderedRightParts.size(); ++j) {
                        // Match
                        if (j != i && orderedRightParts.get(j).size() >= sizeOfSubstring
                                && orderedRightParts.get(i).subList(0, sizeOfSubstring)
                                .equals(orderedRightParts.get(j).subList(0, sizeOfSubstring))) {

                            indexesOfMatches.add(j);
                        }
                    }
                    // If a match is found
                    if (!indexesOfMatches.isEmpty()) {
                        isCommonPrefixFound = true;
                        break;
                    }
                    --sizeOfSubstring;
                }
                

                // If a match is found
                if (isCommonPrefixFound) {
                    // Create new variable
                    Variable newVariable = Variable.getNewVariable();
                    this.grammar.addVariableToVariableSet(newVariable);
                    relations.put(newVariable, new ArrayList<List<Token>>());

                    // A' -> b1 | ... | bn
                    relations.get(leftPart).remove(orderedRightParts.get(i));
                    List<Token> sublist = orderedRightParts.get(i).subList(sizeOfSubstring, orderedRightParts.get(i).size());
                    
                    if (sublist.isEmpty()) {
                        sublist.add(Epsilon.getInstance());
                        this.grammar.addEpsilonToTerminalSet();
                    }
                    relations.get(newVariable).add(sublist);

                    for (Integer index : indexesOfMatches) {
                        relations.get(leftPart).remove(orderedRightParts.get(index));
                        sublist = orderedRightParts.get(index).subList(sizeOfSubstring, orderedRightParts.get(index).size());
                        if (sublist.isEmpty()) {
                            sublist.add(Epsilon.getInstance());
                            this.grammar.addEpsilonToTerminalSet();;
                        }
                        relations.get(newVariable).add(sublist);
                    }
                    
                    
                    // A -> aA'
                    sublist = new ArrayList<>(orderedRightParts.get(i).subList(0, sizeOfSubstring));
                    if (sublist.isEmpty()) {
                        throw new Exception("Error : common prefix (a) is empty");
                    }
                    sublist.add(newVariable);
                    relations.get(leftPart).add(sublist);
                    break;
                }

                
                ++i;
                
            }
            if(!isCommonPrefixFound) {
                ++leftPartIndex;
            }
            else {
                leftPartIndex = 0;
                orderedLeftParts = new ArrayList<>(relations.keySet());
            }
            
        }
    }

    private Grammar grammar;

}
