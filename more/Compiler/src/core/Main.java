/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import parser.StackParser;
import utils.algorithms.FirstFollow;
import utils.datastructure.ActionTable;
import utils.datastructure.Epsilon;
import utils.datastructure.Grammar;
import utils.datastructure.GrammarParser;
import utils.datastructure.Terminal;
import utils.datastructure.Variable;

/**
 *
 * @author arnaud
 */
public class Main {

    public static void main(String[] args) throws Exception {
        GrammarParser gp = new GrammarParser("../../more/grammar/iulius-grammar-without-func-v10.grammar");
        Grammar grammar = gp.generateGrammar();
        FirstFollow algo = new FirstFollow(grammar);
        algo.process();
        algo.printFF("./out.tex");
        //ActionTable at = new ActionTable(grammar);
        //at.writTable("out.tex");
        //StackParser sp = new StackParser("/home/arnaud/Documents/ulb/ma1-2014-2015/compiler/projet/compilo/test/testType.il", at);
    }
}
