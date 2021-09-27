package viewer;

import viewer.CADEditor;
import primitives.*;


/**
 *
 * @author dkruger
 */
public class TestCADViewer {
    public static void main(String[] args) {
        CADEditor v = CADEditor.openWindow(0, 0, 1024, 768);
        v.add(new Sphere(100.0, 30,30));
    }
    
}
