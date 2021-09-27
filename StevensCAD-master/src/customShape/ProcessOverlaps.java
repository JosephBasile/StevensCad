/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customShape;

import java.util.ArrayList;
import primitives.AnalyticalShape3d;
import primitives.Shape;
import viewer.CADEditor;
import GUI.NameShape3d;
import scad.Polyhedron;
import scad.Triangle;
import scad.Vector;
import scad.Edge;
import GUI.gui;
import editorV2.CADEditor2;

/**
 *
 * @author Long Huang, modified by Haomin
 */
public class ProcessOverlaps {
    
    private Polyhedron intersection;

    public ProcessOverlaps(CADEditor2 edit, String str) {
        intersection = new Polyhedron();
        ArrayList<Shape> shapes = edit.getShapes();
        ArrayList<Polyhedron> secondShapes = removeDefinitelyIn(shapes);
        
        ArrayList<Polyhedron> newShapes = new ArrayList<>(shapes.size());

        for (int i = 0; i < secondShapes.size(); i++) {
            ArrayList<Polyhedron> otherShapes = new ArrayList<>();
            for (int j = 0; j < secondShapes.size(); j++) {
                if (j != i) {
                    otherShapes.add(secondShapes.get(j));
                }
            }

            ArrayList<Triangle> otherTriangles = new ArrayList<>();
            for (Polyhedron p : otherShapes) {
                otherTriangles.addAll(p.triangles());
            }
            
            Polyhedron thirdShape = new Polyhedron();
            Polyhedron currentShape = secondShapes.get(i);
            for (int k = 0; k < currentShape.triangles().size(); k++) {
                Triangle currentTriangle = currentShape.triangles().get(k);
                ArrayList<Triangle> newTriangles = getNewTriangles(currentTriangle, otherTriangles);
                for (Triangle t : newTriangles) {
                    thirdShape.add(t);
                }
            }
            Polyhedron newShape = reRemoveDefinitelyIn(thirdShape, i, shapes);
            newShapes.add(newShape);
        }

        edit.clearScene();
     
        // testing
        if (str.equals("remove")) { //union
            for (int i = 0; i < newShapes.size(); ++i) {
                edit.addShape(newShapes.get(i));
            }
        }
        if (str.equals("remain")) {
            edit.addShape(intersection);
        }
        if (str.equals("biRemove")) {
            for (int i = 0; i < newShapes.size(); ++i) {
                edit.addShape(newShapes.get(i));
            }
            edit.addShape(reverse(intersection));
        }

    }
    
    public ProcessOverlaps(CADEditor edit, String str) {
        intersection = new Polyhedron();
        ArrayList<Shape> shapes = edit.getShapes();
        ArrayList<Polyhedron> secondShapes = removeDefinitelyIn(shapes);
        
        ArrayList<Polyhedron> newShapes = new ArrayList<>(shapes.size());

        for (int i = 0; i < secondShapes.size(); i++) {
            ArrayList<Polyhedron> otherShapes = new ArrayList<>();
            for (int j = 0; j < secondShapes.size(); j++) {
                if (j != i) {
                    otherShapes.add(secondShapes.get(j));
                }
            }

            ArrayList<Triangle> otherTriangles = new ArrayList<>();
            for (Polyhedron p : otherShapes) {
                otherTriangles.addAll(p.triangles());
            }
            
            Polyhedron thirdShape = new Polyhedron();
            Polyhedron currentShape = secondShapes.get(i);
            for (int k = 0; k < currentShape.triangles().size(); k++) {
                Triangle currentTriangle = currentShape.triangles().get(k);
                ArrayList<Triangle> newTriangles = getNewTriangles(currentTriangle, otherTriangles);
                for (Triangle t : newTriangles) {
                    thirdShape.add(t);
                }
            }
            Polyhedron newShape = reRemoveDefinitelyIn(thirdShape, i, shapes);
            newShapes.add(newShape);
        }

        edit.clear();
     
        // testing
        if (str.equals("remove")) { //union
            for (int i = 0; i < newShapes.size(); ++i) {
                edit.add(newShapes.get(i));
            }
        }
        if (str.equals("remain")) {
            edit.add(intersection);
        }
        if (str.equals("biRemove")) {
            for (int i = 0; i < newShapes.size(); ++i) {
                edit.add(newShapes.get(i));
            }
            edit.add(reverse(intersection));
        }

    }
    
