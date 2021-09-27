package scad;

/**
 *
 * @author dov
 */
public class Angle {

    private final Vector p1, p2, center;

    public Angle(Vector a, Vector b, Vector center) {
        this.p1 = a;
        this.p2 = b;
        this.center = center;
    }

    public Angle(Vector a, Vector b) {
        this(a, b, Vector.ZERO);
    }

    public Vector r1() {
        return p1.minus(center);
    }

    public Vector r2() {
        return p2.minus(center);
    }

    private double cosTheta() {
        return (r1().dot(r2())) / (r1().abs() * r2().abs());
    }

    public double radians() {
        return Math.acos(cosTheta());
    }

    public double degrees() {
        return radians() * 180 / Math.PI;
    }

    public Vector normal() {
        return r1().cross(r2());
    }

    public static Vector normal(Vector a, Vector b, Vector center) {
        return normal(a.minus(center), b.minus(center));
    }

    public static Vector normal(Vector a, Vector b) {
        return a.cross(b);
    }

    public double compare(Angle angle) {
            
        return angle.cosTheta() - cosTheta();
    }

    @Override
    public String toString() {
        return radians() / Math.PI + " pi";
    }

}
