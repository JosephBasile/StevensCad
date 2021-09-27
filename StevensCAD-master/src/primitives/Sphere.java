package primitives;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import scad.Vector;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;
import scad.Edge;
import scad.Facet;
import scad.Side;
import scad.Vector;
import transformation.Transformation;
import static scad.Vector.EPSILON;
import viewer.CADEditor;

public class Sphere extends AnalyticalShape3d {

    public double r, rMin;
    public int longRes, latRes;
    public static double defaultRadius = 150;
    public static int defaultLatRes = 30;
    public static int defaultLongRes = 30;

    public Sphere() {
        this(defaultRadius, defaultLatRes, defaultLongRes);
    }

    public Sphere(double radius, int res) {
        this(radius, res, res);
    }

    public Sphere(double radius, int longRes, int latRes) {
        poly = new ConvexPoly(longRes * latRes);
        t = new Transformation();
        initSphere(radius, longRes, latRes);
    }

    public Sphere(double radius, int longRes, int latRes, Vector movement) {
        poly = new ConvexPoly(longRes * latRes);
        t = new Transformation();
        t.translate(movement);
        initSphere(radius, longRes, latRes);
    }

    public double getRadius() {
        return r;
    }

    public void setRadius(double r) {
        poly = new ConvexPoly(longRes * latRes);
        this.r = r;
        initSphere(r, longRes, latRes);
    }

    public void setRadius(String sr) throws IOException {
        poly = new ConvexPoly(longRes * latRes);
        double r = Double.valueOf(sr);
        this.r = r;
        initSphere(r, longRes, latRes);
    }

    public int getLongRes() {
        return longRes;
    }

    public void setLongRes(String slongRes) throws IOException {
        poly = new ConvexPoly(longRes * latRes);
        int longRes = Integer.valueOf(slongRes);
        this.longRes = longRes;
        initSphere(r, longRes, latRes);
    }

    public int getLatRes() {
        return latRes;
    }

    public void setLatRes(String slatRes) throws IOException {
        poly = new ConvexPoly(longRes * latRes);
        int latRes = Integer.valueOf(slatRes);
        this.latRes = latRes;
        initSphere(r, longRes, latRes);
    }

    private void initSphere(double radius, int longRes, int latRes) {
        this.longRes = longRes;
        this.latRes = latRes;
        this.unHistory = new ArrayList<>();
        this.r = radius;
        ArrayList<Vector> verts = new ArrayList<>(longRes * latRes + 2);

        double chord = 2 * r * sin(PI / min(longRes, latRes));
        rMin = sqrt(r * r - chord * chord / 4);
        latRes /= 2;

        // first compute a broad band around the sphere excluding the poles
        final double dphi = PI / latRes; // vertically only +90 to -90 degrees
        double phi = -PI / 2 + dphi;

        for (int j = 0; j < latRes - 1; ++j, phi += dphi) {
            double z = radius * sin(phi);
            double r2 = radius * cos(phi);

            double theta = 0;
            final double dtheta = 2 * PI / longRes;
            for (int i = 0; i < longRes; ++i, theta += dtheta) {
                double x = r2 * cos(theta), y = r2 * sin(theta);
                Vector vect = new Vector(x, y, z);
                t.transform(vect);
                verts.add(vect); // first compute all points
            }
        }

        // then create quads with all the points created
        for (int i = 0; i < verts.size() - longRes; i++) {
            addSquare(i + longRes,
                    i,
                    (i % longRes == longRes - 1
                            ? -longRes : 0) + i + 1, //bottom right
                    (i % longRes == longRes - 1
                            ? -longRes : 0) + i + longRes + 1,//top right 

                    verts);
        }

        Vector south = new Vector(0, 0, -r);
        Vector north = new Vector(0, 0, r);
        t.transform(south);
        t.transform(north);

        for (int i = 0; i < longRes - 1; i++) {
            poly.facets.add(new Facet(verts.get(i), south, verts.get(i + 1)));
            poly.facets.add(new Facet(verts.get(i + verts.size() - longRes), verts.get(i + verts.size() - longRes + 1), north));
        }
        poly.facets.add(new Facet(verts.get(longRes - 1), south, verts.get(0)));
        poly.facets.add(new Facet(verts.get(verts.size() - longRes), north, verts.get(verts.size() - 1)));

        poly.vertices = verts;
        poly.vertices.add(north);
        poly.vertices.add(south);
    }

    private void addSquare(int a, int b, int c, int d, List<Vector> list) {
        poly.facets.add(new Facet(list.get(a), list.get(b), list.get(c), list.get(d)));
    }

    private ArrayList<Transformation> unHistory;

    public Sphere transform(Transformation t) {
        unHistory.add(t.inverse());
        poly.transform(t);
        return this;
    }

    public Side definitely(Vector v) {
        v = v.clone();
        for (int i = unHistory.size() - 1; i >= 0; i--) {
            unHistory.get(i).transform(v);
        }

        double vR = v.magnitudeSq();
        if (vR > r * r + EPSILON) {
            return Side.OUT;
        }
        if (vR < rMin * rMin - EPSILON) {
            return Side.IN;
        }
        return Side.SURFACE;
    }

    private Edge edge = new Edge(new Vector(0, 0, 0), new Vector(0, 0, 0));

    protected boolean definitelyOut(Edge e) {
        edge.setTo(e);
        Vector a = edge.a, b = edge.b;
        for (int i = unHistory.size() - 1; i >= 0; i--) {
            unHistory.get(i).transform(a);
            unHistory.get(i).transform(b);
        }
        double t = -a.dot(b.minus(a)) / b.minus(a).magnitudeSq();
        if (t < 0) {
            t = 0;
        }
        if (t > 1) {
            t = 1;
        }
        return edge.at(t).magnitudeSq() > r * r + EPSILON;
    }

    public boolean definitelyIn(Vector v) {
        v = v.clone();
        for (int i = unHistory.size() - 1; i >= 0; i--) {
            unHistory.get(i).transform(v);
        }
        return v.magnitudeSq() < rMin * rMin - EPSILON;
    }

    public Sphere clone() {
        Sphere clone = (Sphere) poly.facets.clone();
        clone.r = r;
        clone.rMin = rMin;
        clone.unHistory = new ArrayList<>(unHistory);
        return clone;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeDouble(r);
        oos.writeInt(longRes);
        oos.writeInt(latRes);

    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        r = ois.readDouble();
        longRes = ois.readInt();
        latRes = ois.readInt();
        initSphere(r, longRes, latRes);

        // do not implement yet read/write transformation
    }
}
