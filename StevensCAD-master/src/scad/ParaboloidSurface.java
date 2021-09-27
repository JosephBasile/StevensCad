package scad;

/**
 * Resources: http://mathworld.wolfram.com/Paraboloid.html
 * @author Alessandro
 */

import static java.lang.Math.*;
import java.util.ArrayList;
import primitives.AnalyticalShape3d;
import viewer.CADEditor;

public class ParaboloidSurface  extends AnalyticalShape3d implements Surface3d{
    
    int  u_res,v_res;
    double radius,height,min_u,min_v,max_u,max_v;
    ArrayList<Vector>  verts;
    
    public ParaboloidSurface(double radius, double height,int u_res,int v_res){
        this.radius = radius;
        this.max_u = height;
        this.max_v = 2 * PI;
        this.u_res = u_res;
        this.v_res = v_res;
        this.min_u = 0;
        this.min_v = 0;
        this.verts = new ArrayList<>(v_res * u_res);
    }
    
    public void initializeParaboloidSurface(){
        double u = min_u, v = min_v;
        double t = max_u / u_res;
        double t1 = max_v / v_res;
        for(int i = 0; i <= u_res - 1; i++, u+=t){
            for(int j = 0; j < v_res; j++, v+=t1){
                verts.add( new Vector(radius*sqrt(u/max_u)*cos(v), radius*sqrt(u/max_u)*sin(v), u));
            }
        }
    }
    
    private void addSquare(int a, int b, int c, int d) {
       poly.facets.add(new Facet(verts.get(a), verts.get(b), verts.get(c), verts.get(d)));
    }
    
    private void drawParaboloid() {
        for (int i = 0; i < verts.size() - v_res; i++)
            addSquare(i ,
                    i + v_res,
                    (i % v_res == v_res - 1 ? -v_res : 0) + i + v_res + 1,
                    (i % v_res == v_res - 1 ? -v_res : 0) + i + 1);
    }
   
    
    public void setMinU(double u) {
        this.min_u =u;
    }
    
   
    public void setMaxU(double u) {
        this.max_u = u;
    }
    
    
    public void setMinV(double v) {
        this.min_v = v;
    }
    
    
    public void setMaxV(double v) {
        this.max_v = v;
    }
    
    
    public double getMinU() {
        return min_u;
    }
    
   
    public double getMaxU() {
        return max_u;
    }
    
    
    public double getMinV() {
        return min_v;    }
    
    
    public double getMaxV() {
        return max_v;    }
    
   
    public Vector mapSurface(int u, int v) {
        return verts.get(this.v_res*(u) + v);     
    }
    
    
    public boolean greaterThan(Vector v) {
        for(int i = 0; i < verts.size(); i++)
            if (v.magnitude() < verts.get(i).magnitude() )
                return false;
        return true;
    }
    
    public static void main(String[] args){
        ParaboloidSurface e = new  ParaboloidSurface(10,20,40,40);
        ParaboloidSurface e1 = new  ParaboloidSurface(10,20,40,40);
        int v;
        e.initializeParaboloidSurface();
        e1.initializeParaboloidSurface();
        Vector b = new Vector(0,0,0);
//        Vector fromCenter = new Vector(0,0,0);
//        for(int u = 0; u < 40; u++){
//            for(v = 0; v < 40; v++){
//                fromCenter = e.mapSurface(u, v).minus(b);
//                Vector temp = e.mapSurface(u, v).plus(fromCenter.normalized().times(5));
//                e.mapSurface(u, v).setCoordinates(temp.x, temp.y, temp.z);
//            }
//        }
        e.drawParaboloid();
        e1.drawParaboloid();
        CADEditor view = CADEditor.openWindow();
        view.add(e);
        view.add(e1);
    }
}
