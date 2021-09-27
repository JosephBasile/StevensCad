package paths3d;

import scad.Vector;
import static java.lang.Math.*;

/**
 *
 * @author minghuijin
 */
public class Spline extends Curve{
    // when point number is over 15, then MATRIX would stay as 3.732050807568877, RATIO as 0.2679491924311227.
    private static final double[] MATRIX = {2.0,                3.5,                3.7142857142857144, 3.7307692307692310,  
                                            3.7319587628865980, 3.7320441988950277, 3.7320503330866024, 3.7320507735025785, 
                                            3.7320508051230270, 3.7320508073932730, 3.7320508075562695, 3.7320508075679720,  
                                            3.7320508075688124, 3.7320508075688728, 3.7320508075688767, 3.7320508075688770 };
                                    
    private static final double[] RATIO  = {0.5,                 0.28571428571428570, 0.26923076923076920, 0.26804123711340205, 
                                            0.26795580110497236, 0.26794966691339750, 0.26794922649742164, 0.26794919487697294, 
                                            0.26794919260672684, 0.26794919244373050, 0.26794919243202790, 0.26794919243118770,  
                                            0.26794919243112736, 0.26794919243112303, 0.26794919243112275, 0.26794919243112270 };                                
    
    private double[][] x_para;
    private double[][] y_para;
    private double[][] z_para;
    
    public Spline() {
        super();
        x_para = new double[size - 1][4];
        y_para = new double[size - 1][4];
        z_para = new double[size - 1][4];
    }
        
    /**
     * get the symmetric tridiagonal system
     *    x            y           z
     * 3(x1 - x0)   3(y1 - y0)   3(z1 - z0)
     * 3(x2 - x0)   3(y2 - y0)   3(z2 - z0)
     * 3(x3 - x1)   3(y3 - y1)   3(z3 - z1)
     * 3(x3 - x2)   3(y3 - y2)   3(z3 - z2)
     */ 
    public void Solution() {        
        double[] X = new double[used];
        double[] Y = new double[used];
        double[] Z = new double[used];
        
        for (int i = 0; i < used; i++) {
            if (i == 0) {
                X[i] = (points[i+1].x - points[i].x) * 3;
                Y[i] = (points[i+1].y - points[i].y) * 3;
                Z[i] = (points[i+1].z - points[i].z) * 3;
            }
            else if (i == used - 1) {
                X[i] = (points[i].x - points[i-1].x) * 3;
                Y[i] = (points[i].y - points[i-1].y) * 3;
                Z[i] = (points[i].z - points[i-1].z) * 3;
            }
            else {
                X[i] = (points[i+1].x - points[i-1].x) * 3;
                Y[i] = (points[i+1].y - points[i-1].y) * 3;
                Z[i] = (points[i+1].z - points[i-1].z) * 3;
            }
        }   
        
        // get the matrix after gaussian elimination
        for (int i = 1; i < used; i++) {
                final double R = i >= 15 ? RATIO[15] : RATIO[i-1];
                X[i] = (X[i] - X[i-1]) * R;
                Y[i] = (Y[i] - Y[i-1]) * R;
                Z[i] = (Z[i] - Z[i-1]) * R;
        }
        
        // get the solution of the equation
        double[] Dx = new double[used]; Dx[used-1] = X[used-1] / (2 - RATIO[used - 2]);
        double[] Dy = new double[used]; Dy[used-1] = Y[used-1] / (2 - RATIO[used - 2]);
        double[] Dz = new double[used]; Dz[used-1] = Z[used-1] / (2 - RATIO[used - 2]);
        for (int i = used-2; i >= 0; i--) {
            final double M = i >= 15 ? MATRIX[15] : MATRIX[i];
            Dx[i] = (X[i] - Dx[i+1]) / M;
            Dy[i] = (Y[i] - Dy[i+1]) / M;
            Dz[i] = (Z[i] - Dz[i+1]) / M;
        }
        
        polynomial(Dx, Dy, Dz);
    }
    
