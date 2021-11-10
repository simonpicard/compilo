/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import parser.Parser;
import utils.datastructure.ActionTable;

/**
 *
 * @author arnaud
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("L'utilisation est : java -jar compiler.jar Main Iuliusfile");
        }
        else {
            ActionTable at = InitActionTable.createActionTable();
            Parser rp = new Parser(args[1], at);
            rp.parseProgram();
        }
    }
}
