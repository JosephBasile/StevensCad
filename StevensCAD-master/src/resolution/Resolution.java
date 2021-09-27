package resolution;

import extrusion.Bezier;
import java.util.ArrayList;
import paths3d.Curve;
import scad.Facet;
import scad.Polyhedron;
import scad.Vector;
import transformation.Transformation;

/**
 *
 * @author Alessandro Della Seta
 *
 */
public class Resolution extends Polyhedron{
    Curve curve;
    Bezier curve2;
    Facet base;
    int numStep;
    

    public Resolution(Curve curve, Facet base, int numStep){
        this.curve = curve;
        this.base = base;
        this.numStep = numStep +1;
        this.vertices = new ArrayList<>(base.size() * numStep);
    }
    
       private Vector target(int step, int i) {
        return vertices.get(step * base.size() + i);
    }

    /**
     * get the polyhedron  nodes
     */
    private void addNode() {
        double step =  1.0 / (numStep - 1);  //the height of a period
        Transformation matrix;
        for (int i = 0; i <= numStep; i++) {
            matrix = curve.getMatrix(1 - i * step);//get the curve transform matrix in each step              
            for (int j = 0; j < base.size(); j++)
                vertices.add(matrix.transform(base.get(j))); //convert coordinate       
        }
        
        for(int i = 0; i < vertices.size();i++)
             this.curve.points[i] = vertices.get(i);
              
    }
    
    /**
     * draw the initial Facet 
     */
//    private void addBottom() {
//        int i = 0;
//        for (int j = 1; j < base.size() - 1; ++j) {
//            Facet f = new Facet(target(i, 0), target(i, j), target(i, j+1));
//            add(f);
//        }
//    }
    
    /**
     * draw the the final Facet 
     */
//    private void addTop() {
//        int i = numStep;
//        for (int j = 1; j < base.size() - 1; ++j) {
//            Facet f = new Facet(target(i, 0), target(i, j), target(i, j+1)); 
//            f = f.flip();           
//            add(f);
//        }
//    }
    
    /**
     * draws sides
     */
//    private void drawResolution() {
//        addBottom();
//        for (int i = 1; i <= numStep; i++)
//            for (int j = 0; j < base.size(); j++) {
//                int preVect = i - 1;
//                int nextVec = (j + 1) % base.size();
//                add(new Facet(target(preVect, j), target(i, j), target(i, nextVec)));
//                add(new Facet(target(preVect, j), target(i, nextVec), target(preVect, nextVec)));
//            }
//        addTop();
//    }

        
}


