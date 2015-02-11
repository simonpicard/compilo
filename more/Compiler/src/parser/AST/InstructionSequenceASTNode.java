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
public class InstructionSequenceASTNode implements ASTNode {
    private List<InstructionASTNode> instructionList;
    
    public InstructionSequenceASTNode() {
        this.instructionList = new ArrayList<>();
    }
    
    public InstructionSequenceASTNode(List<InstructionASTNode> instructionList) {
        this.instructionList = instructionList;
    }

    @Override
    public void addChild(ASTNode child) {
        instructionList.add((InstructionASTNode) child);
    }

    @Override
    public String toString() {
        String res = "Instruction sequence node : ";
        for (InstructionASTNode instruction : instructionList) {
            res += instruction.toString() + ", ";
        }
        return res;
    }
}
