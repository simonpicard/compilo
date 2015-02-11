/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author arnaud
 */
public class LlvmCodeGenerator {
    private FileOutputStream outputFile;
    
    public LlvmCodeGenerator(String outputPath) throws FileNotFoundException {
        outputFile = new FileOutputStream(new File(outputPath));
    }
    
    public void write(LlvmItem llvmItem) throws IOException {
        outputFile.write(llvmItem.toByte());
    }
}
