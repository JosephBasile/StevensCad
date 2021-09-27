/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import primitives.*;

/**
 *
 * @author dkruger
 */
public class InsertSphereMode extends ModeBase {

    public InsertSphereMode(Window w) {
        super(w);
    }

    public void mousePressed(int x, int y) {
        super.mousePressed(x, y);
        Sphere s = new Sphere(1,30);
        w.addShape(s);
    }

    public void mouseDragged(int x, int y) {
         
        Sphere s = (Sphere) w.getCurrent();
        double dx = x - startx, dy = y - starty;
        double size = Math.sqrt((dx * dx) + (dy * dy));
        s.setRadius(size);
 
        w.updateShape(s);
    }

    public void mouseReleased(int x, int y) {
        //perhaps nothing here?
    }
}
