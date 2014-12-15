/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.algorithms;

import utils.datastructure.Grammar;
import utils.datastructure.Terminal;
import utils.datastructure.Token;
import utils.datastructure.Epsilon;
import utils.datastructure.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author arnaud
 */
public class FirstFollow implements GrammarAlgorithm {

    public FirstFollow(Grammar grammar) {
        this.grammar = grammar;
        this.allFirstK1 = new HashMap<>();
        this.allFollowK1 = new HashMap<>();
    }

    @Override
    public void process() throws Exception {
        this.generateFirstSet();
        this.generateFollowSet();
    }

    public Set<Terminal> firstSet(List<Token> tokenList) {
        Set<Terminal> k1Sum = new HashSet<>();
        if (!tokenList.isEmpty()) {
            k1Sum.addAll(this.allFirstK1.get(tokenList.get(0)));
            for (int i = 1; i < tokenList.size(); ++i) {
                k1Sum.addAll(this.addK1(k1Sum, this.allFirstK1.get(tokenList.get(i))));
            }
        }
        return k1Sum;
    }

    private Boolean containsEmptyFirstSet(List<Token> tokenList) {
        Boolean result = false;
        for (Token token : tokenList) {
            if (this.allFirstK1.get(token).isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void generateFirstSet() {
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
        HashMap<Variable, List<List<Token>>> relations = this.grammar.getRelations();
        do {
            isChangeDetected = false;
            for (Variable leftPart : relations.keySet()) {
                for (List<Token> rightPart : relations.get(leftPart)) {
                    if (!containsEmptyFirstSet(rightPart)) {
                        Set<Terminal> k1Sum = firstSet(rightPart);
                        for (Terminal terminal : k1Sum) {
                            if (!this.allFirstK1.get(leftPart).contains(terminal)) {
                                this.allFirstK1.get(leftPart).add(terminal);
                                isChangeDetected = true;
                            }
                        }
                    }
                }
            }

        } while (isChangeDetected);
    }

    private void generateFollowSet() {
        // For all A that belongs to V : Follow(A) = {}
        for (Variable variable : this.grammar.getVariables()) {
            this.allFollowK1.put(variable, new HashSet<Terminal>());
        }

        Boolean isChangeDetected = false;
        HashMap<Variable, List<List<Token>>> relations = this.grammar.getRelations();
        do {
            isChangeDetected = false;
            for (Variable leftPart : relations.keySet()) {
                for (List<Token> rightPart : relations.get(leftPart)) {
                    for (int indexOfB = 0; indexOfB < rightPart.size(); ++indexOfB) {
                        // B has to be a variable
                        if (!rightPart.get(indexOfB).isTerminal()) {

                            List<Token> beta = new ArrayList<>(rightPart.subList(indexOfB + 1, rightPart.size()));

                            // First(beta)
                            Set<Terminal> firstSetBeta = firstSet(beta);
                            if (rightPart.get(indexOfB).equals(new Variable("<expr tail>"))) {
                                System.out.println("FirstSetBeta : " + firstSetBeta);
                            }

                            // Follow(A)
                            Set<Terminal> followSetLeftPart = this.allFollowK1.get(leftPart);

                            // First(beta) + Follow(A)
                            Set<Terminal> resultSet = this.addK1(firstSetBeta, followSetLeftPart);
                            if (resultSet.contains(Epsilon.getInstance())) {
                                resultSet.remove(Epsilon.getInstance());
                            }
                            if (rightPart.get(indexOfB).equals(new Variable("<expr tail>"))) {
                                System.out.println("ResultSet : " + resultSet);
                            }

                            for (Terminal terminal : resultSet) {
                                if (!this.allFollowK1.get(rightPart.get(indexOfB)).contains(terminal)) {
                                    this.allFollowK1.get(rightPart.get(indexOfB)).add(terminal);
                                    isChangeDetected = true;
                                }
                            }
                        }
                    }
                }
            }
        } while (isChangeDetected);
    }

    private Set<Terminal> addK1(Set<Terminal> l1, Set<Terminal> l2) {
        Set<Terminal> sumTerminalSetK1 = new HashSet<>();
        if (l1.isEmpty() && l2.isEmpty()) {

        } else if (l2.isEmpty()) {
            sumTerminalSetK1.addAll(l1);
        } else if (l1.isEmpty()) {
            sumTerminalSetK1.addAll(l2);
        } else {
            for (Terminal t1 : l1) {
                for (Terminal t2 : l2) {
                    sumTerminalSetK1.add(addTerminalK1(t1, t2));
                }

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

    public HashMap<Token, Set<Terminal>> getFollowK1() {
        return allFollowK1;
    }

    private Grammar grammar;

    private HashMap<Token, Set<Terminal>> allFirstK1 = null;
    private HashMap<Token, Set<Terminal>> allFollowK1 = null;

}
