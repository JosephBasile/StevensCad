package primitives;
import scad.Vector;
/**
 *
 * @author dkruger
 */
public class BoundingSphere {
    public double x, y, z, r, rsq;
    public BoundingSphere(double x, double y, double z, double r) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        rsq = r*r;
    }
    
    public boolean contains(Vector v) {
        double dx = v.x - x, dy = v.y - y, dz = v.z - z;
        return dx*dx + dy*dy + dz*dz <= rsq;
    }
}
