/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.HashMap;
import java.util.Set;

/**
 * Block of declaration for identifier.
 * @author Simon
 */
public class Frame {
    
    private HashMap<String, FrameItem> entries;
    
    public Frame(){
        entries = new HashMap<>();
    }
    
    public Set<String> identifiers() {
        return entries.keySet();
    }
    
    public void add(String identifier, FrameItem item){
        entries.put(identifier, item);
    }
    
    public FrameItem get(String identifier){
        return entries.get(identifier);
    }
}
