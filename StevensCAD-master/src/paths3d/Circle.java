package paths3d;

import scad.Vector;
import static java.lang.Math.*;
/**
 * Resources: http://www.math.ubc.ca/~feldman/m263/circle.pdf
 * @author Alessandro Della Seta
 */

public class Circle extends Curve {
    private double radius;  
    private Vector v, u, n;
  
    /**
    * Constructor
    * @param radius 
    */
     public Circle(double radius){
        this.radius = radius;
    }
 
    /**
     * 
     * @return the radius 
     */
    public double getRadius(){
        return radius;
    }
    
    /**
     *Compute the coordinates for the formula of a circle 3D
     */
    public void compute(){
        this.n = points[1].normalized();
        this.u = points[0].cross(n);
        this.u.normalized();
        this.v = u.cross(n);
        this.v.normalized();
    }
    
    /**
     * Circle 3D formula: C + u*cos(t)*r + (u x n)*sin(t)*r
     * Where C is the center of the circle, r is the radius,
     * u is an orthogonal vector to the normal 
     * @param step
     * @return the Vector of a point of the circle
     */
    @Override
    Vector getPoint(double step) {
        double t = PI * 2* step;
        compute();
        return points[0].added(u.mult(radius*cos(t))).added(v.mult(radius*sin(t)));
     }
     /**
      * Calculate the tangent of a circle point on the x,y plane and return it moved in the correct x,y,z position
      * @param step
      * @return the tangent of a circle point 
      */      
    @Override
    Vector getTangent(double step) {
        double t = PI * 2* step;
        return u.mult(-radius*sin(t)).added(v.mult(radius*cos(t)));
   }
    
 /**
  * methods not necessary
  * @return center
  */                            
    Vector getA() {
        return points[0];
    }
   /**
    * methods not necessary
    * @return normal
    */            
    Vector getB() {
        return points[1];
    }
}
