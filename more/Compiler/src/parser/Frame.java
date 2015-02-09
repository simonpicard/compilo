/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.HashMap;

/**
 *
 * @author Simon
 */
public class Frame {
    
    private HashMap<String, FrameItem> entries;
    
    public Frame(){
        entries = new HashMap<>();
    }
    
    public void add(String k, FrameItem item){
        entries.put(k, item);
    }
    
    public FrameItem get(String k){
        return entries.get(k);
    }
}