    public ArrayList<Polyhedron> removeDefinitelyIn(ArrayList<Shape> shapes){
        ArrayList<AnalyticalShape3d> analyticalShapes = new ArrayList<>();
        for(int i = 0; i < shapes.size(); i++){
            if(shapes.get(i) instanceof AnalyticalShape3d){
                analyticalShapes.add((AnalyticalShape3d)shapes.get(i));
            }
        }
        
        ArrayList<Polyhedron> secondShapes = new ArrayList<>(shapes.size());
        
        int num = 0;
        for (int i = 0; i < shapes.size(); i++) {
            Polyhedron secondShape = new Polyhedron();
            ArrayList<Triangle> remainTriangles = shapes.get(i).triangles();
            
            if(shapes.get(i) instanceof AnalyticalShape3d){
                ArrayList<AnalyticalShape3d> otherAnalyticalShapes = new ArrayList<>();
                for (int j = 0; j < analyticalShapes.size(); j++) {
                    if (j != num) {
                        otherAnalyticalShapes.add(analyticalShapes.get(j));
                    }
                }
                
                for (int j = 0; j < otherAnalyticalShapes.size(); j++) {
                    AnalyticalShape3d otherAnalyticalShape = otherAnalyticalShapes.get(j);
                    for (int k = 0; k < remainTriangles.size(); k++) {
                        Triangle currentTriangle = remainTriangles.get(k);
                        if (otherAnalyticalShape.poly.definitelyIn(currentTriangle.vertex.get(0))
                                && otherAnalyticalShape.poly.definitelyIn(currentTriangle.vertex.get(1))
                                && otherAnalyticalShape.poly.definitelyIn(currentTriangle.vertex.get(2))) {
                            intersection.add(currentTriangle);
                            remainTriangles.remove(k);
                            k--;
                        }
                    }
                }
                
                num++;
            }
            else{
                for (int j = 0; j < analyticalShapes.size(); j++) {
                    AnalyticalShape3d analyticalShape = analyticalShapes.get(j);
                    for (int k = 0; k < remainTriangles.size(); k++) {
                        Triangle currentTriangle = remainTriangles.get(k);
                        if (analyticalShape.poly.definitelyIn(currentTriangle.vertex.get(0))
                                && analyticalShape.poly.definitelyIn(currentTriangle.vertex.get(1))
                                && analyticalShape.poly.definitelyIn(currentTriangle.vertex.get(2))) {
                            intersection.add(currentTriangle);
                            remainTriangles.remove(k);
                            k--;
                        }
                    }
                }
            }
            
            for(Triangle t:remainTriangles){
                secondShape.add(t);
            }
            secondShapes.add(secondShape);
        }
        
        return secondShapes;
    }
    
    public Polyhedron reRemoveDefinitelyIn(Polyhedron thirdShape, int num, ArrayList<Shape> shapes){
        ArrayList<AnalyticalShape3d> analyticalShapes = new ArrayList<>();
        for(int i = 0; i < shapes.size(); i++){
            if(shapes.get(i) instanceof AnalyticalShape3d){
                if(i != num){
                    analyticalShapes.add((AnalyticalShape3d)shapes.get(i));
                }
            }
        }
        
        Polyhedron newShape = new Polyhedron();
        ArrayList<Triangle> remainTriangles = thirdShape.triangles();
        
        for (int i = 0; i < analyticalShapes.size(); i++) {
            AnalyticalShape3d analyticalShape = analyticalShapes.get(i);
            for (int j = 0; j < remainTriangles.size(); j++) {
                Triangle currentTriangle = remainTriangles.get(j);
                if (analyticalShape.poly.definitelyIn(currentTriangle.vertex.get(0))
                        && analyticalShape.poly.definitelyIn(currentTriangle.vertex.get(1))
                        && analyticalShape.poly.definitelyIn(currentTriangle.vertex.get(2))) {
                    intersection.add(currentTriangle);
                    remainTriangles.remove(j);
                    j--;
                }
            }
        }

        for (Triangle t : remainTriangles) {
            newShape.add(t);
        }
        
        return newShape;
    }
    
