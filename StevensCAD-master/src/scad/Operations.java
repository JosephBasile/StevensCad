/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scad;
import primitives.Shape;
/**
 *
 * @author dkruger
 */
public interface Operations {
    abstract Shape union(Shape a, Shape b);
    abstract Shape intersection(Shape a, Shape b);
    abstract Shape difference(Shape a, Shape b);
    abstract Shape convexHull(Shape a, Shape b);
    abstract Shape multiunion(Shape... shapes);
    abstract Shape multiintersection(Shape... shapes);
    abstract Shape multiDifference(Shape... shapes);
    abstract Shape mirror(Shape shape, boolean mirX, boolean mirY,boolean mirZ);
    abstract Shape mirror(Shape shape, Vector v);
    abstract Shape scale(Shape shape, double x, double y, double z);
}
