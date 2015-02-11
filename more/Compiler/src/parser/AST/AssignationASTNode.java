/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.AST;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arnaud
 */
public class AssignationASTNode extends InstructionASTNode {
    private List<VariableASTNode> variables;
    private List<ExpressionASTNode> expressions;
    
    public AssignationASTNode() {
        variables = new ArrayList<>();
        expressions = new ArrayList<>();
    }
    
    public void add(VariableASTNode variable, ExpressionASTNode expression) {
        variables.add(variable);
        expressions.add(expression);
    }

    @Override
    public void addChild(ASTNode child) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        String res = "Assignation node : ";
        for (int i=0; i<variables.size(); ++i) {
            res += "(" + variables.get(i).toString() + ", " + expressions.get(i).toString() + ")";
        }
        return res;
    }
}
