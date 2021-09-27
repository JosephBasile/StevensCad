package paths3d;
/**
 *
 * @author Alessandro Della Seta
 */
import scad.Vector;

public class Line extends Curve{
    private Vector[] vt;
    private int used;
    public Line(){
        vt = new Vector[2];
        used = 0;
    }
    
    public void add(Vector s){
        vt[used++] = s;
    }
    @Override
    public String toString() {
        return  "Line control point is: \n p0=" + vt[0].x + "," + vt[0].y + "," +  vt[0].z +
                "\n p1=" +vt[1].x + "," +  vt[1] + "," +  vt[1].z;
    }
    
    /**
     * Parametric function of a line (A,B) with param t[0,1] :
     * x(t) = xA + t(xB-xA) 
     * y(t) = yA + t(yB-yA)
     * z(t) = zA + t(zB-zA)
     * @param step
     * @return 
     */
    @Override
    Vector getPoint(double step) {
        if (step <= 0)
            return vt[0];
        else if(step >= 1)
            return vt[1];
        else 
            return new Vector( vt[0].x + step*(vt[1].x- vt[0].x),  vt[0].y + step*(vt[1].y- vt[0].y),  vt[0].z + step*(vt[1].z- vt[0].z));
    }
    /**
     * the tangent of a line has the same direction for all its point
     * @param step
     * @return the tangent of a line 
     */
    @Override
    Vector getTangent(double step) {
        return vt[1].minus(vt[0]); 
    }
    
    /**
     * 
     * @return the slope of a 3D line
     */
     public double getSlope(){
        double offset = Math.sqrt(Math.pow(points[1].z- points[0].z,2)+ Math.pow(points[1].x- points[0].x,2));
        return (points[1].y-points[0].y)/offset;
    }
     
   /**
    * 
    * @return the first point of the line 
    */
    public Vector getA() {
        return vt[0];
    }
    /**
     * 
     * @return the last point of the line
     */
    public Vector getB() {
        return vt[1];
    }
}
