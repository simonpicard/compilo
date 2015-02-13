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
        ActionTable at = InitActionTable.createActionTable();
        Parser rp = new Parser("../../test/test.il", at);
        rp.parseProgram();
    }
}
