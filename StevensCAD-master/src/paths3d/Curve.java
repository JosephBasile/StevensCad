package paths3d;

import transformation.Transformation;
import scad.Vector;

/**
 *
 * @author dov , Alessandro Della Seta, Minghui Jin
 */
public abstract class Curve {
    public int used;
    public int size;
    public Vector[] points;
    
    public Curve() {
        used = 0;
        size = 2;
        points = new Vector[size];
    }
    
    public void add(Vector p) {
        if (used == size) resize();      
        points[used++] = p;
    }
    
    public void resize() {
        Vector[] temp = points;
        size *= 2;
        points = new Vector[size];
        System.arraycopy(temp, 0, points, 0, size/2);
    }
    
    public int size() {
        return used;
    }
        
//    abstract Vector getA();
//    abstract Vector getB();
    abstract Vector getPoint(double step);
    abstract Vector getTangent(double step);
    
//  public Vector at(double t);
    
    /**
     * get the xyz-plane coordinate matrix of current point
     * @param step 
     * @return a matrix formed by xaxis, yaxis, zaxis and point vector
     */
    public Transformation getMatrix(double step) {
        Vector point = getPoint(step);
        if (point.equals(Vector.ZERO))
            point = new Vector(-0.01,0.01,0);

        Vector zaxis = getTangent(step);
        zaxis = zaxis.normalized();

        Vector yaxis, xaxis;
        if(zaxis.equals(new Vector(0,0,1))){
            yaxis = new Vector(0,1,0);
        }
        else if(zaxis.equals(new Vector(0,0,-1))){
            yaxis = new Vector(0,-1,0);
        }
        else {
            yaxis = point.cross(zaxis);
            yaxis = yaxis.normalized();
        }      
   
        if(zaxis.equals(new Vector(0,0,1))){
            xaxis = new Vector(1,0,0);
        }
        else if(zaxis.equals(new Vector(0,0,-1))){
            xaxis = new Vector(-1,0,0);
        }
        else {
            xaxis = yaxis.cross(zaxis);
            xaxis = xaxis.normalized();
        }        
        
        return new Transformation(xaxis, yaxis, zaxis, point);
    }
}
