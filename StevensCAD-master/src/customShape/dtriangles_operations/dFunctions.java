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
import scad.Facet;
import scad.Polyhedron;
import scad.Triangle;
import scad.Vector;
import scad.Edge;
import transformation.Transformation;
import viewer.CADEditor;

/**
 *
 * @author hemor
 */
public class dFunctions {

    public Polyhedron doubleShapesByMaxTriangles(Polyhedron curr, int maxNumberOfTriangles){
        int currTrianglesNumebr = curr.triangles().size();
        double t = (double) (maxNumberOfTriangles) / currTrianglesNumebr;
        int needDoubleTimes = (int) Math.floor(Math.log(t) / Math.log(2));

        for (int j = 0; j < needDoubleTimes; ++j) {
            curr = doubleTriangles(curr);
        }
        return curr;
    }
    
    public ArrayList<Polyhedron> doubleShapesByMaxTriangles(ArrayList<AnalyticalShape3d> shapes,
            int maxNumberOfTriangles) {
        // not such prefect when shape is cylinder
        ArrayList<Polyhedron> res = new ArrayList<>();
        for (int i = 0; i < shapes.size(); ++i) {
            Polyhedron curr = shapes.get(i).poly;
            curr = doubleShapesByMaxTriangles(curr, maxNumberOfTriangles);
            res.add(curr);
        }
        return res;
    }
    
    public ArrayList<Polyhedron> doubleShapesByMaxTrianglesForPolyhedron(ArrayList<Polyhedron> shapes,
            int maxNumberOfTriangles){
        ArrayList<Polyhedron> res = new ArrayList<>();
        for (int i = 0; i < shapes.size(); ++i) {
            Polyhedron curr = shapes.get(i);
            curr = doubleShapesByMaxTriangles(curr, maxNumberOfTriangles);
            res.add(curr);
        }
        return res;
    }

    public int vertexIn_Common(AnalyticalShape3d comp, Triangle t) {
        int counting = 0;
        if (comp.poly.definitelyIn(t.vertex.get(0))) {
            counting++;
        }
        if (comp.poly.definitelyIn(t.vertex.get(1))) {
            counting++;
        }
        if (comp.poly.definitelyIn(t.vertex.get(2))) {
            counting++;
        }
        return counting;
    }

    public int vertexIn(AnalyticalShape3d comp, Triangle t, boolean isForced) {
        if (comp instanceof Cube) {
            return vertexIn_Common(comp, t);
        } else if (comp instanceof Sphere) {
            int res = vertexIn_Sphere((Sphere) comp, t);
            return res;
        } else if (comp instanceof Cylinder) {
            if (isForced) {
                return vertexIn_Common(comp, t);
            } else {
                return -1;
            }
        } else if (isForced) {
            return vertexIn_Common(comp, t);
        }
        return -1;
    }

    public int vertexIn_Sphere(Sphere ts, Triangle t) {
        int counting = 0;
        Vector center = new Vector(0, 0, 0);
        Transformation trans = ts.t;
        trans.transform(center);
        double r = ts.getRadius();
        double tmin = 1000, tmax = 0;
        for (Vector v : t.vertex) {
            double dis = center.distance(v);
            tmin = Math.min(tmin, dis);
            tmax = Math.max(tmax, dis);
            if (dis <= r) {
                counting++;
            }
        }
        if (counting == 3) {
            return 3;
        } else {
            return 1;
        }
    }

    public void removeReplace(ArrayList<?> shapes, int idx) {
        Collections.swap(shapes, idx, shapes.size() - 1);
        shapes.remove(shapes.size() - 1);
    }

    public Polyhedron doubleTriangles(Polyhedron sp) {
        ArrayList<Triangle> triangles = sp.triangles();
        Polyhedron newSp = new Polyhedron(triangles.size() * 2);
        for (int i = 0; i < triangles.size(); ++i) {
            Triangle t = triangles.get(i);
            Vector a = t.vertex.get(0);
            Vector b = t.vertex.get(1);
            Vector c = t.vertex.get(2);
            double ab = a.distance(b);
            double ac = a.distance(c);
            double bc = b.distance(c);
            if (ab > ac && ab > bc) {
                Vector d = a.added(b).divided(2.);
                newSp.facets.add(new Facet(c, a, d));
                newSp.facets.add(new Facet(d, b, c));
            } else if (ac > ab && ac > bc) {
                Vector d = a.added(c).divided(2.);
                newSp.facets.add(new Facet(a, b, d));
                newSp.facets.add(new Facet(b, c, d));
            } else {
                Vector d = b.added(c).divided(2.);
                newSp.facets.add(new Facet(a, b, d));
                newSp.facets.add(new Facet(a, d, c));
            }
        }
        newSp.getVertsItr();
        return newSp;
    }

