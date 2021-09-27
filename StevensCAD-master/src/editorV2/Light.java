/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;


import org.joml.Vector3f;

/**
 *
 * @author itay
 */
public class Light {
    private Vector3f pos;
    private Vector3f color;
    
    public Light(Vector3f pos, Vector3f color){
        this.pos = pos;
        this.color = color;
    }
    
    public void setPosition(Vector3f pos){
        this.pos = pos;
    }
    
    public Vector3f getPosition(){
        return this.pos;
    }
    
    public void setColor(Vector3f color){
        this.color = color;
    }
    
    public Vector3f getColor(){
        return this.color;
    }
}