    public Polyhedron reverse(Polyhedron p){
        Polyhedron newPolyhedron = new Polyhedron();
        for(Triangle t : p.triangles()){
            newPolyhedron.add(new Triangle(t.vertex.get(0),t.vertex.get(2),t.vertex.get(1)));
        }
        return newPolyhedron;
    }

    public ArrayList<Triangle> getNewTriangles(Triangle currentTriangle, ArrayList<Triangle> otherTriangles) {
        ArrayList<Triangle> newTriangles = new ArrayList<>();

        newTriangles.add(currentTriangle);

        for (Triangle t : otherTriangles) {
            ArrayList<Triangle> news = new ArrayList<>();

            for (Triangle n : newTriangles) {
                news.addAll(processTriangle(n, t));
            }

            newTriangles.clear();
            newTriangles.addAll(news);
        }

        return newTriangles;
    }

    public ArrayList<Triangle> processTriangle(Triangle current, Triangle t) {
        ArrayList<Triangle> newTriangles = new ArrayList<>();

        newTriangles.add(current);

        ArrayList<Vector> intersections = intersections(current, t);
        
        boolean above = false;
        for(Vector vector : t.vertex){
            if(current.above(vector)){
                above = true;
            }
        }
        
        if (intersections.size() == 2 && above) {
            Vector[] v = new Vector[2];
            v[0]=intersections.get(0);
            v[1]=intersections.get(1);

            ArrayList<Edge> edgesContainv0 = new ArrayList<>();
            ArrayList<Edge> edgesContainNov0 = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (current.edge(j).contains(v[0])) {
                    edgesContainv0.add(current.edge(j));
                } else {
                    edgesContainNov0.add(current.edge(j));
                }
            }

            ArrayList<Edge> edgesContainv1 = new ArrayList<>();
            ArrayList<Edge> edgesContainNov1 = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (current.edge(j).contains(v[1])) {
                    edgesContainv1.add(current.edge(j));
                } else {
                    edgesContainNov1.add(current.edge(j));
                }
            }

            if (edgesContainv0.isEmpty() && edgesContainv1.isEmpty()) {
                ArrayList<Triangle> trianglesContainv1 = new ArrayList<>();
                ArrayList<Triangle> trianglesContainNov1 = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    Triangle triangle = new Triangle(v[0], current.edge(j).a, current.edge(j).b);
                    if ((!triangle.contains(v[1])) && (!onEdge(triangle,v[1]))) {
                        trianglesContainNov1.add(triangle);
                    } else {
                        trianglesContainv1.add(triangle);
                    }
                }

                if (trianglesContainv1.size() == 1) {
                    newTriangles.remove(current);
                    for(Triangle tri : trianglesContainNov1){
                        newTriangles.add(tri);
                    }
                    newTriangles.add(new Triangle(v[1], trianglesContainv1.get(0).get(1), trianglesContainv1.get(0).get(2)));
                    newTriangles.add(new Triangle(v[1], v[0], trianglesContainv1.get(0).get(1)));
                    newTriangles.add(new Triangle(v[0], v[1], trianglesContainv1.get(0).get(2)));
                }

