/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructure;

import java.util.HashMap;
import java.util.List;
import algorithms.FirstFollow;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class ActionTable {

    public ActionTable(Grammar grammar) throws Exception {
        this.grammar = grammar;
        this.productionRules = ProductionRule.generateProductionRulesFromGrammar(this.grammar);
        table = new HashMap<>();
        this.generateActionTable();
    }

    private void generateActionTable() throws Exception {

        // Initialisation
        for (Variable variable : this.grammar.getVariables()) {
            this.table.put(variable, new HashMap<Terminal, ProductionRule>());
            for (Terminal terminal : this.grammar.getTerminals()) {
                this.table.get(variable).put(terminal, null);
            }
        }

        FirstFollow firstFollow = new FirstFollow(this.grammar);
        firstFollow.process();

        // For all A->alpha in P
        for (ProductionRule productionRule : this.productionRules) {
            //For all terminal in First(alpha)
            Set<Terminal> firstAlpha = firstFollow.firstSet(productionRule.getRightPart());
            for (Terminal terminal : firstAlpha) {
                // Two production rules for the same M[A,a], the grammar is not LL(1)
                if (this.table.get(productionRule.getLeftPart()).get(terminal) != null) {
                    throw new Exception("Two production rules for the same M[A,a], the grammar is not LL(1)");
                }
                this.table.get(productionRule.getLeftPart()).put(terminal, productionRule);
            }

            // If epsilon in First(alpha)
            if (firstAlpha.contains(Epsilon.getInstance())) {
                for (Terminal terminal : firstFollow.getFollowK1().get(productionRule.getLeftPart())) {
                    // Two production rules for the same M[A,a], the grammar is not LL(1)
                    if (this.table.get(productionRule.getLeftPart()).get(terminal) != null) {
                        throw new Exception("Two production rules for the same M[A,a], the grammar is not LL(1)");
                    }
                    this.table.get(productionRule.getLeftPart()).put(terminal, productionRule);
                }
            }

        }
    }

    public HashMap<Variable, HashMap<Terminal, ProductionRule>> getTable() {
        return this.table;
    }

    @Override
    public String toString() {
        String result = "";
        for (ProductionRule productionRule : this.productionRules) {
            result += productionRule.toString() + "\n";
        }

        List<Variable> orderedVariables = new ArrayList<>(this.grammar.getVariables());
        List<Terminal> orderedTerminals = new ArrayList<>(this.grammar.getTerminals());

        result += "  ";
        for (Terminal terminal : orderedTerminals) {
            result += terminal.toString() + " ";
        }

        result += "\n";

        for (Variable variable : orderedVariables) {
            result += variable.toString() + " ";
            for (Terminal terminal : orderedTerminals) {
                ProductionRule productionRule = this.table.get(variable).get(terminal);
                if (productionRule == null) {
                    result += "null" + " ";
                } else {
                    result += productionRule.getId() + " ";
                }
            }
            result += "\n";
        }
        return result;
    }

    private Grammar grammar;
    private List<ProductionRule> productionRules;
    private HashMap<Variable, HashMap<Terminal, ProductionRule>> table;
}
