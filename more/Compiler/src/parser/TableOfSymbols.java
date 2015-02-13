/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;

/**
 *
 * @author Simon
 */
public class TableOfSymbols {
    
    private ArrayList<Frame> tos;
    
    public TableOfSymbols(){
        tos = new ArrayList<>();
    }
    
    public void addNewEntry(String identifier, FrameItem entry) {
        tos.get(0).add(identifier, entry);
        System.out.println(this);
    }
    
    public void addFrame(Frame f){
        tos.add(0, f);
    }
    
    public void removeFrame() {
        tos.remove(0);
    }
    
    public FrameItem lookup(String identifier){
        for(int frame = 0; frame< tos.size(); frame++){
            FrameItem f = tos.get(frame).get(identifier);
            if (f != null){
                return f;
            }
        }
        return null;
    }
    
    public void removeItem(String identifier){
        for(int frame = 0; frame< tos.size(); frame++){
            boolean f = tos.get(frame).remove(identifier);
            if (f){
                return;
            }
        }
    }
    
    @Override
    public String toString() {
        String res = "=====Table of symbols=====\n";
        for (int frame=0; frame<tos.size(); ++frame) {
            res += "===Frame " + frame + "===\n";
            for (String identifier : tos.get(frame).identifiers()) {
                res += identifier + " : " + tos.get(frame).get(identifier).toString() + "\n";
            }
            res += "\n";
        }
        return res;
    }
    
}
