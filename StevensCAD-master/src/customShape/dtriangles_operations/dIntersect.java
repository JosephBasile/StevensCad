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
import scad.Polyhedron;
import scad.Triangle;
import scad.Vector;
import viewer.CADEditor;

/**
 *
 * @author hemor
 */
public class dIntersect {
    
    private CADEditor edit;
    private dFunctions dFun;
    private ArrayList<AnalyticalShape3d> origin_shapes;
    private ArrayList<Polyhedron> resPoly;
    
    public dIntersect() {
        init(null);
    }
    
    public dIntersect(CADEditor _edit) {
        init(_edit);
    }
    
    public void init(CADEditor _edit) {
        edit = _edit;
        dFun = new dFunctions();
    }
    
    public void doIntersect(AnalyticalShape3d a, AnalyticalShape3d b) {
        ArrayList<AnalyticalShape3d> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        doIntersect(list);
    }
    
    public void doIntersectShape(ArrayList<Shape> shapes) {
        ArrayList<AnalyticalShape3d> list = dFun.toAnalysticalArray(shapes);
        doIntersect(list);
    }
    
    public void doIntersect(ArrayList<AnalyticalShape3d> shapes) {
        // processing
        origin_shapes = shapes;
        
        resPoly = new ArrayList<>();
        for (int i = 0; i < origin_shapes.size(); ++i) {
            resPoly.add(new Polyhedron());
        }
        
        int maxNumberOfTriangles = dFun.getMaxNumberOfTriangles(shapes);
        ArrayList<Polyhedron> resPolyList = dFun.doubleShapesByMaxTriangles(shapes, maxNumberOfTriangles);
        
        for (int i = 0; i < resPolyList.size(); ++i) {
            resPolyList.set(i, clearOut(resPolyList.get(i), i));
        }
        
        processing(resPolyList);
        
        // output
//        output();
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
                clearOutAfter(thisRound, i);
                newShape.facets.addAll(thisRound);
            }
        }
    }
    
    private void clearOutAfter(ArrayList<Triangle> triangles, int idx) {
        for (int i = 0; i < triangles.size(); ++i) {
            Triangle t = triangles.get(i);
            boolean isIn = true;
            for (int j = 0; j < origin_shapes.size(); ++j) {
                if (j == idx) {
                    continue;
                }
                if (dFun.vertexIn(origin_shapes.get(j), t, true) != 3) {
                    isIn = false;
                    break;
                }
            }
            if (!isIn) {
                dFun.removeReplace(triangles, i);
                i--;
            }
        }
    }
    
    private Polyhedron clearOut(Polyhedron p, int idx) {
        ArrayList<Triangle> t = p.triangles();
        clearOut(t, idx);
        Polyhedron np = new Polyhedron();
        np.facets.addAll(t);
        np.getVertices();
        return np;
    }
    
    private void clearOut(ArrayList<Triangle> triangles, int idx) {
        for (int i = 0; i < triangles.size(); ++i) {
            boolean flag = true;
            Triangle t = triangles.get(i);
            for (int j = 0; j < origin_shapes.size(); ++j) {
                if (j == idx) {
                    continue;
                }
                AnalyticalShape3d s = origin_shapes.get(j);
                if (dFun.isDefinitelyOut(s, t)) {
                    dFun.removeReplace(triangles, i);
                    flag = false;
                    i--;
                    break;
                }
            }
            if (flag) {
                boolean isIn = true;
                for (int j = 0; j < origin_shapes.size(); ++j) {
                    if (dFun.vertexIn(origin_shapes.get(j), t, true) != 3) {
                        isIn = false;
                        break;
                    }
                }
                if (isIn) {
                    resPoly.get(idx).facets.add(t);
                    dFun.removeReplace(triangles, i);
                    i--;
                }
            }
        }
    }
    
     
}
