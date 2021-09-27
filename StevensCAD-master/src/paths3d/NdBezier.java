package paths3d;

import scad.Vector;

/**
 *
 * @author yanyanjiang
 */
 
public abstract class NdBezier extends Curve{
    
    public NdBezier() {
    }

    
    
    Vector getPoint(double step) {
        
        int number = super.size();
        //at least 2
        if (number < 2 )
            return null;

        //Pascal Triangle
        int[] mi = new int[number];
        mi[0] = mi[1] = 1;
        for (int i = 3; i <= number; i++) {
            int[] t = new int[i - 1];
            for (int j = 0; j < t.length; j++) {
                t[j] = mi[j];
            }
            mi[0] = mi[i - 1] = 1;
            for (int j = 0; j < i - 2; j++) {
                mi[j + 1] = t[j] + t[j + 1];
            }
        }

        // points 
        Vector temp = Vector.ZERO;
            for (int k = 0; k < number; k++) {
                temp = temp.added(super.points[k].mult(Math.pow(1 - step, number - k - 1)* Math.pow(step, k) * mi[k]));
            }
        return temp;
    }

    Vector getTangent(double step) {  
        double deltaStep = 0.0000001;
        return getPoint(step+ deltaStep).minus(getPoint(step)).divided(deltaStep);
    }
/*    
    public Matrix3D getMatrix(double step) {
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

        return new Matrix3D(xaxis, yaxis, zaxis, point);
    }
*/
}
