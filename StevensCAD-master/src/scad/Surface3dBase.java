/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scad;
import primitives.AnalyticalShape3d;

/**
 *
 * @author dkruger
 */
public abstract class Surface3dBase extends AnalyticalShape3d implements Surface3d {
    public static int defaultURes = 30;
    public static int defaultVRes = 30;
    public static double defaultx = 0;
    public static double defaulty = 0;
    public static double defaultz = 0;
    
    protected int u_res, v_res;
    protected double min_u, min_v, max_u, max_v;

    public Surface3dBase() {}
    public Surface3dBase(int uRes, int vRes, double uMin, double uMax, double vMin, double vMax) {
        u_res = uRes; v_res = vRes;
        min_u = uMin; max_u = uMax; // TODO: rationalize these names, start with the variable, min/max goes at the end.  COMMON NAMING CONVENTIONS ARE IMPORTANT!
        min_v = vMin; max_v = vMax;
    }
    public abstract void initializeSurface();
    public void rebuild() {
        vertices.clear();
        poly.clear();
        initializeSurface();
    }
    
    public int getURes() { return u_res; }
    public void setURes(String res) { 
        u_res = Integer.valueOf(res); 
    }
    public int getVRes() { return v_res; }
    public void setVRes(String res) { 
        v_res = Integer.valueOf(res); 
    }
    
    public void setMinU(String u) {
        min_u = Double.valueOf(u);
        rebuild();
    }
    

    public void setMaxU(String u) {
        max_u = Double.valueOf(u);
        rebuild();
    }
    
 
    public void setMinV(String v) {
        min_v = Double.valueOf(v);
        rebuild();
    }
    

    public void setMaxV(String v) {
        max_v = Double.valueOf(v);
        rebuild();
    }
    
    public double getMinU() {
        return min_u;
    }
    
    
    public double getMaxU() {
        return max_u;
    }
    
   
    public double getMinV() {
        return min_v;
    }
    
    
    public double getMaxV() {
        return max_v;
    }

    private static final double DEG2RAD = Math.PI/180;
    private static final double RAD2DEG = 180 / Math.PI;
    public void setMinUAsDegrees(String u) {
        min_u = Double.valueOf(u) * DEG2RAD;
        rebuild();
    }
    

    public void setMaxUAsDegrees(String u) {
        max_u = Double.valueOf(u) * DEG2RAD;
        rebuild();
    }
    
 
    public void setMinVAsDegrees(String v) {
        min_v = Double.valueOf(v) * DEG2RAD;
        rebuild();
    }
    

    public void setMaxVAsDegrees(String v) {
        max_v = Double.valueOf(v) * DEG2RAD;
        rebuild();
    }

    public double getMinUAsDegrees() {
        return min_u * RAD2DEG;
    }

}
