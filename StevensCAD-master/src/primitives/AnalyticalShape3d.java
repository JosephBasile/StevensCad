/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import java.util.ArrayList;
import scad.Facet;
import scad.Triangle;
import scad.Vector;
import transformation.Transformation;

/**
 *
 * @author yifan
 */
public abstract class AnalyticalShape3d extends Shape3d {
    public ConvexPoly poly;
    public static double defaultx = 0;
    public static double defaulty = 0;
    public static double defaultz = 0;
    /*
        for analytical shapes, the convex polygon has the actual vertices.  The base vertex list is unused.
    //TODO: is this necessary?
    */
    public ArrayList<Vector> getVertices() { return poly.getVertices(); }
    public AnalyticalShape3d(int size) {
        poly = new ConvexPoly(size);
    }
    public AnalyticalShape3d() {
        poly = new ConvexPoly(0);
    }
    public ArrayList<Triangle> triangles() {
        return poly.triangles();
    }
    public int size() {
        return poly.size();
    }
    public void applyTransform() {
        for(Vector v : poly.vertices) {
             t.transform(v);
        }
    }
}
