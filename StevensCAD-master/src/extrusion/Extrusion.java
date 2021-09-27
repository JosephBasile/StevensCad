/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrusion;

import java.util.ArrayList;
import scad.Facet;
import scad.Polyhedron;
import scad.Vector;
import transformation.Transformation;

/**
 *
 * @author Minghui Jin and Yanyan Jiang
 */
public class Extrusion extends Polyhedron {
    private Facet base;
    private Bezier curve;   //the bezier curve track    
    private int numStep; //split number of curves
    private double rotationAngle; //the rotate angle of each step 
    private double[] numScale; //scale number to create a weight change polyhedron   

    public Extrusion(Bezier curve, Facet base, int numStep, double angle, double[] scaleNum ) {
        this.curve = curve;
        this.base = base;
        this.numStep = numStep;
        this.rotationAngle = angle;
        this.numScale = scaleNum;

        vertices = new ArrayList<>(base.size() * numStep);

        setPolygonVertices();
        getNode();
        drawExtrusion();
    }
    
    public Extrusion(Bezier curve, Facet base, int numStep) {
        this(curve, base, numStep, 0);
    }

    public Extrusion(Bezier curve, Facet base, int numStep, double angle){
        this(curve, base, numStep, angle, new double[]{1, 1, 1});
    }
    
    public Extrusion(Bezier curve, Facet base, int numStep, double[] scaleNum){
        this(curve, base, numStep, 0, scaleNum);
    }
    
    public Extrusion(Bezier curve, int numVertices, int numStep, double[] numScale, double angle) {
        this(curve, Facet.regular(numVertices, numScale[0]).flip(), numStep, angle, numScale);
    }
    
    public Extrusion(Bezier curve, int numVertices, int numStep, double[] numScale) {
        this(curve, numVertices, numStep, numScale, 0);
    }
    
    private Vector target(int step, int i) {
        return vertices.get(step * base.size() + i);
    }

    /**
     * get the vertices position from its center
     * coordinate
     */
    public final void setPolygonVertices() {
        Vector center = base.midPoint();
      
        for (int i = 0; i < base.size(); i++)
            base.vertex.set(i, base.get(i).minus(center));
    }

    /**
     * get polyhedron vertices positions
     */
    private void getNode() {
        double step = (double) 1 / (numStep - 1);  //the height of a period
        Transformation trns;
        for (int i = 0; i < numStep; i++) {
            double multipleNum = scaleExtrusion(numScale,1 - i * step);
            trns = curve.getMatrix(1 - i * step).rotZ(i * rotationAngle);//get the curve transform matrix in each step              
            for (int j = 0; j < base.size(); j++)
                vertices.add(trns.reflection3D(base.get(j).times(multipleNum))); //convert coordinate        
        }       
    }
    
    /**
     * draw the bottom base
     */
    private void addBottom() {
        int i = 0;
        for (int j = 1; j < base.size() - 1; ++j) {
            Facet f = new Facet(target(i, 0), target(i, j), target(i, j+1));
            add(f);
        }
    }
    
    /**
     * draw top base
     */
    private void addTop() {
        int i = numStep-1;
        for (int j = 1; j < base.size() - 1; ++j) {
            Facet f = new Facet(target(i, 0), target(i, j), target(i, j+1)); 
            f = f.flip();           
            add(f);
        }
    }
    
    /**
     * draws sides
     */
    private void drawExtrusion() {
        addBottom();
        for (int i = 1; i < numStep; i++)
            for (int j = 0; j < base.size(); j++) {
                int bottom = i - 1;
                int nextVec = (j + 1) % base.size();
                add(new Facet(target(bottom, j), target(i, j), target(i, nextVec)));
                add(new Facet(target(bottom, j), target(i, nextVec), target(bottom, nextVec)));
            }
        addTop();
    }

    /**
     * get the scale factor of the base plate to create a weight change
     * polyhedron.
     *
     * scaleNum numScale target scale number array evenly distributed
     * @param stp the step position ratio
     * @return scale factor of the base plate
     */
    private double scaleExtrusion(double[] scaleNum, double stp) {
        int len = scaleNum.length;

        //if it's  the curve's head or tail
        if (len == 1 || stp <= 0)
            return scaleNum[0];
        else if (stp >= 1)
            return scaleNum[len - 1];

        double scaleStp = stp * (len - 1);
        int i = (int) scaleStp;
        double interpolation = scaleNum[i] + (scaleNum[i+1] - scaleNum[i]) * (scaleStp - i);
        return interpolation; //for gradient
    }

    public static void main(String[] args) {
        Vector p0 = new Vector(150, 50, -500);//first anchor point
        Vector p1 = new Vector(100, 400, -450);//first control point
        Vector p2 = new Vector(50, -600, 100);//second control point
        Vector p3 = new Vector(-550, -150, -200);//second anchor point
        Bezier curve = new Bezier(p0, p1, p2, p3); //create a curve using p0,p1,p2,p3
 
        Facet f = new Facet();
        f.add(new Vector(0, 0, 0));
        f.add(new Vector(0, 50, 0));
        f.add(new Vector(80, 50, 0));
        f.add(new Vector(80, 0, 0));

        int numStep = 10; //split number of curves

        double angle = Math.PI / 3;

        Extrusion e = new Extrusion(curve, f, numStep);
        e.setPolygonVertices();
        e.getNode();
        e.drawExtrusion();
    }
}
