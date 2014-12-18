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
        GrammarParser gp = new GrammarParser("../grammar/iulius-grammar-with-func-v2.grammar");
        Grammar grammar = gp.generateGrammar();
        ActionTable at = new ActionTable(grammar);
        StackParser sp = new StackParser("../../test/testType.il", at);
    }
}
