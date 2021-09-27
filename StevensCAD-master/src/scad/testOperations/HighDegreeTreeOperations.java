/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scad.testOperations;

import primitives.Shape;
import scad.Operations;
import scad.Vector;

/**
 *
 * Implement fast approximate unions and intersections
 * @author Dov Kruger
 */
public class HighDegreeTreeOperations implements Operations{

    @Override
    public Shape union(Shape a, Shape b) {
        return a;
    }

    @Override
    public Shape intersection(Shape a, Shape b) {
        return a;
    }

    @Override
    public Shape difference(Shape a, Shape b) {
        return a;
    }

    @Override
    public Shape convexHull(Shape a, Shape b) {
        return a;
    }

    @Override
    public Shape multiunion(Shape... shapes) {
        for (int i = 0; i < shapes.length; i++)
            for (int j = i+1; j < shapes.length; j++) {
               // if (shapes[i].mightIntersect(shapes[j]))
                    
                    
            }
        return null;
    }

    @Override
    public Shape multiintersection(Shape... shapes) {
        return null;
    }

    /**
     *
     * @param shapes
     * @return
     */
    @Override
    public Shape multiDifference(Shape... shapes) {
        return null;
    }
    
    @Override
     public Shape mirror(Shape shape, boolean mirX, boolean mirY,boolean mirZ) {
        return shape;
    }
     
    @Override
      public Shape mirror(Shape shape, Vector v) {
        return shape;
    }

    @Override
    public Shape scale(Shape shape, double x, double y, double z) {
        return shape;
    }   

//    @Override
//    public Shape multiDifference(Shape[] shapes) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
    
}
