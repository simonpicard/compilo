/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.datastructure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author arnaud
 */
public class Grammar {

    public Grammar(List<Variable> variables, List<Terminal> terminals,
            HashMap< Variable, List<List<Token>>> relations, Variable start) {
        this.variables = variables;
        this.terminals = terminals;
        this.relations = relations;
        this.start = start;
    }
    
    public Grammar(ProductionRule[] productionRules) {
        this.variables = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.start = productionRules[0].getLeftPart();
        
        this.relations = new HashMap<>();
        for(ProductionRule productionRule : productionRules) {
            Variable leftPart = productionRule.getLeftPart();
            List<Token> rightPart = productionRule.getRightPart();
            // Add new variable
            if(!this.variables.contains(leftPart)) {
                this.variables.add(leftPart);
            }
            // Add new terminal
            for(Token token : rightPart) {
                if(token.isTerminal()) {
                    Terminal terminal = (Terminal) token;
                    if(!this.terminals.contains(terminal)) {
                        this.terminals.add(terminal);
                    }
                }
            }
            if(!this.relations.containsKey(leftPart)) {
                this.relations.put(leftPart, new ArrayList<List<Token>>());
            }
            this.relations.get(leftPart).add(rightPart);
        }
    }

    public void addVariableToVariableSet(Variable variable) {
        this.variables.add(variable);
    }

    public void addEpsilonToTerminalSet() {
        if (!this.terminals.contains(Epsilon.getInstance())) {
            this.terminals.add(Epsilon.getInstance());
        }
    }

    public void assignNewVariableSet(List<Variable> newVariableSet) {
        if (!newVariableSet.equals(this.getVariables())) {
            HashMap<Variable, List<List<Token>>> relations = this.getRelations();
            HashMap<Variable, List<List<Token>>> newRelations = new HashMap<>();
            if (!newVariableSet.contains(this.getStart())) {
                System.out.println("The start symbol is unproductive");
            } else {
                for (Variable leftPart : relations.keySet()) {
                    if (newVariableSet.contains(leftPart)) {
                        List<List<Token>> newRightParts = new ArrayList<>();
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
            List<Terminal> newTerminals = new ArrayList<>();

            for (Variable leftPart : relations.keySet()) {
                for (List<Token> rightPart : relations.get(leftPart)) {
                    for (Token token : rightPart) {
                        if (token.isTerminal() && !newTerminals.contains((Terminal) token)) {
                            newTerminals.add((Terminal) token);
                        }
                    }
                }
            }

            this.terminals = newTerminals;
        }
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    public HashMap<Variable, List<List<Token>>> getRelations() {
        return relations;
    }

    public Variable getStart() {
        return start;
    }

    private String relationsToString() {
        HashMap<Variable, List<List<Token>>> relations = this.getRelations();
        String result = "";
        for (Variable variable : this.variables) {
            result += variable + "->";
            List<List<Token>> rightPartsForTheSameVariable = relations.get(variable);
            for (List<Token> rightPart : rightPartsForTheSameVariable) {
                for (Token token : rightPart) {
                    result += token + " ";
                }
                // Pop the last character
                result = result.substring(0, result.length() - 1);
                result += "|";
            }
            // Pop the last character
            result = result.substring(0, result.length() - 1);
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
                + relationsToString();
    }

    public void writeLatexTable(String filename) throws IOException {
        BufferedWriter file = new BufferedWriter(new FileWriter(filename));

        file.write("\\begin{longtable}{llll}\n");

        final int maxInCell = 40;
        int productionCount = 1;
        for (Variable leftPart : this.getVariables()) {
            file.write("$[" + productionCount + "]$&" + leftPart.toLatex() + "&$\\rightarrow$&\\begin{tabular}[t]{@{}l@{}}");
            ++productionCount;
            int currentInCell = maxInCell;
            for (Token token : this.relations.get(leftPart).get(0)) {
                currentInCell -= token.toString().length();
                if (currentInCell < 0) {
                    file.write("\\\\" + token.toLatex() + " ");
                    currentInCell = maxInCell - token.toString().length();
                } else {
                    file.write(token.toLatex() + " ");
                }
            }
            file.write("\\end{tabular}\\\\\n");
            for (int i = 1; i < this.relations.get(leftPart).size(); ++i) {
                file.write("$[" + productionCount + "]$&" + "&$\\rightarrow$&\\begin{tabular}[t]{@{}l@{}}");
                ++productionCount;
                currentInCell = maxInCell;
                for (Token token : this.relations.get(leftPart).get(i)) {
                    currentInCell -= token.toString().length();
                    if (currentInCell < 0) {
                        file.write("\\\\" + token.toLatex() + " ");
                        currentInCell = maxInCell - token.toString().length();
                    } else {
                        file.write(token.toLatex() + " ");
                    }
                }
                file.write("\\end{tabular}\\\\\n");
            }
        }

        file.write("\\end{longtable}");

        file.flush();
        file.close();
    }

    private List<Variable> variables;
    private List<Terminal> terminals;
    private HashMap<Variable, List<List<Token>>> relations;
    private Variable start;
}
