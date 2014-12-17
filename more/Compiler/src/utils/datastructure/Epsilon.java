/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.datastructure;

/**
 *
 * @author arnaud
 */
public class Epsilon extends Terminal {

    private Epsilon(String value) {
        super(value);
    }
    
    @Override
    public String toLatex() {
        return "$\\epsilon$";
    }
    
    public static Epsilon getInstance() {
        if (epsilon == null) {
            epsilon = new Epsilon("EPSILON_VALUE");
        }
        return epsilon;
    }
    
    private static Epsilon epsilon = null;
    
}
