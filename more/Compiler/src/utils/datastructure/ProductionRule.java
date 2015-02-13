/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author arnaud
 */
public class ProductionRule {
    
    private ProductionRule(Variable leftPart, List<Token> rightPart) {
        this.id = counter;
        ++counter;
        this.leftPart = leftPart;
        this.rightPart = rightPart;
        productionRules.put(id, this);
    }
    
    public ProductionRule(int id, Variable leftPart, Token... rightPart) {
        this.id = id;
        this.leftPart = leftPart;
        this.rightPart = Arrays.asList(rightPart);
    }
    
    public static List<ProductionRule> generateProductionRulesFromGrammar(Grammar grammar) {
        List<ProductionRule> result = new ArrayList<>();
        
        HashMap<Variable, List<List<Token>>> relations = grammar.getRelations();
        
        for (Variable leftPart : grammar.getVariables()) {
            for (List<Token> rightPart : relations.get(leftPart)) {
                result.add(new ProductionRule(leftPart, rightPart));
            }
        }
        
        return result;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Variable getLeftPart() {
        return this.leftPart;
    }
    
    public List<Token> getRightPart() {
        return this.rightPart;
    }

    @Override
    public String toString() {
        String result = "";
        result += "(" + this.id + ")" + this.leftPart.toString() + "->";
        for (Token token : this.rightPart) {
            result += token.toString() + " ";
        }
        result = result.substring(0, result.length()-1);
        return result;
    }
    
    
    
    private static int counter = 1;
    public static HashMap<Integer, ProductionRule> productionRules = new HashMap<>();
    private int id;
    private Variable leftPart;
    private List<Token> rightPart;
}
