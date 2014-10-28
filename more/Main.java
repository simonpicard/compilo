import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeSet;

public class Main {
    
    public static void main (String[] args) throws FileNotFoundException, IOException, SyntaxErrorException {
    
        if (args.length != 2) {
            System.out.println("L'utilisation est : java -jar compiler.jar Main Iuliusfile");
        }
        else {
            LexicalAnalyzer scanner = null;
                
            scanner = new LexicalAnalyzer(new java.io.FileReader(args[1])); 
                
            Symbol symbol = null;
            do {
                symbol = scanner.nextToken();
                if (symbol.getType() != LexicalUnit.END_OF_STREAM) {
                    System.out.println(symbol);
                }
            } while(symbol.getType() != LexicalUnit.END_OF_STREAM);
            System.out.println("Identifiers");

            HashMap<Object, Integer> identifiersMap = scanner.getIdentifiersMap();
            TreeSet<Object> keys = new TreeSet<Object>(identifiersMap.keySet());
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                Object identifier = it.next();
                System.out.println(identifier + "  " + identifiersMap.get(identifier));
            }
        }
    }
}