                if (trianglesContainv1.size() == 2) {
                    newTriangles.remove(current);
                    Triangle t1 = null;
                    Triangle t2 = null;
                    if (trianglesContainv1.get(0).edge(1).contains(trianglesContainNov1.get(0).get(2))) {
                        t1 = trianglesContainv1.get(0);
                        t2 = trianglesContainv1.get(1);
                    }
                    if (trianglesContainv1.get(0).edge(1).contains(trianglesContainNov1.get(0).get(1))) {
                        t1 = trianglesContainv1.get(1);
                        t2 = trianglesContainv1.get(0);
                    }

                    newTriangles.add(new Triangle(v[0], trianglesContainNov1.get(0).get(1), trianglesContainNov1.get(0).get(2)));
                    newTriangles.add(new Triangle(v[1], v[0], t1.get(1)));
                    newTriangles.add(new Triangle(v[0], v[1], t2.get(2)));
                    newTriangles.add(new Triangle(v[1], t1.get(1), t1.get(2)));
                    newTriangles.add(new Triangle(v[1], t2.get(1), t2.get(2)));
                }
            }

            if ((!edgesContainv0.isEmpty()) && edgesContainv1.isEmpty()) {
                processVector(edgesContainv0, edgesContainNov0, v[0], v[1], newTriangles, current);
            }

            if (edgesContainv0.isEmpty() && (!edgesContainv1.isEmpty())) {
                processVector(edgesContainv1, edgesContainNov1, v[1], v[0], newTriangles, current);
            }
            
            if ((!edgesContainv0.isEmpty()) && (!edgesContainv1.isEmpty())) {
                if (edgesContainv0.size() == 1) {
                    Vector c = null;
                    for(Vector vec:current.vertex){
                        if(!edgesContainv0.get(0).contains(vec)){
                            c = vec;
                        }
                    }
                        
                    if (edgesContainv0.get(0).contains(v[1])) {
                        if((new Edge(edgesContainv0.get(0).a, v[0])).contains(v[1])){
                            newTriangles.remove(current);
                            if(v[1].equals(edgesContainv0.get(0).a)){
                                newTriangles.add(new Triangle(v[0], c, edgesContainv0.get(0).a));
                                newTriangles.add(new Triangle(v[0],edgesContainv0.get(0).b,c));
                            }
                            else{
                                newTriangles.add(new Triangle(v[1],v[0],c));
                                newTriangles.add(new Triangle(c,v[0],edgesContainv0.get(0).b));
                                newTriangles.add(new Triangle(v[1],c,edgesContainv0.get(0).a));
                            }
                        }
                        if((new Edge(v[0], edgesContainv0.get(0).b)).contains(v[1])){
                            newTriangles.remove(current);
                            if(v[1].equals(edgesContainv0.get(0).b)){
                                newTriangles.add(new Triangle(v[0], edgesContainv0.get(0).b, c));
                                newTriangles.add(new Triangle(v[0], c, edgesContainv0.get(0).a));
                            }
                            else{
                                newTriangles.add(new Triangle(v[0],v[1],c));
                                newTriangles.add(new Triangle(c,v[1],edgesContainv0.get(0).b));
                                newTriangles.add(new Triangle(v[0],c,edgesContainv0.get(0).a));
                            }
                        }
                    }
                    
                    if (!edgesContainv0.get(0).contains(v[1])) {
                        if (edgesContainv1.size() == 1) {
                            if (edgesContainv1.get(0).contains(edgesContainv0.get(0).a)) {
                                newTriangles.remove(current);
                                newTriangles.add(new Triangle(v[0], v[1], edgesContainv0.get(0).a));
                                newTriangles.add(new Triangle(v[1], v[0], edgesContainv0.get(0).b));
                                newTriangles.add(new Triangle(edgesContainv0.get(0).b, edgesContainv1.get(0).a, v[1]));
                            }
                            if (edgesContainv1.get(0).contains(edgesContainv0.get(0).b)) {
                                newTriangles.remove(current);
                                newTriangles.add(new Triangle(v[1], v[0], edgesContainv0.get(0).b));
                                newTriangles.add(new Triangle(v[0], v[1], edgesContainv0.get(0).a));
                                newTriangles.add(new Triangle(edgesContainv1.get(0).b, edgesContainv0.get(0).a, v[1]));
                            }
                        }

                        if (edgesContainv1.size() == 2) {
                            newTriangles.remove(current);
                            newTriangles.add(new Triangle(v[0], v[1], edgesContainv0.get(0).a));
                            newTriangles.add(new Triangle(v[1], v[0], edgesContainv0.get(0).b));
                        }
                    }
                }

                if (edgesContainv0.size() == 2) {
                    if (!edgesContainNov0.get(0).contains(v[1])) {
                        if(edgesContainv1.get(0).contains(edgesContainNov0.get(0).a)){
                            newTriangles.remove(current);
                            newTriangles.add(new Triangle(v[0],v[1],edgesContainNov0.get(0).b));
                            newTriangles.add(new Triangle(v[1],edgesContainNov0.get(0).a,edgesContainNov0.get(0).b));
                        }
                        if(edgesContainv1.get(0).contains(edgesContainNov0.get(0).b)){
                            newTriangles.remove(current);
                            newTriangles.add(new Triangle(v[1],v[0],edgesContainNov0.get(0).a));
                            newTriangles.add(new Triangle(v[1],edgesContainNov0.get(0).a,edgesContainNov0.get(0).b));
                        }
                    }
                    if (edgesContainNov0.get(0).contains(v[1])) {
                        if (v[1].equals(edgesContainNov0.get(0).a)) {
                            newTriangles.remove(current);
                            newTriangles.add(new Triangle(v[0],edgesContainNov0.get(0).a,edgesContainNov0.get(0).b));
                        }
                        else if (v[1].equals(edgesContainNov0.get(0).b)) {
                            newTriangles.remove(current);
                            newTriangles.add(new Triangle(v[0],edgesContainNov0.get(0).a,edgesContainNov0.get(0).b));
                        } 
                        else {
                            newTriangles.remove(current);
                            newTriangles.add(new Triangle(v[1], v[0], edgesContainNov0.get(0).a));
                            newTriangles.add(new Triangle(v[0], v[1], edgesContainNov0.get(0).b));
                        }
                    }
                }
            }
        }

        return newTriangles;
    }

    public ArrayList<Vector> intersections(Triangle currentTriangle, Triangle t) {
        ArrayList<Vector> intersections = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            Edge edge = t.edge(i);
            Vector v = currentTriangle.intersect(edge);
            if (edge.contains(v)) {
                if (currentTriangle.contains(v) || onEdge(currentTriangle,v)) {
                    intersections.add(v);
                }
            }
        }
        for (int i = 0; i < currentTriangle.size(); i++) {
            Edge edge = currentTriangle.edge(i);
            Vector v = t.intersect(edge);
            if (edge.contains(v)) {
                if (t.contains(v) || onEdge(t,v)) {
                    intersections.add(v);
                }
            }
        }
        
        for (int i = 0; i < intersections.size(); i++) {
            for (int j = i + 1; j < intersections.size(); j++) {
                if (intersections.get(i).equals(intersections.get(j))) {
                    intersections.remove(j);
                    j--;
                }
            }
        }
        
        return intersections;
    }
    
    public boolean onEdge(Triangle t, Vector v){
        boolean b = false;
        for(int i = 0; i<3; i++){
            if(t.edge(i).contains(v)){
                b=true;
            }
        }
        return b;
    }

    public void processVector(ArrayList<Edge> edgesContainv, ArrayList<Edge> edgesContainNov, Vector onEdgeVector,
            Vector insideVector, ArrayList<Triangle> newTriangles, Triangle current) {
        if (edgesContainv.size() == 1) {
            newTriangles.remove(current);
            newTriangles.add(new Triangle(insideVector, edgesContainNov.get(0).a, edgesContainNov.get(0).b));
            newTriangles.add(new Triangle(insideVector, edgesContainNov.get(1).a, edgesContainNov.get(1).b));
            newTriangles.add(new Triangle(onEdgeVector, insideVector, edgesContainv.get(0).a));
            newTriangles.add(new Triangle(insideVector, onEdgeVector, edgesContainv.get(0).b));
        }

        if (edgesContainv.size() == 2) {
            newTriangles.remove(current);
            newTriangles.add(new Triangle(insideVector, onEdgeVector, edgesContainNov.get(0).a));
            newTriangles.add(new Triangle(onEdgeVector, insideVector, edgesContainNov.get(0).b));
            newTriangles.add(new Triangle(insideVector, edgesContainNov.get(0).a, edgesContainNov.get(0).b));
        }
    }

}
