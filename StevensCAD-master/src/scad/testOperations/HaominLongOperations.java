/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scad.testOperations;

import primitives.Shape;
import scad.Operations;
import scad.Vector;
import customShape.dtriangles_operations.*;
import primitives.AnalyticalShape3d;
import scad.Polyhedron;
import java.util.ArrayList;

/**
 *
 * @author Haomin, Long
 */
public class HaominLongOperations implements Operations{

    @Override
    public Shape union(Shape a, Shape b) {
        dUnion du = new dUnion();
        du.doUnion((AnalyticalShape3d)a, (AnalyticalShape3d)b);
//        ArrayList<Polyhedron> res = du.getFinished();
        Polyhedron res = du.getFinishedSingle();
        return (Shape)res;
    }

    @Override
    public Shape intersection(Shape a, Shape b) {
        dIntersect di = new dIntersect();
        di.doIntersect((AnalyticalShape3d)a, (AnalyticalShape3d)b);
        Polyhedron res = di.getFinishedSingle();
        return (Shape)res;
    }

    @Override
    public Shape difference(Shape a, Shape b) {
        dDiffer dd = new dDiffer();
        dd.doDiffer((AnalyticalShape3d)a, (AnalyticalShape3d)b);
        Polyhedron res = dd.getFinishedSingle();
        return (Shape)res;
    }

    @Override
    public Shape convexHull(Shape a, Shape b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Shape multiunion(Shape... shapes) {
        dUnion du = new dUnion();
        ArrayList<AnalyticalShape3d> ana = new ArrayList<>();
        for (int i = 0; i < shapes.length; ++i){
            ana.add((AnalyticalShape3d)shapes[i]);
        }
        du.doUnion(ana);
        Polyhedron res = du.getFinishedSingle();
        return (Shape)res;
    }

    @Override
    public Shape multiintersection(Shape... shapes) {
        dIntersect di = new dIntersect();
        ArrayList<AnalyticalShape3d> ana = new ArrayList<>();
        for (int i = 0; i < shapes.length; ++i){
            ana.add((AnalyticalShape3d)shapes[i]);
        }
        di.doIntersect(ana);
        Polyhedron res = di.getFinishedSingle();
        return (Shape)res;
    }

    @Override
    public Shape multiDifference(Shape... shapes) {
        // first one is the one need keep
        dDiffer dd = new dDiffer();
        AnalyticalShape3d needKeep = (AnalyticalShape3d)shapes[0];
        ArrayList<AnalyticalShape3d> ana = new ArrayList<>();
        for (int i = 1; i < shapes.length; ++i){
            ana.add((AnalyticalShape3d)shapes[i]);
        }
        dd.doDiffer(needKeep, ana);
        Polyhedron res = dd.getFinishedSingle();
        return (Shape)res;
    }
    
    @Override
     public Shape mirror(Shape shape, boolean mirX, boolean mirY,boolean mirZ) {
        throw new UnsupportedOperationException();
    }
     
    @Override
      public Shape mirror(Shape shape, Vector v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Shape scale(Shape shape, double x, double y, double z) {
        throw new UnsupportedOperationException();
    }   

    
    
}
   