    /** 
     * get the parameters for X, Y, Z polynomials of each segment
     * a_i  =   y_i
     * b_i  =   D_i
     * c_i  =   3(y_(i+1) - y_i) - 2D_i - D_(i+1)
     * d_i  =   2(y_i - y_(i+1)) + D_i + D_(i+1).
     * @param Dx the X solution
     * @param Dy the Y solution
     * @param Dz the Z solution
     */
    public void polynomial(double[] Dx, double[] Dy, double[] Dz) {
        x_para = new double[used - 1][4];
        y_para = new double[used - 1][4];
        z_para = new double[used - 1][4];
        for (int i = 0; i < used - 1; i++) {
            x_para[i][0] = points[i].x;
            y_para[i][0] = points[i].y;
            z_para[i][0] = points[i].z;
            
            x_para[i][1] = Dx[i];
            y_para[i][1] = Dy[i];
            z_para[i][1] = Dz[i];
            
            x_para[i][2] = 3 * (points[i+1].x - points[i].x) - 2 * Dx[i] - Dx[i+1];
            y_para[i][2] = 3 * (points[i+1].y - points[i].y) - 2 * Dy[i] - Dy[i+1];
            z_para[i][2] = 3 * (points[i+1].z - points[i].z) - 2 * Dz[i] - Dz[i+1];
            
            x_para[i][3] = 2 * (points[i].x - points[i+1].x) + Dx[i] + Dx[i+1];
            y_para[i][3] = 2 * (points[i].y - points[i+1].y) + Dy[i] + Dy[i+1];
            z_para[i][3] = 2 * (points[i].z - points[i+1].z) + Dz[i] + Dz[i+1];
        }
    }
    
    /**
     * get the position of current point
     * @param step the proportion of current point on the whole line
     * @return
     */
    public Vector getPoint(double step) {       
        if (step <= 0) {
            return points[0];
        } 
        else if (step >= 1) {
            return points[used - 1];
        }
        else{
            int i = (int) floor(step * (used - 1));
            double st = step * (used - 1) - i;           
            double x = x_para[i][3]*st*st*st + x_para[i][2]*st*st + x_para[i][1]*st + x_para[i][0];
            double y = y_para[i][3]*st*st*st + y_para[i][2]*st*st + y_para[i][1]*st + y_para[i][0];  
            double z = z_para[i][3]*st*st*st + z_para[i][2]*st*st + z_para[i][1]*st + z_para[i][0];
            Vector p = new Vector(x, y, z);
            return p;
        }
    }
    
    /**
     * get the tangent of current point on the curve 
     * the equation of the derivative of Cubic Spline : 3a * t * t + 2b * t + c
     * @param step the segment place of current point on the curve
     * @return 
     */
    public Vector getTangent(double step) {        
        double n = step * (used - 1);
        int i;
        if (n == used - 1) {
            i = used - 2;
        }
        else {
            i = (int) Math.floor(n);   
        }     

        double st = step * (used - 1) - i;         
        double x = 3 * x_para[i][3]*st*st + 2 * x_para[i][2]*st + x_para[i][1];
        double y = 3 * y_para[i][3]*st*st + 2 * y_para[i][2]*st + y_para[i][1];  
        double z = 3 * z_para[i][3]*st*st + 2 * z_para[i][2]*st + z_para[i][1];
        Vector tang = new Vector(x, y, z).neg();
        
        return tang;
    }
       
    public String toString() {
        String s = "";
        for (int i = 0; i < used; i++) {
            s += points[i].x + "\t" + points[i].y + "\t" + points[i].z + "\n";
        }
        return s;
    }
    
    public static void main(String[] args) {
        Spline p1 = new Spline();
        p1.add(new Vector(0, 0, 0));
        p1.add(new Vector(100, 50, 50));
        p1.add(new Vector(200, 25, 20));
        p1.add(new Vector(250, 0, 40));
//        p.add(new Vector(400, -50, 0));

        p1.Solution();
//        System.out.println(p);
//        System.out.println("size = " + p.size());
        System.out.println(p1.getPoint(0.75));
        p1.getMatrix(0.2);
//        System.out.println(p.getMatrix(0));
    }
}