    public int getMaxNumberOfTriangles(ArrayList<AnalyticalShape3d> shapes) {
        int maxNumberOfTriangles = 0;
        for (int i = 0; i < shapes.size(); ++i) {
            int triangles_number = shapes.get(i).poly.triangles().size();
            maxNumberOfTriangles = Math.max(triangles_number, maxNumberOfTriangles);
        }
        return maxNumberOfTriangles;
    }
    
    public int getMaxNumberOfTrianglesByPolyheron(ArrayList<Polyhedron> shapes){
        int maxNumberOfTriangles = 0;
        for (int i = 0; i < shapes.size(); ++i) {
            int triangles_number = shapes.get(i).triangles().size();
            maxNumberOfTriangles = Math.max(triangles_number, maxNumberOfTriangles);
        }
        return maxNumberOfTriangles;
    }

    public double distancePoint2Edge(Edge e, Vector v) {
        Vector a = e.a;
        Vector b = e.b;
        double ab = a.distance(b);
        double av = a.distance(v);
        double bv = b.distance(v);
        double cosA = (Math.pow(av, 2) + Math.pow(ab, 2) - Math.pow(bv, 2)) / (2 * av * ab);
        double sinA = Math.sqrt(1 - Math.pow(cosA, 2));
        return av * sinA;
    }

    public List<Double> getPanel(Vector v1, Vector v2, Vector v3) {
        List<Double> res = new ArrayList<>();
        double a = ((v2.y - v1.y) * (v3.z - v1.z) - (v2.z - v1.z) * (v3.y - v1.y));
        double b = ((v2.z - v1.z) * (v3.x - v1.x) - (v2.x - v1.x) * (v3.z - v1.z));
        double c = ((v2.x - v1.x) * (v3.y - v1.y) - (v2.y - v1.y) * (v3.x - v1.x));
        double d = -(a * v1.x + b * v1.y + c * v1.z);
        res.add(a);
        res.add(b);
        res.add(c);
        res.add(d);
        return res;
    }

    double distancePoint2Panel(Vector v, double a, double b, double c, double d) {
        return Math.abs(a * v.x + b * v.y + c * v.z + d) / Math.sqrt(a * a + b * b + c * c);
    }

    public ArrayList<AnalyticalShape3d> toAnalysticalArray(ArrayList<Shape> shapes) {
        ArrayList<AnalyticalShape3d> res = new ArrayList<>();
        for (Shape s : shapes) {
            res.add((AnalyticalShape3d) s);
        }
        return res;
    }

    protected boolean isDefinitelyOut(Triangle t, Vector v, double r) {
        List<Double> panel_abcd = getPanel(t.get(0), t.get(1), t.get(2));
        double dis = distancePoint2Panel(v, panel_abcd.get(0), panel_abcd.get(1), panel_abcd.get(2), panel_abcd.get(3));
        if (dis > r) {
            return true;
        }
        return false;
    }

