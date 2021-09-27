/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mode;

import primitives.Shape;
import java.util.ArrayList;
import processing.event.MouseEvent;
import processing.core.*;

/**
 *
 * @author yangbai
 */
public class MoveMode extends ModeBase {
    
    private ArrayList<Shape> shapes;
    
    public MoveMode(ArrayList<Shape> s) {
        super();
        this.shapes = s;
    }

    /*public void mouseDragged(float pressedX, float pressedY) {
        
        for (Shape shape : shapes) {
            shape.translate(pressedX - origx, pressedY - origy, 0.0);
        }
    }*/

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //translate((float)(e.getX() - origx), (float)(e.getY() - origy), 0.0f);
    }
}
