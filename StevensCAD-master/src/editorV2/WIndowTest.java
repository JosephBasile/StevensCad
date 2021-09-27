/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import gutil.test.GUI2;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import primitives.Sphere;
import primitives.SphericalShell;

/**
 *
 * @author Alessandro
 */
public class WIndowTest extends Window{
    public WIndowTest(){
        super(1000,700,"Hi");
    }
    //Show Window... This must be called after the other window is opened. 
    public void openWindow() {
        while (!glfwWindowShouldClose(windowRef)) {
            this.renderWindow();
            //w2d.renderWindow();
        }

    }
    public static void main(String[] args) {
        WIndowTest w = new WIndowTest();
        //  w.addOneShape(new Sphere());
        File f = new File("assassins.jpg");
        SphericalShell s = new SphericalShell(5,1,5,5);
       // s.setMaxU("1.5");
        try {
            s.setTickness(f);
        } catch (IOException ex) {
            Logger.getLogger(GUI2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        w.addOneShape(s);
        
        w.openWindow();
    }
}
