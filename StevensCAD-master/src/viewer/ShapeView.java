/**
 * This project is licensed under GPL 2.1. See @link License.html
 */
package viewer;

import processing.core.*;
import scad.Triangle;
import processing.core.*;
import java.util.*;
import primitives.Shape;
import static processing.core.PConstants.*;
import scad.Facet;
import scad.Polyhedron;

/**
 *
 * @author dkruger Draw SCAD shapes as solids, as wireframe, and show volumes
 * being subtracted also for ease of debugging models.
 * 
 * @edit  Fady
 * 
 */
public class ShapeView {

    public static Color inColor = Color.purple(), outColor = new Color(180, 150, 0);

    public enum Mode {
        WIREFRAME, TRIANGLE, DOUBLEWALL, VERTEX, POINTS
    };

    public static PShape create(PApplet p, Shape s) {
        return create(p, s, ShapeView.Mode.DOUBLEWALL, Color.white());
    }
    
//     public static PShape create(PApplet p, Polyhedron s, viewer.Color stroke, viewer.Color color) {
//        return create(p, s, ShapeView.Mode.DOUBLEWALL, stroke, color);
//    }
//     
//      
     
     

    public static PShape create(PApplet processing, Shape shape3d, Mode m, Color stroke) {

        PShape visual = processing.createShape();

        ArrayList<Triangle> triangles = shape3d.triangles();
        
        switch (m) {
            case DOUBLEWALL:
                visual.beginShape(TRIANGLE);
                visual.stroke(stroke.r, stroke.g, stroke.b);
                visual.fill(inColor.r, inColor.g, inColor.b);

                for (Triangle t : triangles) {
                    Facet backSide = t.backSide();
                    for (scad.Vector v : backSide.vertex)
                        visual.vertex((float) v.x, (float) v.y, (float) v.z);
                }
                visual.endShape();
                
            case TRIANGLE:
                visual.beginShape(TRIANGLE);
                visual.stroke(stroke.r, stroke.g, stroke.b);
                visual.fill(outColor.r, outColor.g, outColor.b);
                for (Triangle t : triangles)
                    for (scad.Vector v : t.vertex)
                        visual.vertex((float) v.x, (float) v.y, (float) v.z);
                break;
            
            case WIREFRAME:
                visual.beginShape(LINES);
                visual.stroke(stroke.r, stroke.g, stroke.b);

                for (Triangle t : triangles) {
                    for (scad.Vector v : t.vertex)
                        visual.vertex((float) v.x, (float) v.y, (float) v.z);
                    visual.vertex((float) t.get(0).x, (float) t.get(0).y, (float) t.get(0).z);
                }
                break;
        }
        visual.endShape();
        return visual;
    }
    
    /**
     * 
     * @Fady
     * points or vertex plotting
     * @param processing
     * @param vertexlist
     * @param color
     * @return 
     */
    
     public static PShape create(PApplet processing, ArrayList<scad.Vector> vertexlist, Color color) {
         //int count =0;
         PShape visual = processing.createShape();
       
                visual.beginShape(POINTS);
                visual.stroke(color.r, color.g, color.b);
                
                for (scad.Vector v: vertexlist) {
                      visual.vertex((float) v.x,(float) v.y, (float)v.z);
                     // System.out.print    ln(v.x + " "+ v.y + " "+ v.z);
                    //   System.out.println(count);
                      // count++;
                }
                 //System.out.println("pointcloud plot is successful");
                visual.endShape();
                
        return visual;
    }
}
