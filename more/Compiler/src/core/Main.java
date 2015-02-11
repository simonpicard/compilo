/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import parser.RecursiveParser;
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
        GrammarParser gp = new GrammarParser("../grammar/iulius-grammar-with-func-v2.grammar");
        Grammar grammar = gp.generateGrammar();
        //grammar.writeLatexTable("../../doc/grammar.tex");
        //FirstFollow firstFollow = new FirstFollow(grammar);
        //firstFollow.process();
        //firstFollow.printFF("../../doc/ff.tex");
        ActionTable at = new ActionTable(grammar);
        //at.writTable("../../doc/action-table.tex");
        RecursiveParser rp = new RecursiveParser("../../test/test.il", at);
        rp.parseProgram();
    }
}
