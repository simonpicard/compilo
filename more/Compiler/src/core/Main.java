/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import parser.StackParser;
import utils.datastructure.ActionTable;
import utils.datastructure.Grammar;
import utils.datastructure.GrammarParser;

/**
 *
 * @author arnaud
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("ok");
        GrammarParser gp = new GrammarParser("C:\\Users\\Simon\\Desktop\\git\\compilo\\more\\grammar\\iulius-grammar-without-func-v8.grammar");
        Grammar grammar = gp.generateGrammar();
        ActionTable at = new ActionTable(grammar);
        at.writTable("out.tex");
        StackParser sp = new StackParser("C:\\Users\\Simon\\Desktop\\git\\compilo\\test.txt", at);
    }
}
