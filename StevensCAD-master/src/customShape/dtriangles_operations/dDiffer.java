/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customShape.dtriangles_operations;

import java.util.ArrayList;
import primitives.AnalyticalShape3d;
import primitives.Cube;
import primitives.Shape;
import primitives.Sphere;
import scad.Facet;
import scad.Polyhedron;
import scad.Triangle;
import scad.Vector;
import viewer.CADEditor;

/**
 *
 * @author hemor
 */
public class dDiffer {
    
    private CADEditor edit;
    private dFunctions dFun;
    private AnalyticalShape3d origin_shape;
    private ArrayList<AnalyticalShape3d> origin_differ;
    
    private Polyhedron resPoly;

    public dDiffer() {
        init(null);
    }

    public dDiffer(CADEditor _edit) {
        init(_edit);
    }
    
    public void init(CADEditor _edit) {
        this.edit = _edit;
        this.dFun = new dFunctions();
        this.resPoly = null;
    }

    public void doDiffer(AnalyticalShape3d shapeA, AnalyticalShape3d shapeB) {
        ArrayList<AnalyticalShape3d> shapes = new ArrayList<>();
        shapes.add(shapeB);
        doDiffer(shapeA, shapes);
    }

    public void doDifferShapes(Shape A, ArrayList<Shape> shapes) {
        ArrayList<AnalyticalShape3d> tana = dFun.toAnalysticalArray(shapes);
        AnalyticalShape3d a = (AnalyticalShape3d) A;
        doDiffer(a, tana);
    }
    
    public void doDiffer(AnalyticalShape3d shapeA, ArrayList<AnalyticalShape3d> shapes){
        origin_shape = shapeA;
        origin_differ = shapes;
        
        resPoly = new Polyhedron();
        
        // union first
        dUnion du = new dUnion();
        du.doUnion(shapes);
        ArrayList<Polyhedron> union_shapes = du.getFinished();
        
        // processing
        int maxNumberOfTriangles = dFun.getMaxNumberOfTrianglesByPolyheron(union_shapes);
        maxNumberOfTriangles = Math.max(maxNumberOfTriangles, shapeA.poly.triangles().size());
        
        Polyhedron poly = dFun.doubleShapesByMaxTriangles(shapeA.poly, maxNumberOfTriangles);
        ArrayList<Polyhedron> differ_list = dFun.doubleShapesByMaxTrianglesForPolyhedron(union_shapes, maxNumberOfTriangles);

        ArrayList<Triangle> polyRTriangles = clearIn(poly);
        differ_list = clearIn(differ_list);
        processing(polyRTriangles, differ_list);
        processingBack(polyRTriangles, differ_list);
        
        // output
//        output(resPoly);
    }
    
    public Polyhedron getFinishedSingle(){
        resPoly.getVertices();
        return resPoly;
    }

        
    public void output(Polyhedron resultShape){
        if (edit == null){
            System.err.println("Didn't get the editor, check constructor part!");
            return;
        }
        edit.clear();
        resultShape.getVertices();
        edit.add(resultShape);
    }
    
    public void processing(ArrayList<Triangle> polyRTriangles, ArrayList<Polyhedron> differ_list){
        for (int i = 0; i < polyRTriangles.size(); ++i){
            ArrayList<Triangle> thisRound = new ArrayList<>();
            thisRound.add(polyRTriangles.get(i));
            for (int j = 0; j < differ_list.size(); ++j){
                ArrayList<Triangle> otherTrianlges = differ_list.get(j).triangles();
                for (Triangle tt : otherTrianlges){
                    ArrayList<Triangle> tempResult = new ArrayList<>();
                    for (Triangle t : thisRound) {
                        tempResult.addAll(dFun.processTriangle(t, tt));
                    }
                    thisRound = tempResult;
                }
            }
            clearInDiffers(thisRound);
            resPoly.facets.addAll(thisRound);
        }   
    }
    
