/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import org.joml.Vector3f;
import primitives.*;

/**
 *
 * @author dkruger
 */
public class InsertPointMode extends ModeBase {

    public InsertPointMode(Window w) {
        super(w);
    }

    public void mousePressed(int x, int y) {
        super.mousePressed(x, y);
        Sphere s = new Sphere(1,30);
        w.addShape(s);
    }


    public void mouseReleased(int x, int y) {
        w.addPoint(new Vector3f(x,y,0));
        System.out.print("Hi");
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY) {
        
    }
}
