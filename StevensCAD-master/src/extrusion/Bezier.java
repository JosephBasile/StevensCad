package extrusion;

import scad.Vector;
import java.util.ArrayList;
import transformation.Transformation;

/**
 *
 * Reference : http://devmag.org.za/2011/04/05/bzier-curves-a-tutorial/
 *
 * @author Yanyan Jiang and Minghui Jin
 */
public class Bezier {

    private ArrayList<Vector> vt;

    /*
    TODO: This one really shouldn't exist.  What are the points for an empty bezier?
    public Bezier() {
        vt = new ArrayList<>(4);
    }
     */
    // two anchor points and two control points
    public Bezier(Vector p0, Vector p1, Vector p2, Vector p3) {
        vt = new ArrayList<>(4);

        vt.add(p0);
        add(p1, p2, p3);
    }

    /*
    
        for (int i = 0; i < bezier.size(); i += 3) {
      for (double t = 0; t < 1; t += 0.1)
           getPoint(i, t)

     */
    public int size() {
        return vt.size();
    }

    public double getLastX() {
        return vt.get(vt.size() - 1).x;
    }

    public double getLastY() {
        return vt.get(vt.size() - 1).y;
    }

    public double getLastZ() {
        return vt.get(vt.size() - 1).z;
    }

    /*
    public Bezier(double p0x, double p0y, 
                  double p1x, double p1y,
                  double p2x, double p2y,
                  double p3x, double p3y) {
        vt = new ArrayList<>(4);
        set(p0x, p0y, 0, 
            p1x, p1y, 0, 
            p2x, p2y, 0, 
            p3x, p3y, 0);
    }
     */
 /*
    public Bezier(double p0x, double p0y, double p0z, 
                  double p1x, double p1y, double p1z, 
                  double p2x, double p2y, double p2z,
                  double p3x, double p3y, double p3z) {
        vt = new ArrayList<>(4);
        set(p0x, p0y, p0z, 
            p1x, p1y, p1z, 
            p2x, p2y, p2z, 
            p3x, p3y, p3z);
    }
     */
    public String toString() {
        StringBuilder b = new StringBuilder(vt.size() / 3 * 100);
        b.append("Bezier:");
        for (int i = 0; i < vt.size(); i += 3) {
            b.append("p0=").append(vt.get(i).x).append(',').append(vt.get(i).y).append(',').append(vt.get(i).z).append('\n');
            b.append("p1=").append(vt.get(i + 1).x).append(',').append(vt.get(i + 2).y).append(',').append(vt.get(i + 2).z).append('\n');
            b.append("p2=").append(vt.get(i + 2).x).append(',').append(vt.get(i + 2).y).append(',').append(vt.get(i + 2).z).append('\n');
            b.append("p3=").append(vt.get(i + 3).x).append(',').append(vt.get(i + 3).y).append(',').append(vt.get(i + 3).z).append("\n\n");
        }
        return b.toString();
    }

    /**
     * Set the segment starting at index i
     *
     * @param i
     * @param p0
     * @param p1
     * @param p2
     * @param p3
     */
    public void set(int i, Vector p0, Vector p1, Vector p2, Vector p3) {
        vt.set(i, p0);
        vt.set(i + 1, p1);
        vt.set(i + 2, p2);
        vt.set(i + 3, p3);
    }

    /*
    public void set(double p0x, double p0y, double p0z, 
             double p1x, double p1y, double p1z,
             double p2x, double p2y, double p2z,
             double p3x, double p3y, double p3z) {
        vt[0] = new Vector(p0x, p0y, p0z);
        vt[1] = new Vector(p1x, p1y, p1z);
        vt[2] = new Vector(p2x, p2y, p2z);
        vt[3] = new Vector(p3x, p3y, p3z);
    }
     */
    /**
     * Add a segment to an existing Bezier. The last point of the Bezier is the
     * first point of this segment. Therefore add only 3 points for each segment
     *
     * @param p1 derivative for the first point
     * @param p2 derivative of the second point
     * @param p3 end point in the segment
     */
    public void add(Vector p1, Vector p2, Vector p3) {
        vt.add(p1);
        vt.add(p2);
        vt.add(p3);
    }

    /**
     * Insert a segment into the middle of an existing Bezier. The last point of
     * the Bezier is the first point of this segment. Therefore add only 3
     * points for each segment
     *
     * @param offset the starting location in the bezier
     * @param p1 derivative for the first point
     * @param p2 derivative of the second point
     * @param p3 end point in the segment
     */
    public void insert(int offset, Vector p1, Vector p2, Vector p3) {
        vt.add(offset, p1);
        vt.add(offset + 1, p2);
        vt.add(offset + 2, p2);
    }

    public void insertPoint(int offset, Vector p) {
        Vector deriv1 = new Vector(p.x - 5, p.y, p.z);
        Vector deriv2 = new Vector(p.x + 5, p.y, p.z);
        insert(offset,deriv1,p,deriv2);
    }

