import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class Main {
    public static void main (String[] args){
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
                if (symbol.getType() != LexicalUnit.END_OF_STREAM) {
                    System.out.println(symbol);
                }
            } while(symbol.getType() != LexicalUnit.END_OF_STREAM);
            
        }
        catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("Identifiers");
//         to do : trier les identifiers par ordre lexicographique
        HashMap<Integer, Symbol> identifiersMap = scanner.getIdentifiersMap();
        Set<Integer> keys = identifiersMap.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Symbol symbol = identifiersMap.get(it.next());
            System.out.println(symbol.getValue() + "  " + symbol.getLine());
        }
    }
}