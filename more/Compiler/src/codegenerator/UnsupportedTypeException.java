/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;
import parser.Type;

/**
 *
 * @author arnaud
 */
public class UnsupportedTypeException extends CodeGeneratorException {
    public UnsupportedTypeException(Type type, String methodName) {
        super("unsupported type (" + type.toString() + ") for method : " + methodName);
    }
}
