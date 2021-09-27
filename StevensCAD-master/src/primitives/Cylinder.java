package primitives;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import scad.Vector;
import static java.lang.Math.*;
import java.util.ArrayList;
import scad.Edge;
import scad.Facet;
import scad.Polyhedron;
import scad.Side;
import transformation.Transformation;
import static scad.Vector.EPSILON;
import viewer.CADEditor;

public class Cylinder extends AnalyticalShape3d {
    private double r, rMin, h;
    private int res;
    private Vector center;

    /**
     * static variables starting with default will be automatically parsed by the
     * bean editor and applied as defaults to attributes of the same base name.
     * For example, the field to set attribute Radius will be populated by default
     * with defaultRadius
     */
    public static double defaultRadius = 15.0;
    public static double defaultHeight = 30.0;
    public static int defaultRes = 30;
    
    public Cylinder() {
        this(defaultRadius, defaultHeight, defaultRes);
    }
    
    public Cylinder(double r, double h, int res) {
        this(r, h, res, new Vector(0, 0, -h/2));
    }
    
    public Cylinder(Vector p1, Vector p2, double r){
        this(p1,p2,r,defaultRes);
    }
    
        //TODO add this constructor for cylinder
     public Cylinder(Vector p1, Vector p2, double r, int res){
        
    }
     
     //TODO make constructor for oval cylinders
     public Cylinder(Vector p1, Vector p2, double rx, double ry, int res){
         
     }
    
    public Cylinder(double r, double h, int res, Vector center) {
        poly = new ConvexPoly(res * 3);
        this.r = r;
        this.h = h;
        this.res = res;
        t = new Transformation();
        t.translate(center);
//        initCylinder(r, h, res, center);
        initCylinder(r, h, res);
    }

    public double getRadius() { return r; }
    public void setRadius(String sr) throws IOException {
        poly = new ConvexPoly(res * 3);
        double r = Double.valueOf(sr);
        this.r = r; 
        initCylinder(r, h, res);
    }
    
    public double getHeight() { return h; }
    public void setHeight(String sh) throws IOException {
        poly = new ConvexPoly(res * 3);
        double h = Double.valueOf(sh);
        this.h = h; 
        initCylinder(r, h, res);
    }
    
    public int getRes() { return res; }

    /**
     *
     * @param sres
     */
    public void setRes(String sres) throws IOException {
        poly = new ConvexPoly(res * 3);
        int res = Integer.valueOf(sres);
        this.res = res; 
        initCylinder(r, h, res);
    }
    

    // need to modify to creat new object at certain center.
    private void initCylinderOld(double r, double h, int res, Vector center) {
        
        this.unHistory = new ArrayList<>();
        this.h = h;
        this.r = r;
        this.center = center;
        
        double chord = 2 * r * sin(Math.PI / res);
        rMin = Math.sqrt(r * r - chord * chord / 4);
        poly.vertices = new ArrayList<>();
        
        Facet bottom = new Facet(res), top = new Facet(res);
        poly.facets.add(top);
        poly.facets.add(bottom);
        top.add(new Vector(r, 0, h));
        bottom.add(new Vector(r, 0, 0));
        poly.vertices.add(top.get(0));
        poly.vertices.add(bottom.get(0));

        addBottomTopSide(
                top.get(0),
                bottom.get(0),
                new Vector(r * cos(2 * PI / res), r * sin(2 * PI / res), h),
                new Vector(r * cos(2 * PI / res), r * sin(2 * PI / res), 0), top, bottom);

        for (double theta = 4 * PI / res; theta < 2 * PI - EPSILON; theta += 2 * PI / res) {
            double x = r * cos(theta), y = r * sin(theta);

            addBottomTopSide(
                    poly.facets.get(poly.facets.size() - 1).get(3),
                    poly.facets.get(poly.facets.size() - 1).get(2),
                    new Vector(x, y, h),
                    new Vector(x, y, 0), top, bottom);
        }
        poly.facets.add(new Facet(poly.facets.get(poly.facets.size() - 1).get(3), 
                        poly.facets.get(poly.facets.size() - 1).get(2),
                        poly.facets.get(2).get(1), 
                        poly.facets.get(2).get(0)));
        poly.facets.set(1, poly.facets.get(1).flip());
    }
    
    private final void addBottomTopSide(Vector topLeft, Vector bottomLeft, Vector topRight, Vector bottomRight, Facet top, Facet bottom) {
        poly.vertices.add(topRight);
        poly.vertices.add(bottomRight);
        top.add(topRight);
        bottom.add(bottomRight);
        poly.add(new Facet(topLeft, bottomLeft, bottomRight, topRight));
    }
    
