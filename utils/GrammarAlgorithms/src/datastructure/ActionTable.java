/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructure;

import java.util.HashMap;
import java.util.List;
import algorithms.FirstFollow;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
                if (this.table.get(productionRule.getLeftPart()).get(terminal) != null && !this.table.get(productionRule.getLeftPart()).get(terminal).equals(productionRule)) {
                    throw new Exception("Two production rules for the same M[A,a], the grammar is not LL(1) : A=" + productionRule.getLeftPart() + ", a=" + terminal + ", oldProductionRule=" + this.table.get(productionRule.getLeftPart()).get(terminal) + ", newProductionRule=" + productionRule);
                }
                this.table.get(productionRule.getLeftPart()).put(terminal, productionRule);
            }

            // If epsilon in First(alpha)
            if (firstAlpha.contains(Epsilon.getInstance())) {
                for (Terminal terminal : firstFollow.getFollowK1().get(productionRule.getLeftPart())) {
                    // Two production rules for the same M[A,a], the grammar is not LL(1)
                    if (this.table.get(productionRule.getLeftPart()).get(terminal) != null && !this.table.get(productionRule.getLeftPart()).get(terminal).equals(productionRule)) {
                        throw new Exception("Two production rules for the same M[A,a], the grammar is not LL(1) : A=" + productionRule.getLeftPart() + ", a=" + terminal + ", oldProductionRule=" + this.table.get(productionRule.getLeftPart()).get(terminal) + ", newProductionRule=" + productionRule);
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

    public void writTable(String path) throws IOException {
        BufferedWriter file = new BufferedWriter(new FileWriter(path));
        
        file.write("\\documentclass[a2paper,landscape]{article}\n" +
"\\usepackage[utf8]{inputenc}\n" +
"\\usepackage[top=2.5cm, bottom=2.5cm, left=2.5cm, right=2.5cm]{geometry}\n" +
"\\usepackage[english]{babel}\n" +
"\\usepackage{graphicx}\n" +
"\\usepackage{float}" +
"\n" +
"\n" +
"\\begin{document}");

        List<Variable> orderedVariables = new ArrayList<>(this.grammar.getVariables());
        List<Terminal> orderedTerminals = new ArrayList<>(this.grammar.getTerminals());

        file.write("\\begin{tabular}{|");
        for (Terminal terminal : orderedTerminals) {
            file.write("c|");
        }
        file.write("c|}\n\\hline\n&");

        for (int i = 0; i < orderedTerminals.size()-1; i++) {
            file.write(" \\rotatebox{-90}{"+orderedTerminals.get(i).toString().replace("_", "\\_") + "}&");
        }
        file.write(" \\rotatebox{-90}{"+orderedTerminals.get(orderedTerminals.size() - 1).toString().replace("_", "\\_") + "}\\\\\n\\hline\n");

        for (int i = 0; i < orderedVariables.size(); i++) {
            file.write(orderedVariables.get(i) + "&");
            for (int j = 0; j < orderedTerminals.size() - 1; j++) {
                ProductionRule productionRule = this.table.get(orderedVariables.get(i)).get(orderedTerminals.get(j));
                if (productionRule == null) {
                    file.write("&");
                } else {
                    file.write(productionRule.getId() + "&");
                }
            }
            ProductionRule productionRule = this.table.get(orderedVariables.get(i)).get(orderedTerminals.get(orderedTerminals.size() - 1));
            if (productionRule == null) {
                file.write("\\\\\n\\hline\n");
            } else {
                file.write(productionRule.getId() + "\\\\\n\\hline\n");
            }
        }

        file.write("\\end{tabular}\\end{document} ");
        file.close();
    }

    private Grammar grammar;
    private List<ProductionRule> productionRules;
    private HashMap<Variable, HashMap<Terminal, ProductionRule>> table;
}