    protected boolean isDefinitelyOut(AnalyticalShape3d s, Triangle t) {
        if (s instanceof Cube) {
            Vector center = new Vector(0, 0, 0);
            s.t.transform(center);
            double r = ((Cube) s).getSize();
            if (isDefinitelyOut(t, center, r)) {
                return true;
            }
        } else if (s instanceof Sphere) {
            Vector center = new Vector(0, 0, 0);
            s.t.transform(center);
            double r = ((Sphere) s).getRadius();
            if (isDefinitelyOut(t, center, r)) {
                return true;
            }
        } else if (s instanceof Cylinder) {
            double h = ((Cylinder) s).getHeight();
            double r = ((Cylinder) s).getRadius();
            Vector center = new Vector(0, 0, h / 2);
            s.t.transform(center);
            double rr = Math.sqrt(h * h / 4 + r * r);
            if (isDefinitelyOut(t, center, rr)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    protected boolean isDefinitelyOut(ArrayList<AnalyticalShape3d> shapes, Triangle t, int idx) {
        for (int i = 0; i < shapes.size(); ++i) {
            if (i == idx) {
                continue;
            }
            AnalyticalShape3d s = shapes.get(i);
            if (!isDefinitelyOut(s, t)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Triangle> processTriangle(Triangle current, Triangle t) {
        ArrayList<Triangle> newTriangles = new ArrayList<>();
        ArrayList<Vector> intersections = intersections(current, t);
        boolean above = false;
        for (Vector vector : t.vertex) {
            if (current.above(vector)) {
                above = true;
            }
        }

        if (intersections.size() == 2 && above) {
            Vector v0 = intersections.get(0);
            Vector v1 = intersections.get(1);

            ArrayList<Edge> currentEdges = new ArrayList<>();
            for (int i = 0; i < 3; ++i) {
                currentEdges.add(current.edge(i));
            }

            ArrayList<Edge> edgesContainv0 = new ArrayList<>();
            ArrayList<Edge> edgesContainNov0 = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (currentEdges.get(j).contains(v0)) {
                    edgesContainv0.add(current.edge(j));
                } else {
                    edgesContainNov0.add(current.edge(j));
                }
            }

            ArrayList<Edge> edgesContainv1 = new ArrayList<>();
            ArrayList<Edge> edgesContainNov1 = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (currentEdges.get(j).contains(v1)) {
                    edgesContainv1.add(current.edge(j));
                } else {
                    edgesContainNov1.add(current.edge(j));
                }
            }

            if (edgesContainv0.isEmpty() && edgesContainv1.isEmpty()) {
                ArrayList<Triangle> trianglesContainv1 = new ArrayList<>();
                ArrayList<Triangle> trianglesContainNov1 = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    Triangle triangle = new Triangle(v0, current.edge(j).a, current.edge(j).b);
                    if ((!triangle.contains(v1)) && (!onEdge(triangle, v1))) {
                        trianglesContainNov1.add(triangle);
                    } else {
                        trianglesContainv1.add(triangle);
                    }
                }

                if (trianglesContainv1.size() == 1) {
                    for (Triangle tri : trianglesContainNov1) {
                        newTriangles.add(tri);
                    }
                    newTriangles.add(new Triangle(v1, trianglesContainv1.get(0).get(1), trianglesContainv1.get(0).get(2)));
                    newTriangles.add(new Triangle(v1, v0, trianglesContainv1.get(0).get(1)));
                    newTriangles.add(new Triangle(v0, v1, trianglesContainv1.get(0).get(2)));
                } else if (trianglesContainv1.size() == 2) {
                    Triangle t1, t2;
                    if (trianglesContainv1.get(0).edge(1).contains(trianglesContainNov1.get(0).get(2))) {
                        t1 = trianglesContainv1.get(0);
                        t2 = trianglesContainv1.get(1);
                    } else {
                        // trianglesContainv1.get(0).edge(1).contains(trianglesContainNov1.get(0).get(1))
                        t1 = trianglesContainv1.get(1);
                        t2 = trianglesContainv1.get(0);
                    }
                    newTriangles.add(new Triangle(v0, trianglesContainNov1.get(0).get(1), trianglesContainNov1.get(0).get(2)));
                    newTriangles.add(new Triangle(v1, v0, t1.get(1)));
                    newTriangles.add(new Triangle(v0, v1, t2.get(2)));
                    newTriangles.add(new Triangle(v1, t1.get(1), t1.get(2)));
                    newTriangles.add(new Triangle(v1, t2.get(1), t2.get(2)));
                } else {
                    // could be 3
                    newTriangles.add(current);
                }
            } else if ((!edgesContainv0.isEmpty()) && edgesContainv1.isEmpty()) {
                processVector(edgesContainv0, edgesContainNov0, v0, v1, newTriangles, current);
            } else if (edgesContainv0.isEmpty() && (!edgesContainv1.isEmpty())) {
                processVector(edgesContainv1, edgesContainNov1, v1, v0, newTriangles, current);
            } else {
                // !edgesContainv0.isEmpty()) && (!edgesContainv1.isEmpty()
                if (edgesContainv0.size() == 1) {
                    Vector c = null;
                    for (Vector vec : current.vertex) {
                        if (!edgesContainv0.get(0).contains(vec)) {
                            c = vec;
                        }
                    }
                    if (edgesContainv0.get(0).contains(v1)) {
                        newTriangles.add(current);
                    } else {
                        if (edgesContainv1.size() == 1) {
                            if (edgesContainv1.get(0).contains(edgesContainv0.get(0).a)) {
                                newTriangles.add(new Triangle(v0, v1, edgesContainv0.get(0).a));
                                newTriangles.add(new Triangle(v1, v0, edgesContainv0.get(0).b));
                                newTriangles.add(new Triangle(edgesContainv0.get(0).b, edgesContainv1.get(0).a, v1));
                            } else {
                                // edgesContainv1.get(0).contains(edgesContainv0.get(0).b)
                                newTriangles.add(new Triangle(v1, v0, edgesContainv0.get(0).b));
                                newTriangles.add(new Triangle(v0, v1, edgesContainv0.get(0).a));
                                newTriangles.add(new Triangle(edgesContainv1.get(0).b, edgesContainv0.get(0).a, v1));
                            }

                        } else {
                            // edgesContainv1.size() == 2
                            newTriangles.add(new Triangle(v0, v1, edgesContainv0.get(0).a));
                            newTriangles.add(new Triangle(v1, v0, edgesContainv0.get(0).b));
                        }

                    }
                } else if (edgesContainv0.size() == 2) {
                    if (!edgesContainNov0.get(0).contains(v1)) {
                        newTriangles.add(current);
                    } else {
                        //edgesContainNov0.get(0).contains(v1)
                        if (v1.equals(edgesContainNov0.get(0).a) || v1.equals(edgesContainNov0.get(0).b)) {
                            newTriangles.add(current);
                        } else {
                            newTriangles.add(new Triangle(v1, v0, edgesContainNov0.get(0).a));
                            newTriangles.add(new Triangle(v0, v1, edgesContainNov0.get(0).b));
                        }
                    }
                }
            }
        } else {
            newTriangles.add(current);
        }

        return newTriangles;
    }

    public void interSectionAdd(ArrayList<Vector> arr, Vector v) {
        for (int i = 0; i < arr.size(); ++i) {
            if (arr.get(i).equals(v)) {
                return;
            }
        }
        arr.add(v);
    }

    public ArrayList<Vector> intersections(Triangle currentTriangle, Triangle t) {
        ArrayList<Vector> intersections = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            Edge edge = t.edge(i);
            Vector v = currentTriangle.intersect(edge);
            if (edge.contains(v)) {
                if (currentTriangle.contains(v) || onEdge(currentTriangle, v)) {
                    interSectionAdd(intersections, v);
                }
            }
        }
        for (int i = 0; i < currentTriangle.size(); i++) {
            Edge edge = currentTriangle.edge(i);
            Vector v = t.intersect(edge);
            if (edge.contains(v)) {
                if (t.contains(v) || onEdge(t, v)) {
                    interSectionAdd(intersections, v);
                }
            }
        }
        return intersections;
    }

    public boolean onEdge(Triangle t, Vector v) {
        boolean b = false;
        for (int i = 0; i < 3; i++) {
            if (t.edge(i).contains(v)) {
                b = true;
            }
        }
        return b;
    }

    public void finished(CADEditor edit, ArrayList<Polyhedron> resPoly) {
        if (edit == null){
            System.err.println("Didn't get the editor, check constructor part!");
            return;
        }
        edit.clear();
        for (int i = 0; i < resPoly.size(); ++i) {
            Polyhedron resultShape = resPoly.get(i);
            resultShape.getVertices();
            edit.add(resultShape);
        }
    }

    public void processVector(ArrayList<Edge> edgesContainv, ArrayList<Edge> edgesContainNov, Vector onEdgeVector,
            Vector insideVector, ArrayList<Triangle> newTriangles, Triangle current) {
        if (edgesContainv.size() == 1) {
            newTriangles.add(new Triangle(insideVector, edgesContainNov.get(0).a, edgesContainNov.get(0).b));
            newTriangles.add(new Triangle(insideVector, edgesContainNov.get(1).a, edgesContainNov.get(1).b));
            newTriangles.add(new Triangle(onEdgeVector, insideVector, edgesContainv.get(0).a));
            newTriangles.add(new Triangle(insideVector, onEdgeVector, edgesContainv.get(0).b));
        } else {
            newTriangles.add(new Triangle(insideVector, onEdgeVector, edgesContainNov.get(0).a));
            newTriangles.add(new Triangle(onEdgeVector, insideVector, edgesContainNov.get(0).b));
            newTriangles.add(new Triangle(insideVector, edgesContainNov.get(0).a, edgesContainNov.get(0).b));
        }
    }

    public dFunctions() {

    }

}
