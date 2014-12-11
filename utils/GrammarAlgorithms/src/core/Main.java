package core;

import algorithms.LeftRecursionRemoval;
import algorithms.UselessSymbolRemoval;
import algorithms.GrammarAlgorithm;
import datastructure.GrammarParser;
import datastructure.Grammar;


/**
 *
 * @author arnaud
 */
public class Main {

    public static void main(String args[]) {
        GrammarParser grammarParser = new GrammarParser("/home/arnaud/Documents/ulb/ma1-2014-2015/compiler/projet/compilo/utils/test.grammar");
        Grammar grammar = grammarParser.generateGrammar();
        System.out.println(grammar);
        GrammarAlgorithm algorithm = new LeftRecursionRemoval(grammar);
        algorithm.process();
        System.out.println(grammar);
    }
}
