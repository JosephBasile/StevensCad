/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import mode.*;
import processing.event.MouseEvent;

/**
 * new version based on original for processing.
 * This one is based on LWJGL using Window class that Itay implemented
 * 
 * @author yangbai
 */
public abstract class ModeBase {
    protected Window w;
    protected int startx, starty;
    
    public ModeBase(Window w) {
        this.w = w;
    }
    public void mousePressed(int mouseX, int mouseY) {
        this.startx = mouseX;
        this.starty = mouseY;
    }
    public abstract void mouseReleased(int mouseX, int mouseY);
    public abstract void mouseDragged(int mouseX, int mouseY);
}
