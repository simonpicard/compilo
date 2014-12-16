/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import utils.datastructure.ActionTable;
import utils.datastructure.Token;
import scanner.LexicalAnalyzer;
import scanner.Symbol;
import scanner.SyntaxErrorException;
import utils.datastructure.Epsilon;
import utils.datastructure.ProductionRule;
import utils.datastructure.Terminal;
import utils.datastructure.Variable;

/**
 *
 * @author Simon
 */
public class StackParser {

    public StackParser(String path, ActionTable actionTable) throws IOException, FileNotFoundException, PatternSyntaxException, SyntaxErrorException {
        parse(path, actionTable);
    }

    private void parse(String path, ActionTable actionTable) throws FileNotFoundException, IOException, PatternSyntaxException, SyntaxErrorException {
        LexicalAnalyzer la = new LexicalAnalyzer(new FileInputStream(path));
        List<Token> stack = new ArrayList<Token>();
        Terminal inputTerminal = Terminal.castToTerminal(la.nextToken());
        HashMap<Variable, HashMap<Terminal, ProductionRule>> table = actionTable.getTable();
        stack.add(new Variable("<Program>"));
        Token topOfStack;
        ProductionRule productionRule;
        Boolean go = true;
        String res = "";
        while (go) {
            topOfStack = stack.isEmpty() ? Epsilon.getInstance() : stack.get(stack.size() - 1);
            System.out.println("Top of stack : " + topOfStack);
            System.out.println("Input terminal : " + inputTerminal);
            System.out.println("Stack : " + stack);
            if (inputTerminal.equals(Epsilon.getInstance()) && stack.isEmpty()) {
                System.out.println("Accept " + res);
                go = false;
            } else if (!topOfStack.isTerminal() && table.get((Variable) topOfStack).get(inputTerminal) != null) {
                //produce
                System.out.println("Produce " + table.get((Variable) topOfStack).get(inputTerminal));
                productionRule = table.get((Variable) topOfStack).get(inputTerminal);
                stack.remove(stack.size() - 1);
                for (int i = productionRule.getRightPart().size() - 1; i >= 0; i--) {
                    if (!productionRule.getRightPart().get(i).equals(Epsilon.getInstance())) {
                        stack.add(productionRule.getRightPart().get(i));
                    }
                }
            } else if (topOfStack.isTerminal() && (inputTerminal).equals((Terminal) topOfStack)) {
                //match
                System.out.println("Match " + inputTerminal);
                res += stack.get(stack.size() - 1).toString() + " ";
                stack.remove(stack.size() - 1);
                inputTerminal = Terminal.castToTerminal(la.nextToken());
            } else {
                System.out.println("Error " + res);
                go = false;
            }
        }
    }

}
