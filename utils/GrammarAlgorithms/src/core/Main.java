package core;

import algorithms.FirstFollow;
import algorithms.LeftRecursionRemoval;
import algorithms.UselessSymbolRemoval;
import algorithms.LeftFactoring;
import algorithms.GrammarAlgorithm;
import datastructure.ActionTable;
import datastructure.GrammarParser;
import datastructure.Grammar;
import datastructure.Variable;
import datastructure.Terminal;
import java.util.HashSet;


/**
 *
 * @author arnaud
 */
public class Main {

    public static void main(String args[]) throws Exception {
        //GrammarParser grammarParser = new GrammarParser("/home/arnaud/Documents/ulb/ma1-2014-2015/compiler/projet/iulius-grammar-without-func-v8.grammar");
        GrammarParser grammarParser = new GrammarParser("C:\\Users\\Simon\\Desktop\\git\\compilo\\utils\\iulius-grammar-without-func-v8.grammar");
        Grammar grammar = grammarParser.generateGrammar();
        System.out.println(grammar);
        ActionTable actionTable = new ActionTable(grammar);
        actionTable.writTable("./out.tex");
        //FirstFollow algo = new FirstFollow(grammar);
        //algo.process();
        System.out.println(actionTable.);
    }
}
