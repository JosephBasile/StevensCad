/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import scad.Triangle;
import scad.Vector;
import transformation.Transformation;

/**
 *
 * @author yifan
 */
public abstract class Shape implements Serializable, Cloneable {

    protected ArrayList<Vector> vertices;//always use VertsIter
    protected String name;
    private HashMap<Vector, Integer> vectorToIndexMap = new HashMap();
    public Transformation t;

    public Shape() {
        t = new Transformation();
        vertices = new ArrayList<>();
    }
    
    public Transformation getTransformation(){
        return t;
    }

    //public String getName() { return name; }
    //public void setName(String n) { name = n; }
    public ArrayList<Vector> getVertices() {
        return vertices;
    }

    public abstract void applyTransform();

    public final void rotX(double radians) {
        t.rotX(radians);
        applyTransform();
    }

    public final void rotY(double radians) {
        t.rotY(radians);
        applyTransform();
    }

    public final void rotZ(double radians) {
        t.rotZ(radians);
        applyTransform();
    }

    public final void rot(double x, double y, double z) {
        t.rot(x, y, z);
        applyTransform();
    }

    public final void rot(Vector v) {
        t.rot(v);
        applyTransform();
    }

    public final void rot(Vector from, Vector to) {
        t.rot(from, to);
        applyTransform();
    }

    public final void rot(Vector axisPos, Vector axisDir, double radians) {
        t.rot(axisPos, axisDir, radians);
        applyTransform();
    }

    public final void translateX(double val) {
        t.translateX(val);
        applyTransform();
    }

    public final void translateY(double val) {
        t.translateY(val);
        applyTransform();
    }

    public final void translateZ(double val) {
        t.translateZ(val);
        applyTransform();
    }

    public final void translate(double x, double y, double z) {
        t.translate(x, y, z);
        applyTransform();
    }

    public final void translate(Vector v) {
        t.translate(v);
        applyTransform();
    }

    public final void scaleX(double s) {
        t.scaleX(s);
        applyTransform();
    }

    public final void scaleY(double s) {
        t.scaleY(s);
        applyTransform();
    }

    public final void scaleZ(double s) {
        t.scaleZ(s);
        applyTransform();
    }

    public final void scale(double x, double y, double z) {
        t.scale(x, y, z);
        applyTransform();
    }

    public final void scale(Vector v) {
        t.scale(v);
        applyTransform();
    }

    public final void scale(double s) {
        t.scale(s);
        applyTransform();
    }

    public final Shape rrotX(double radians) {
        rotX(radians);
        return this;
    }

    public final Shape rrotY(double radians) {
        rotY(radians);
        return this;
    }

    public final Shape rrotZ(double radians) {
        rotZ(radians);
        return this;
    }

    public final Shape rrot(double x, double y, double z) {
        rot(x, y, z);
        return this;
    }

    public final Shape rrot(Vector v) {
        rot(v);
        return this;
    }

    public final Shape rrot(Vector from, Vector to) {
        rot(from, to);
        return this;
    }

    public final Shape rrot(Vector axisPos, Vector axisDir, double radians) {
        rot(axisPos, axisDir, radians);
        return this;
    }

    public final Shape rtranslateX(double val) {
        translateX(val);
        return this;
    }

    public final Shape rtranslateY(double val) {
        translateY(val);
        return this;
    }

    public final Shape rtranslateZ(double val) {
        translateZ(val);
        return this;
    }

    public final Shape rtranslate(double x, double y, double z) {
        translate(x, y, z);
        return this;
    }

    public final Shape rtranslate(Vector v) {
        translate(v);
        return this;
    }

    public final Shape rscaleX(double s) {
        scaleX(s);
        return this;
    }

    public final Shape rscaleY(double s) {
        scaleY(s);
        return this;
    }

    public final Shape rscaleZ(double s) {
        scaleZ(s);
        return this;
    }

    public final Shape rscale(double x, double y, double z) {
        scale(x, y, z);
        applyTransform();
        return this;
    }

    public final Shape rscale(Vector v) {
        scale(v);
        applyTransform();
        return this;
    }

    public final Shape rscale(double s) {
        scale(s);
        return this;
    }

    //TODO maybe make editing functions return themselves, this will allow dev to do obj.scale().translate()...
    //You can add a return this; and replace the void with Shape
    //TODO Add mirroring
    public final Shape mirror(boolean x, boolean y, boolean z) {
        //Add mirroring
        return this;
    }

    //TODO Create union and difference functions and Intersect
    public Shape union(Shape s) {
        //Change to union code
        return this;
    }

    public Shape difference(Shape s) {

        //Change to union code
        return this;
    }

    public Shape intersect(Shape s) {

        //Change to union code
        return this;
    }

    //TODO Shape should have a hull function
    public Shape hull() {
        //Change to hull code
        return this;
    }

    public Shape hull(Shape s) {
        //Change to hull code
        return this;
    }
    //TODO dumbUnion fucntion for faster union if needed

    public Shape dumbUnion(Shape s) {

        //Change to dumb union code
        return this;
    }

    public void setHashMap() {
        //Clear Map
        this.vectorToIndexMap.clear();
        //Map every vector to a index
        for (int i = 0; i < this.getVertices().size(); i++) {
            this.vectorToIndexMap.put(this.getVertices().get(i), i);
        }
    }

    public HashMap<Vector, Integer> getHashMap() {
        return vectorToIndexMap;
    }

    public abstract ArrayList<Triangle> triangles();

    public abstract int size();

}