    /**
     * Find the segment cont
     *
     * @param v
     * @return
     */
    public int findNearestSegment(float dt, Vector point) {

        double bestDist = 1000000;
        float bestdt = 0;

        for (float i = 0; i <= 1; i += dt) {
            double dist = calculateDistance(point, getPoint(i));
            if (bestDist > dist) {
                bestDist = dist;
                bestdt = i;
            }
        }

        int segments = vt.size() / 3;
        int pickSegment = (int) (segments * bestdt);
        double startSegment = (double) pickSegment / segments;
        double endSegment = (double) (pickSegment + 1) / segments;
        double f = (bestdt - startSegment) / (endSegment - startSegment);
        pickSegment *= 3;
        
        System.out.println(getTangent(pickSegment, f));
        
        if (f > (1f / 3f)) {
            if (f > (2f / 3f)) {
                return 2 + pickSegment;
            } else {
                return 1 + pickSegment;
            }
        } else {
            return 0 + pickSegment;
        }
    }

    /**
     * cubic Bezier equation (1–t)^3*P0+3*(1–t)^2*t*P1+3*(1–t)*t^2*P2+t^3*P3 get
     * the current point position
     *
     * @param t the fraction of the way through the Bezier curve
     * @return the position of current point
     */
    public Vector getPoint(double t) {
        if (t >= 1) {
            return vt.get(vt.size() - 1);
        }
        int segments = vt.size() / 3;
        int pickSegment = (int) (segments * t);
        double startSegment = (double) pickSegment / segments;
        double endSegment = (double) (pickSegment + 1) / segments;
        double f = (t - startSegment) / (endSegment - startSegment);
        pickSegment *= 3;
        return getPoint(pickSegment, f);
    }

    /**
     * cubic Bezier equation (1–t)^3*P0+3*(1–t)^2*t*P1+3*(1–t)*t^2*P2+t^3*P3 get
     * the current point position
     *
     * @param t the fraction of the way through the Bezier curve
     * @return the position of current point
     */
    public Vector getPoint(int i, double t) {
        return vt.get(i).mult((1 - t) * (1 - t) * (1 - t))
                .added(vt.get(i + 1).mult(3 * (1 - t) * (1 - t) * t))
                .added(vt.get(i + 2).mult(3 * (1 - t) * t * t))
                .added(vt.get(i + 3).mult(t * t * t));
    }

    /**
     * differential coefficient of cubic Bezier equation get the tangent line
     * direction vector
     *
     * @return the tangent line direction vector
     */
    Vector getTangent(double t) {
        int segments = vt.size() / 3;
        int pickSegment = (int) (segments * t);
        double startSegment = (double) pickSegment / segments;
        double endSegment = (double) (pickSegment + 1) / segments;
        double f = (t - startSegment) / (endSegment - startSegment);
        return getTangent(pickSegment, t);
    }

    /**
     * differential coefficient of cubic Bezier equation get the tangent line
     * direction vector
     *
     * @return the tangent line direction vector
     */
    Vector getTangent(int i, double t) {
        Vector a = vt.get(i + 1).minus(vt.get(i)).mult(3 * (1 - t) * (1 - t));
        Vector b = vt.get(i + 2).minus(vt.get(i + 1)).mult(6 * (1 - t) * t);
        Vector c = (vt.get(i + 3).minus(vt.get(i + 2))).mult(3 * t * t);
        return a.added(b).added(c);
    }

    /**
     * get the xyz-plane coordinate matrix of current point
     *
     * @return a matrix formed by xaxis, yaxis, zaxis and point vector
     */
    public Transformation getMatrix(double step) {
        Vector point = getPoint(step);
        if (point.equals(Vector.ZERO)) {
            point = new Vector(-0.01, 0.01, 0);
        }

        Vector zaxis = getTangent(step);
        zaxis = zaxis.normalized();

        Vector yaxis, xaxis;
        if (zaxis.equals(new Vector(0, 0, 1))) {
            yaxis = new Vector(0, 1, 0);
        } else if (zaxis.equals(new Vector(0, 0, -1))) {
            yaxis = new Vector(0, -1, 0);
        } else {
            yaxis = point.cross(zaxis);
            yaxis = yaxis.normalized();
        }

        if (zaxis.equals(new Vector(0, 0, 1))) {
            xaxis = new Vector(1, 0, 0);
        } else if (zaxis.equals(new Vector(0, 0, -1))) {
            xaxis = new Vector(-1, 0, 0);
        } else {
            xaxis = yaxis.cross(zaxis);
            xaxis = xaxis.normalized();
        }

        return new Transformation(xaxis, yaxis, zaxis, point);
    }

    private double calculateDistance(Vector p1, Vector p2) {
        Vector delta = p2.minus(p1);
        return Math.abs(Math.sqrt((delta.x * delta.x) + (delta.y * delta.y) + (delta.z * delta.z)));
    }

    public void setPoint(int index, Vector point) {
        if (index < vt.size()) {
            vt.set(index, point);
        }
    }
}
