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
public class Environment {
    
    private ArrayList<Frame> env;
    
    public Environment(){
        env = new ArrayList<>();
    }
    
    public void add(Frame f){
        env.add(0, f);
    }
    
    public FrameItem get(String k){
        for(int i = 0; i< env.size(); i++){
            FrameItem f = env.get(i).get(k);
            if (f != null){
                return f;
            }
        }
        return null;
    }
    
    
    
}
