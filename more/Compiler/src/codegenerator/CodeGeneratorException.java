/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

/**
 * Generic exception occuring during code generation step.
 * @author arnaud
 */
public class CodeGeneratorException extends Exception {

    public CodeGeneratorException(String message) {
        super("Error occuring during code generation step : " + message);
    }
    
}
