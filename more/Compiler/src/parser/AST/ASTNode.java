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
public interface ASTNode {
    public abstract void addChild(ASTNode child) throws Exception;
}
