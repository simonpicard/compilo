import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main (String[] args){
    System.out.println(args[0]);
    LexicalAnalyzer scanner = null;
        try {
            scanner = new LexicalAnalyzer(new java.io.FileReader(args[0]));
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        try {
        Symbol symbol = null;
            do {
                symbol = scanner.nextToken();
                System.out.println(symbol);
            } while(symbol.getType() != LexicalUnit.END_OF_STREAM);
            
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}