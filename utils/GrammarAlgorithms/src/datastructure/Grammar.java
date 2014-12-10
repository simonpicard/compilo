/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class Grammar {

    public Grammar(Set<Variable> variables, Set<Terminal> terminals,
            HashMap< Variable, List<List<Token>>> relations, Variable start) {
        this.variables = variables;
        this.terminals = terminals;
        this.relations = relations;
        this.start = start;
    }

    public Set<Variable> getVariables() {
        return variables;
    }

    public void setVariables(Set<Variable> variables) {
        this.variables = variables;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<Terminal> terminals) {
        this.terminals = terminals;
    }

    public HashMap<Variable, List<List<Token>>> getRelations() {
        return relations;
    }

    public void setRelations(HashMap<Variable, List<List<Token>>> relations) {
        this.relations = relations;
    }

    public Variable getStart() {
        return start;
    }

    public void setStart(Variable start) {
        this.start = start;
    }

    public static String relationsToString(HashMap<Variable, List<List<Token>>> relations) {
        String result = "";
        for(Variable variable : relations.keySet()) {
            result += variable + "->";
            List<List<Token>> rightPartsForTheSameVariable = relations.get(variable);
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
    private HashMap<Variable, List<List<Token>>> relations;
    private Variable start;
}