    private void initCylinder(double r, double h, int res){
        this.h = h;
        this.r = r;
        
        double chord = 2 * r * sin(Math.PI / res);
        rMin = Math.sqrt(r * r - chord * chord / 4);
        
        ArrayList<Vector> topVerts = new ArrayList<>();
        ArrayList<Vector> bottomVerts = new ArrayList<>();
        
        Vector topVertice = new Vector(0, 0, h);
        Vector bottomVertice = new Vector(0, 0, 0);
        
        topVerts.add(topVertice);
        bottomVerts.add(bottomVertice);
        
        for (double theta = 0; theta < 2 * PI - EPSILON; theta += 2 * PI / res) {
            double x = r * cos(theta), y = r * sin(theta);
            topVerts.add(new Vector(x, y, h));
            bottomVerts.add(new Vector(x, y, 0));
        }
        
        for (int i = 0; i < topVerts.size(); ++i){
            topVerts.set(i, t.transform(topVerts.get(i)));
            bottomVerts.set(i, t.transform(bottomVerts.get(i)));
        }
        topVertice = topVerts.get(0);
        bottomVertice = bottomVerts.get(0);
        this.center = bottomVertice; // Is center necessary?
        
        for (int i = 2; i < topVerts.size(); ++i){
            poly.facets.add(new Facet(topVertice, topVerts.get(i-1), topVerts.get(i)));
            poly.facets.add(new Facet(bottomVertice, bottomVerts.get(i), bottomVerts.get(i-1)));
            poly.facets.add(new Facet(topVerts.get(i-1), bottomVerts.get(i-1), bottomVerts.get(i), topVerts.get(i)));
        }
        poly.facets.add(new Facet(topVertice, topVerts.get(topVerts.size() - 1), topVerts.get(1)));
        poly.facets.add(new Facet(bottomVertice, bottomVerts.get(1), bottomVerts.get(bottomVerts.size() - 1)));
        poly.facets.add(new Facet(topVerts.get(topVerts.size() - 1), bottomVerts.get(bottomVerts.size() - 1), bottomVerts.get(1), topVerts.get(1)));
        
        poly.vertices = topVerts;
        poly.vertices.addAll(bottomVerts);
    }


    private ArrayList<Transformation> unHistory;

    
    public Cylinder transform(Transformation t) {
        unHistory.add(t.inverse());
        poly.transform(t);
        return this;
    }
    
    public boolean contains(Vector v) {
        Vector temp = null; //TODO tinv.transform(v);
        return temp.x*temp.x + temp.y*temp.y < 1 && temp.z >= 0 && temp.z <= 1;
        
    }
    
    public Side definitely(Vector v) {
        v = v.clone();
        for (int i = unHistory.size() - 1; i >= 0; i--)
            unHistory.get(i).transform(v);

        if( v.z > h + EPSILON || v.z < -EPSILON) 
            return Side.OUT;
        
        double vd = v.x * v.x + v.y * v.y;
        
        if (vd > r * r + EPSILON) return Side.OUT;

        if (vd < rMin*rMin - EPSILON && v.z > EPSILON && v.z < h - EPSILON)
            return Side.IN;

        return Side.SURFACE;
    }
    
    
    public boolean definitelyIn(Vector v) {
        v = v.clone();
        for (int i = unHistory.size() - 1; i >= 0; i--)
            unHistory.get(i).transform(v);

        double vd = v.x * v.x + v.y * v.y;

        if (vd < rMin*rMin - EPSILON && v.z > EPSILON && v.z < h - EPSILON)
            return true;

        return false;
    }

    private Edge s =  new Edge(new Vector(0, 0, 0), new Vector(0, 0, 0));
    
    protected boolean definitelyOut(Edge e) {
        s.setTo(e);
        Vector a = s.a, b = s.b;
        
        for (int i = unHistory.size() - 1; i >= 0; i--) {
            unHistory.get(i).transform(a);
            unHistory.get(i).transform(b);
        }

        if (a.z > h + EPSILON && b.z > h + EPSILON
                || a.z < -EPSILON && b.z < -EPSILON) return true;

        double sDen = (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);

        if (Math.abs(sDen) < EPSILON)
            if (a.x * a.x + a.y * a.y < r * r + EPSILON) return false;
            else return true;

        double s = -(a.x * (b.x - a.x) + a.y * (b.y - a.y)) / sDen,
               t = (a.z + (b.z - a.z) * s) / h;

        if (t < 0) t = 0;
        else if (t > 1) t = 1;
        if (s < 0) s = 0;
        else if (s > 1) s = 1;

        Edge edge = new Edge(a, b);

        
        if (edge.at(s).z < 0) {
            s = -a.z / (b.z - a.z);
            t = 0;
        }

        else if (edge.at(s).z > h) {
            s = (h - a.z) / (b.z - a.z);
            t = 1;
        }

        return edge.at(s).minus(new Vector(0, 0, t*h)).magnitudeSq() > r*r + EPSILON;
    }
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        
        oos.writeDouble(center.x);
        oos.writeDouble(center.y);
        oos.writeDouble(center.z);
        
        oos.writeDouble(r);
        oos.writeDouble(h);
     
        oos.writeDouble(res);
    }
    
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        double x, y, z;
        x = ois.readDouble();
        y = ois.readDouble();
        z = ois.readDouble();
        center = new Vector(x, y, z);
        
        r = ois.readDouble();
        h = ois.readDouble();
        res = ois.readInt();
        
        
        //initCylinder(r, h, res, center);
    }
    public static void main(String[] arg) {
        Cylinder s = new Cylinder(10, 10, 100, new Vector(0, 0, 0));
        Cylinder s2 = new Cylinder(10, 10, 100, new Vector(20, 20, 20));
//        System.out.println(s.poly.facets.size());
//        for(Facet v : s.poly.facets) {
//            System.out.println(v);
//        }
        CADEditor view = CADEditor.openWindow();
        view.add(s);
        view.add(s2);
    }
}
