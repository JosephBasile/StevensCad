package test;

import editorV2.CADEditor2;
import editorV2.WIndowTest;
import gutil.Conf;
import gutil.test.GUI2;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import scad.STL;
import primitives.SphericalShell;
import viewer.CADEditor;

/**
 *
 * @author dkruger
 */
public class TestSphericalShell {
    public static void main(String[] args) throws IOException {
      WIndowTest w = new WIndowTest();
        //  w.addOneShape(new Sphere());
        File f = new File("earth1000x500.png");
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
