/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    }
    
    public static List<ProductionRule> generateProductionRulesFromGrammar(Grammar grammar) {
        List<ProductionRule> result = new ArrayList<>();
        
        HashMap<Variable, Set<List<Token>>> relations = grammar.getRelations();
        
        for (Variable leftPart : relations.keySet()) {
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
    
    private static int counter = 0;
    private int id;
    private Variable leftPart;
    private List<Token> rightPart;
}
