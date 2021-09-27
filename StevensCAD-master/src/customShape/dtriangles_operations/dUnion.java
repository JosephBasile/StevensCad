/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customShape.dtriangles_operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import primitives.AnalyticalShape3d;
import primitives.Cube;
import primitives.Cylinder;
import primitives.Shape;
import primitives.Sphere;
import scad.Edge;
import scad.Facet;
import scad.Polyhedron;
import scad.Triangle;
import scad.Vector;
import transformation.Transformation;
import viewer.CADEditor;

/**
 *
 * @author Long Huang, Haomin Zhang
 */
public class dUnion {

    private CADEditor edit;
    private dFunctions dFun;
    private ArrayList<AnalyticalShape3d> origin_shapes;
    private ArrayList<Polyhedron> resPoly;
    private int debug;

    public void init(CADEditor _edit) {
        this.edit = _edit;
        this.dFun = new dFunctions();
        this.resPoly = null;
        this.debug = 1;
    }

    public dUnion() {
        init(null);
    }

    public dUnion(CADEditor _edit) {
        init(_edit);
    }

    public void doUnion(AnalyticalShape3d shapeA, AnalyticalShape3d shapeB) {
        ArrayList<AnalyticalShape3d> shapes = new ArrayList<>();
        shapes.add(shapeA);
        shapes.add(shapeB);
        doUnion(shapes);
    }

    public void doUnionShapes(ArrayList<Shape> shapes) {
        ArrayList<AnalyticalShape3d> tana = dFun.toAnalysticalArray(shapes);
        doUnion(tana);
    }

    public void doUnion(ArrayList<AnalyticalShape3d> shapes) {

        origin_shapes = shapes;
        resPoly = new ArrayList<>();
        for (int i = 0; i < origin_shapes.size(); ++i) {
            resPoly.add(new Polyhedron());
        }
        // check resolution
        int maxNumberOfTriangles = dFun.getMaxNumberOfTriangles(shapes);
        ArrayList<Polyhedron> resPolyList = dFun.doubleShapesByMaxTriangles(shapes, maxNumberOfTriangles);
        for (int i = 0; i < resPolyList.size(); ++i) {
            resPolyList.set(i, clearIn(resPolyList.get(i), i));
        }
        
        processing(resPolyList);
        
        // output
//        output();
    }
    
    public ArrayList<Polyhedron> getFinished(){
        return resPoly;
    }
    
    public Polyhedron getFinishedSingle(){
        Polyhedron resSingle = new Polyhedron();
        for (int i = 0; i < resPoly.size(); ++i){
            resSingle.facets.addAll(resPoly.get(i).triangles());
        }
        resSingle.getVertices();
        return resSingle;
    }
    
    public void output(){
        dFun.finished(edit, resPoly);
    }
        
    private Polyhedron clearIn(Polyhedron p, int idx) {
        ArrayList<Triangle> t = p.triangles();
        clearIn(t, idx);
        Polyhedron np = new Polyhedron();
        np.facets.addAll(t);
        np.getVertices();
        return np;
    }

    private void clearIn(ArrayList<Triangle> triangles, int idx) {
        for (int i = 0; i < triangles.size(); ++i) {
            boolean flag = true;
            Triangle t = triangles.get(i);
            for (int j = 0; j < origin_shapes.size(); ++j) {
                if (j == idx) {
                    continue;
                }
                if (dFun.vertexIn(origin_shapes.get(j), t, true) == 3) {
                    dFun.removeReplace(triangles, i);
                    flag = false;
                    i--;
                    break;
                }
            }
            if (flag) {
                if (dFun.isDefinitelyOut(origin_shapes, t, idx)) {
                    resPoly.get(idx).facets.add(t);
                    dFun.removeReplace(triangles, i);
                    i--;
                }
            }
        }
    }

    private void processing(ArrayList<Polyhedron> resPolyList) {
        for (int i = 0; i < resPolyList.size(); ++i) {
            Polyhedron currentShape = resPolyList.get(i);
            Polyhedron newShape = resPoly.get(i);
            ArrayList<Triangle> currentTriangles = currentShape.triangles();
            for (int k = 0; k < currentTriangles.size(); ++k) {
                if (k % 500 == 0 || k == currentTriangles.size() - 1) {
                    System.out.println("This is " + k + " / " + currentTriangles.size() + " in shape: " + i);
                }
                ArrayList<Triangle> thisRound = new ArrayList<>();
                thisRound.add(currentTriangles.get(k));
                for (int j = 0; j < resPolyList.size(); ++j) {
                    if (i == j) {
                        continue;
                    }
                    ArrayList<Triangle> j_triangles = resPolyList.get(j).triangles();
                    for (Triangle tt : j_triangles) {
                        ArrayList<Triangle> tempResult = new ArrayList<>();
                        for (Triangle t : thisRound) {
                            tempResult.addAll(dFun.processTriangle(t, tt));
                        }
                        thisRound = tempResult;
                    }
                }
                clearIn(thisRound, i);
                newShape.facets.addAll(thisRound);
            }
        }
    }

  
}
