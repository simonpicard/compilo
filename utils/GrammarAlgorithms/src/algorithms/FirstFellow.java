/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import datastructure.Grammar;
import datastructure.Terminal;
import datastructure.Token;
import datastructure.Epsilon;
import datastructure.Variable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class FirstFellow implements GrammarAlgorithm {

    public FirstFellow(Grammar grammar) {
        this.grammar = grammar;
        this.allFirstK1 = new HashMap<>();
    }

    @Override
    public void process() throws Exception {
        // For all a that belongs to T : First(a) = {a}
        for (Terminal terminal : this.grammar.getTerminals()) {
            this.allFirstK1.put(terminal, new HashSet<Terminal>());
            this.allFirstK1.get(terminal).add(terminal);
        }
        
        // For all A that belongs to V : First(A) = {}
        for (Variable variable : this.grammar.getVariables()) {
            this.allFirstK1.put(variable, new HashSet<Terminal>());
        }
        
        
        Boolean isChangeDetected = false;
        HashMap<Variable, Set<List<Token>>> relations = this.grammar.getRelations();
        do {
            isChangeDetected = false;
            for(Variable leftPart : relations.keySet()) {
                for(List<Token> rightPart : relations.get(leftPart)) {
                    Set<Terminal> k1Sum = new HashSet<>();
                    k1Sum.addAll(this.allFirstK1.get(rightPart.get(0)));
                    for(int i=1; i<rightPart.size(); ++i) {
                        k1Sum.addAll(this.addK1(k1Sum, this.allFirstK1.get(rightPart.get(i))));
                    }
                    for (Terminal terminal : k1Sum) {
                        if (!this.allFirstK1.get(leftPart).contains(terminal)) {
                            this.allFirstK1.get(leftPart).add(terminal);
                            isChangeDetected = true;
                        }
                    }
                }
            }
            
        }while(isChangeDetected);

    }
    
    

    private Set<Terminal> addK1(Set<Terminal> l1, Set<Terminal> l2) {
        Set<Terminal> sumTerminalSetK1 = new HashSet<>();
        for (Terminal t1 : l1) {
            for (Terminal t2 : l2) {
                sumTerminalSetK1.add(addTerminalK1(t1, t2));
            }
        }
        return sumTerminalSetK1;
    }

    private Terminal addTerminalK1(Terminal t1, Terminal t2) {
        Terminal sumK1Terminal = null;
        if (t1.equals(Epsilon.getInstance())) {
            sumK1Terminal = t2;
        } else {
            sumK1Terminal = t1;
        }
        return sumK1Terminal;
    }
    
    public HashMap<Token, Set<Terminal>> getFirstK1() {
        return allFirstK1;
    }

    private Grammar grammar;
    
    private HashMap<Token, Set<Terminal>> allFirstK1 = null;

}