    public void processingBack(ArrayList<Triangle> polyRTriangles, ArrayList<Polyhedron> differ_list){
        ArrayList<Triangle> needProcess = new ArrayList<>();
        for (int i = 0; i < differ_list.size(); ++i){
            ArrayList<Triangle> triangles = differ_list.get(i).triangles();
            for (int j = 0; j < triangles.size(); ++j){
                ArrayList<Triangle> thisRound = new ArrayList<>();
                thisRound.add(triangles.get(j));
                for (int k = 0; k < differ_list.size(); ++k){
                    if (k == i) {continue;}
                    ArrayList<Triangle> otherTriangles = differ_list.get(k).triangles();
                    for (Triangle tt : otherTriangles) {
                        ArrayList<Triangle> tempResult = new ArrayList<>();
                        for (Triangle t : thisRound) {
                            tempResult.addAll(dFun.processTriangle(t, tt));
                        }
                        thisRound = tempResult;
                    }
                }
                clearOutOfOrigin(thisRound);
                needProcess.addAll(thisRound);
            }
        }
        
        // Do we need union differ list?
        
        
        for (int i = 0; i < needProcess.size(); ++i){
            ArrayList<Triangle> thisRound = new ArrayList<>();
            thisRound.add(needProcess.get(i));
            for (Triangle tt : polyRTriangles){
                ArrayList<Triangle> tempResult = new ArrayList<>();
                for (Triangle t : thisRound) {
                    tempResult.addAll(dFun.processTriangle(t, tt));
                }
                thisRound = tempResult;
            }
            clearNotInOriginal(thisRound);
            for (Triangle ttt : thisRound){
                Triangle newttt = flipTriangle(ttt);
                resPoly.facets.add(newttt);
            }
//            resPoly.facets.addAll(thisRound);
        }
    }
    
    public Triangle flipTriangle(Triangle t){
        Triangle res = new Triangle(t.get(2), t.get(1), t.get(0));
        return res;
    }
    
    public void clearNotInOriginal(ArrayList<Triangle> triangles){
        for (int i = 0; i < triangles.size(); ++i){
            Triangle t = triangles.get(i);
            if (dFun.vertexIn(origin_shape, t, true) != 3){
                dFun.removeReplace(triangles, i);
                i--;
            }
        }
    }
    
    public void clearOutOfOrigin(ArrayList<Triangle> triangles){
        for (int i = 0; i < triangles.size(); ++i){
            Triangle t = triangles.get(i);
            if (dFun.isDefinitelyOut(origin_shape, t)){
                dFun.removeReplace(triangles, i);
                i--;
            }
        }
    }

    
    public void clearInDiffers(ArrayList<Triangle> triangles){
        for (int i = 0; i < triangles.size(); ++i) {
            Triangle t = triangles.get(i);
            for (int j = 0; j < origin_differ.size(); ++j) {
                if (dFun.vertexIn(origin_differ.get(j), t, true) == 3) {
                    dFun.removeReplace(triangles, i);
                    i--;
                    break;
                }
            }
        }
    }
    
    public ArrayList<Triangle>  clearIn(Polyhedron poly){
        ArrayList<Triangle> triangles = poly.triangles();
        for (int i = 0; i < triangles.size(); ++i){
            Triangle t = triangles.get(i);
            boolean flag = true;
            for (int j = 0; j < origin_differ.size(); ++j){
                if (dFun.vertexIn(origin_differ.get(j), t, true) == 3) {
                    dFun.removeReplace(triangles, i);
                    flag = false;
                    i--;
                    break;
                } 
            }
            if (flag){
                if (dFun.isDefinitelyOut(origin_differ, t, -1)) {
                    resPoly.facets.add(t);
                    dFun.removeReplace(triangles, i);
                    i--;
                }
            }
        }

        return triangles;
    }
    
    public ArrayList<Polyhedron> clearIn(ArrayList<Polyhedron> differ_list){
        ArrayList<Polyhedron> res = new ArrayList<>();
        for (int i = 0; i < differ_list.size(); ++i){
            ArrayList<Triangle> triangles = differ_list.get(i).triangles();
            for (int j = 0; j < triangles.size(); ++j){
                Triangle t = triangles.get(j);
                if (dFun.vertexIn(origin_shape, t, true) == 3){
                    // pass
                }
                else if (dFun.isDefinitelyOut(origin_shape, t)){
                    dFun.removeReplace(triangles, j);
                    j--;
                }
            }
            Polyhedron p = new Polyhedron();
            p.facets.addAll(triangles);
            res.add(p);
        }
        return res;
    }
    

    
    
//    public static void main(String[] args) {
//        CADEditor view = CADEditor.openWindow();
//
//        Cube a = new Cube(100);
//        Sphere s = new Sphere(80, 50, 50, new Vector(50, 0, 0));
////        Cube a2 = new Cube(25, new Vector(-50, 0, 0));
//        Cube a2 = new Cube(25, new Vector(-25, 0, 0));
//        view.add(a);
//        view.add(s);
//        view.add(a2);
//        
//        long timeA = System.currentTimeMillis();
//
//        dDiffer du = new dDiffer(view);
//        ArrayList<AnalyticalShape3d> ana = new ArrayList<>();
//        ana.add((AnalyticalShape3d)s);
//        ana.add((AnalyticalShape3d)a2);
//        du.doDiffer((AnalyticalShape3d)a, ana);
//
//        long timeB = System.currentTimeMillis();
//        System.out.println("time: " + (timeB - timeA) + "ms");
//
//    }

    
    
}
