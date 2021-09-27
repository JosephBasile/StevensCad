package primitives;

/**
 * Resources: http://mathworld.wolfram.com/Sphere.html
 * @author Alessandro
 */

import static java.lang.Math.*;
import primitives.ConvexPoly;
import scad.Facet;
import scad.Surface3dBase;
import scad.Vector;
import transformation.Transformation;
import viewer.CADEditor;

public class SphereSurface  extends Surface3dBase {
    
    double radius,tickness;
    Vector center;
    public static double defaultMinV, defaultMinU, defaultMaxV, defaultMaxU, defaultRadius;
    public static Vector default_center;
    static {
        defaultRadius = 20;
        defaultMinV = 0;
        defaultMinU = 0;
        defaultMaxV = 2*PI;
        defaultMaxU = PI;
        default_center = new Vector(defaultx,defaulty,defaultz);
    }
    public SphereSurface() {
        this(defaultRadius, defaultURes, defaultVRes);
    }
    
    public SphereSurface(double radius, int u_res, int v_res){
        poly = new ConvexPoly(u_res * v_res);
        t = new Transformation();
        this.radius = radius;   
        this.u_res = u_res;
        this.v_res = v_res;
        this.max_u = defaultMaxU;
        this.max_v = defaultMaxV;
        this.center = default_center;
        
        initializeSurface();
    }
    
    public SphereSurface(double radius, int u_res, int v_res, Vector movement) {
        this(radius,u_res,v_res);
        this.max_u = defaultMaxU;
        this.max_v = defaultMaxV;
        this.center = movement;
        poly = new ConvexPoly(u_res * v_res);
        t = new Transformation();
        
        initializeSurface();
    }
    
    public void initializeSurface(){
        vertices.clear();
        System.out.println(radius + "," + min_u + "," + min_v + "," + max_u + "," + max_v);
        double u = min_u, v = min_v;
        double phi = (max_u-min_u) / u_res;
        double theta = (max_v-min_v) / v_res;
        outer: for(int i = 0; i <= u_res; i++, u+=phi){
            v = min_v;
            for(int j = 0; j < v_res; j++, v+=theta){
                vertices.add(new Vector(center.x + radius*cos(v)*sin(u),center.y + radius*sin(v)*sin(u),center.z + radius*cos(u)));
                if(i==0 || i==u_res)
                    continue outer;
            }
        }
        drawSphere();
    }
    
    private void addSquare(int a, int b, int c, int d) {
        poly.add(new Facet(vertices.get(a), vertices.get(b), vertices.get(c), vertices.get(d)));
    }
    private void addTriangle(int a, int b, int c) {
        poly.add(new Facet(vertices.get(a), vertices.get(b), vertices.get(c)));
    }
    
    public void drawSphere() {
        /*To create the north pole of the sphere */
        if(min_u == 0.0)
            for (int i = 1; i <= v_res ; i++)
                addTriangle(i,(i == v_res  ? 1 : i+1),0);
         /*To create the south pole of the sphere */ 
        if(max_u == PI)
            for (int i = 0; i < v_res ; i++)
                addTriangle((i == v_res - 1   ? vertices.size() - v_res - 1  : i + vertices.size() - v_res ) ,i + vertices.size() - v_res -1 ,   vertices.size()-1);
          /*To create all the other faces of the sphere */
        for (int i = 1; i < vertices.size() - v_res -1  ; i++){
            addSquare( i ,i + v_res,   (i % v_res == 0 ? -v_res : 0) + i + v_res + 1,(i % v_res == 0 ? -v_res : 0) + i + 1);
        }
    }
    public double getRadius() {
        return radius;
    }    
   
    public Vector mapSurface(int u, int v) {
        return vertices.get(this.v_res*(u)+v);
    }
    
    public void setRadius(String r) {
        radius = Double.valueOf(r);
        rebuild();
    }
        
    @Override
    public boolean greaterThan(Vector v) {
        return v.magnitudeSq() > radius*radius;
    }
    
    public static void main(String[] args){
        SphereSurface e = new  SphereSurface(50,50,50,new Vector(0, 0, 0));
        e.setMaxV("3.14");
        e.setMinV("1.56");
        e.setMinU("1");
        e.setMaxU("2.98");

        CADEditor view = CADEditor.openWindow();
        view.add(e);
    } 
} 
