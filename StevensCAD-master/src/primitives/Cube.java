package primitives;

import java.io.*;
import scad.Vector;
import transformation.*;
import scad.Facet;
import scad.Polyhedron;
import viewer.CADEditor;

/**
 *
 * @author Dov Kruger
 * modified by yifan & siyu
 */
public class Cube extends AnalyticalShape3d {
    
    public double size;
    public boolean center;

    public static double defaultSize = 30.0;
  
    public Cube() {
        this(defaultSize);
    }
    
    public Cube(Vector v1, Vector v2) {
        poly = new ConvexPoly(6);
        t = new Transformation();
        initCube(v1, v2);
    }
    
    //TODO make this constructor
    public Cube(double length, double width, double height){
        
    }

    public Cube(double size) {
        this.size = size;
        poly = new ConvexPoly(6);
        t = new Transformation();
        initCube(this.size);
    }
    
    public Cube(double size, Vector movement){
        this.size = size;
        poly = new ConvexPoly(6);
        t = new Transformation();
        t.translate(movement);
        initCube(this.size);
    }
    
    private void initCube(double size) {
        initCube(new Vector(size * .5, size * .5, size * .5), 
                 new Vector(-size * .5, -size * .5, -size * .5));
    }

    private void initCube(Vector v1, Vector v2) {
        Vector fbl = v1,  /*Front Bottom Right*/ fbr = new Vector(v2.x, v1.y, v1.z),
               ftl = new Vector(v1.x, v2.y, v1.z), ftr = new Vector(v2.x, v2.y, v1.z),
               bbl = new Vector(v1.x, v1.y, v2.z), bbr = new Vector(v2.x, v1.y, v2.z),
               btl = new Vector(v1.x, v2.y, v2.z), btr = v2;
        t.transform(fbl);
        t.transform(fbr);
        t.transform(ftl);
        t.transform(ftr);
        t.transform(bbl);
        t.transform(bbr);
        t.transform(btl);
        t.transform(btr);
        poly.vertices.add(fbl);
        poly.vertices.add(fbr);
        poly.vertices.add(ftl);
        poly.vertices.add(ftr);
        poly.vertices.add(bbl);
        poly.vertices.add(bbr);
        poly.vertices.add(btl);
        poly.vertices.add(btr);
        poly.facets.add(new Facet(ftl, fbl, fbr, ftr));//front
        poly.facets.add(new Facet(ftr, fbr, bbr, btr));//right
        poly.facets.add(new Facet(fbl, bbl, bbr, fbr));//bottom
        poly.facets.add(new Facet(ftl, btl, bbl, fbl));//left
        poly.facets.add(new Facet(btl, btr, bbr, bbl));//back
        poly.facets.add(new Facet(btl, ftl, ftr, btr));//top
    }

    public double getSize() {
        return size;
    }

    public void setSize(String ssize) throws IOException {
        System.err.println("Running in setSize");
        double size = Double.valueOf(ssize);
        this.size = size;
        poly = new ConvexPoly(6); 
        initCube(size);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        //TODO: put all common code into the Shape3d base class        super.writeObject(oos, "cube");
        // oos.writeString("cube(").writeDouble(size).writeString(",center=").writeString(center ? "true" : "false").writeString(")");
        oos.writeUTF("cube");
        double[] matrix = t.getMatrix();
        oos.writeDouble(size);
        for(double e : matrix) {
            oos.writeDouble(e);
        }
//        oos.writeDouble(size);
    }
    
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        //TODO: readObject is supposed to return the object, something is wrong here!
        //ois.defaultReadObject();
        size = ois.readDouble();
        double[] transformMatrix = new double[12];
        for(int i = 0; i < 12; i++) {
            transformMatrix[i] = ois.readDouble();
        }
        t = new Transformation(transformMatrix);
//        initCube(size, t);
        initCube(size);
    }
    public static void main(String[] arg) throws FileNotFoundException, IOException {
//        Cube s = new Cube(10);
//        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("cube.ser")));
//        s.writeObject(oos);
//        System.out.println(s.poly.facets.size());
//        for(Facet v : s.poly.facets) {
//            System.out.println(v);
//        }
        
//        CADEditor view = CADEditor.openWindow();
//        view.add(new Cube(100));
//        view.add(new Cube(100, new Vector(100, 100, 100)));
    }
}
