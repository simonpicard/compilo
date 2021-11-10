/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;
import parser.Type;

/**
 * Exception occuring 
 * @author arnaud
 */
public class IncompatibleTypeException extends CodeGeneratorException {

    public IncompatibleTypeException(Type type1, Type type2, String operation, String line) {
        super("incompatible types " + type1.toString() + " and " + type2.toString() + " for operation " + operation +"\nLine: "+line);
    }
    
}
