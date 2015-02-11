/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.AST;

/**
 *
 * @author arnaud
 */
public class ProgramASTNode implements ASTNode {
    private InstructionSequenceASTNode sequenceOfInstructions;
    
    public ProgramASTNode() {
        sequenceOfInstructions = null;
    }

    @Override
    public void addChild(ASTNode child) throws Exception {
        if (sequenceOfInstructions == null) {
            sequenceOfInstructions = (InstructionSequenceASTNode) child;
        }
        else {
            throw new Exception("Sequence of instructions is already set");
        }
    }

    @Override
    public String toString() {
        return "Program node : " + sequenceOfInstructions.toString();
    }
    
}
