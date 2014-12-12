/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class Grammar {

    public Grammar(Set<Variable> variables, Set<Terminal> terminals,
            HashMap< Variable, Set<List<Token>>> relations, Variable start) {
        this.variables = variables;
        this.terminals = terminals;
        this.relations = relations;
        this.start = start;
    }
    
    public void addVariableToVariableSet(Variable variable) {
        this.variables.add(variable);
    }
    
    public void addEpsilonToTerminalSet() {
        if (!this.terminals.contains(Epsilon.getInstance())) {
            this.terminals.add(Epsilon.getInstance());
        }
    }
    
    public void assignNewVariableSet(Set<Variable> newVariableSet) {
        if (!newVariableSet.equals(this.getVariables())) {
            HashMap<Variable, Set<List<Token>>> relations = this.getRelations();
            HashMap<Variable, Set<List<Token>>> newRelations = new HashMap<>();
            if (!newVariableSet.contains(this.getStart())) {
                System.out.println("The start symbol is unproductive");
            } else {
                for (Variable leftPart : relations.keySet()) {
                    if (newVariableSet.contains(leftPart)) {
                        Set<List<Token>> newRightParts = new HashSet<>();
                        for (List<Token> rightPart : relations.get(leftPart)) {
                            Boolean isWholeRightPartInProductiveVariables = true;
                            for (Token token : rightPart) {
                                if (!token.isTerminal() && !newVariableSet.contains((Variable) token)) {
                                    isWholeRightPartInProductiveVariables = false;
                                    break;
                                }
                            }
                            if (isWholeRightPartInProductiveVariables) {
                                newRightParts.add(rightPart);
                            }
                        }
                        newRelations.put(leftPart, newRightParts);
                    }
                }
            }
            this.variables = newVariableSet;
            this.relations = newRelations;

            // Check terminal reachability
            relations = this.getRelations();
            Set<Terminal> newTerminals = new HashSet<>();
            
            for (Variable leftPart : relations.keySet()) {
                for (List<Token> rightPart : relations.get(leftPart)) {
                    for (Token token : rightPart) {
                        if(token.isTerminal() && !newTerminals.contains((Terminal) token)) {
                            newTerminals.add((Terminal) token);
                        }
                    }
                }
            }
            
            this.terminals = newTerminals;
        }
    }

    public Set<Variable> getVariables() {
        return variables;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public HashMap<Variable, Set<List<Token>>> getRelations() {
        return relations;
    }

    public Variable getStart() {
        return start;
    }


    private static String relationsToString(HashMap<Variable, Set<List<Token>>> relations) {
        String result = "";
        for(Variable variable : relations.keySet()) {
            result += variable + "->";
            Set<List<Token>> rightPartsForTheSameVariable = relations.get(variable);
            for(List<Token> rightPart : rightPartsForTheSameVariable) {
                for(Token token : rightPart) {
                    result += token + " ";
                }
                // Pop the last character
                result = result.substring(0, result.length()-1);
                result += "|";
            }
            // Pop the last character
            result = result.substring(0, result.length()-1);
            result += "\n";
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "Variables: " + this.variables + "\n"
                + "Terminal: " + this.terminals + "\n"
                + "Start: " + this.start + "\n"
                + "Relations:\n"
                + relationsToString(this.relations);
    }
    
    

    private Set<Variable> variables;
    private Set<Terminal> terminals;
    private HashMap<Variable, Set<List<Token>>> relations;
    private Variable start;
}
