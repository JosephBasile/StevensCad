/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mode;

import processing.event.MouseEvent;

/**
 *
 * @author yangbai
 */
public abstract class ModeBase {
    protected int origx, origy;
    
    public ModeBase() {
        
    }
    public void mousePressed(int mouseX, int mouseY) {
        this.origx = mouseX;
        this.origy = mouseY;
    }
    public abstract void mouseReleased(MouseEvent e);
    public abstract void mouseDragged(MouseEvent e);
}
