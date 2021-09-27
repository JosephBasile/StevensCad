package test;

import BinaryCombinations.*;
import primitives.*;
import scad.*;
import transformation.*;
import viewer.CADEditor;
import extrusion.*;
import java.io.*;

/**
 *
 * @author dov
 */
public class Test {
/*
    public Polyhedron p;
    public double time;

    public Test(Polyhedron p, double time) {
        this.p = p;
        this.time = time;
    }

    public static Test translateTest(int res) {
        int r = 200;
        int dist = 150;

        Polyhedron p = new Sphere(r, res);

        long T0 = System.nanoTime();
        p.translateX(dist);
        return new Test(p, (System.nanoTime() - T0) * 1e-9);
    }

    public static Test rotateTest() {
        int r = 200;

        Cube p = new Cube(r);

        long T0 = System.nanoTime();
        p.rotateZ(Math.PI / 4);

        return new Test(p, (System.nanoTime() - T0) * 1e-9);
    }

    public static Test unionTest(int res) {

        int r = 400;

        Sphere s1 = new Sphere(r, res);
        Polyhedron s2 = new Sphere(r, res).translateX(3 * r / 2);

        long T0 = System.nanoTime();
        Polyhedron p = new Union(s1, s2);

        return new Test(p, (System.nanoTime() - T0) * 1e-9);
    }

    public static Test differenceTest(int res) {

        int r = 300;

        Sphere s1 = new Sphere(r, res);
        Polyhedron s2 = new Cylinder(r / 2, 2 * r, res);

        long T0 = System.nanoTime();
        Polyhedron p = new Difference(s1, s2);

        return new Test(p, (System.nanoTime() - T0) * 1e-9);
    }

    public static double averageTime(Test... a) {
        int sum = 0;
        for (int i = 1; i < a.length; i++)
            sum += a[i].time;
        CADViewer.openWindow(a[0].p);
        return sum * 1.0 / a.length;
    }

    public static Test intersectionTest(int res) {
        long T0 = System.nanoTime();

        int r = 200;
        int dist = 150;

        Sphere s1 = new Sphere(r, res);

        Polyhedron s2 = new Sphere(r, res).translateX(r/ 2);

        Polyhedron p = new Intersection(s1, s2);

        return new Test(p, (System.nanoTime() - T0) * 1e-9);
    }

//    public static Test ConvexHullTest(int res) {
//        long T0 = System.nanoTime();
//
//        int r = 200;
//        int dist = 150;
//
//        Sphere s1 = new Sphere(r, res);
//        Polyhedron s2 = new Cube(r / 5).translate(new Vector(2 * r, 0, 0));
//
//        Polyhedron p = new ConvexHull_origin(new Union(s1, s2));
//
//        return new Test(p, (System.nanoTime() - T0) * 1e-9);
//    }
//
    public static Test multiBinaryTest(int res) {
        return multiBinaryTest(res, res);
    }
    public static Test multiBinaryTest(int sRes, int cRes) {//Do not change this test.  It reveals an important bug if I use unsureB instead of b in Union.unsure().
        Polyhedron c = new Sphere(100, sRes);
        Polyhedron c1 = new Cylinder(30, 230, cRes);
        Polyhedron c2 = new Cylinder(30, 230, cRes).rotateY(Math.PI / 2);

        long T0 = System.nanoTime();

        Polyhedron d = c.intersect(c2.union(c1));

        return new Test(d, (System.nanoTime() - T0) * 1e-9);

    }
    
    public static Test multiSphereTest(int n, int res){
        long T0 = System.nanoTime();
        Polyhedron p = new Sphere(100, res);
        for(int i = 1; i < n; i++)
            p = p.union(new Sphere(100, res).translateX(i*50));
        return new Test(p, (System.nanoTime() - T0) * 1e-9);
    }
    
    
    public static Test crossCylinderTest(int res) {
        Polyhedron c1 = new Cylinder(30, 300, res);
        Polyhedron c2 = new Cylinder(30, 200, res).rotateY(Math.PI / 2);

        long T0 = System.nanoTime();

        Polyhedron c3 = c1.union(c2);

        return new Test(c3, (System.nanoTime() - T0) * 1e-9);

    }

    public static void SphereTransformDefinitelyTest() {
        Polyhedron s = new Sphere(100, 100).translateX(201).translateX(100);
        System.out.println(s.definitely(new Vector(300, 0, 0)));
    }

    
    public static void extrusionTest(){ 
        // curve1
//        Vector p0 = new Vector(0, 0, 0);//first anchor point
//        Vector p1 = new Vector(0, 400, 0);//first control point
//        Vector p2 = new Vector(400, 400, 0);//second control point
//        Vector p3 = new Vector(400, 0, 0);//second anchor point
       
        //curve2
        Vector p0 = new Vector(100, 250, -100);//first anchor point
        Vector p1 = new Vector(220, 120, 0);//first control point
        Vector p2 = new Vector(270, 370, 0);//second control point
        Vector p3 = new Vector(400, 250, 0);//second anchor point
        
        
        // straight line
//        Vector p0 = new Vector(0,0,-200);//first anchor point
//        Vector p1 = new Vector(0,0,1);//first anchor point
//        Vector p2 = new Vector(0,0,50);//first anchor point
//        Vector p3 = new Vector(0,0,300);//first anchor point

        Bezier curve = new Bezier(p0,p1,p2,p3); //create a curve using p0,p1,p2,p3
        
        // triangle
        Facet tria = new Facet();
        tria.add(new Vector(0, 0, 0));
        tria.add(new Vector(50, 0, 0));
        tria.add(new Vector(0,50, 0));
        
        // star
        Facet star = new Facet();
        double sin36 = Math.sin(Math.PI/5);
        double cos36 = Math.cos(Math.PI/5); 
        double sin54 = Math.sin(Math.PI * 3 / 10);
        double cos54 = Math.cos(Math.PI * 3 / 10);
        double sin72 = Math.sin(Math.PI/5*2);
        double cos72 = Math.cos(Math.PI/5*2);
      
        double r = 10;
        double L1=r*cos36;
        double L2=r*sin36;
        double L3=r*cos72;
        double L4=r*sin72;
        double L5=L2*sin72/cos72;
        double L6=L2/cos72;
        double L7=L6*sin54;
        double L8=L6*cos54+r;
        
        star.add(new Vector(0, L5+L1, 0));
        star.add(new Vector(L2, L1, 0));
        star.add(new Vector(L2 + L6, L1, 0));
        star.add(new Vector(L4, -L3, 0));
        star.add(new Vector(L7, -L8, 0));
        star.add(new Vector(0, -r, 0));
        star.add(new Vector(-L7, -L8, 0));
        star.add(new Vector(-L4, -L3, 0));
        star.add(new Vector(-L2 - L6, L1, 0));
        star.add(new Vector(-L2, L1, 0));
        
        star = star.flip();
        
        // rectangular
        Facet rect = new Facet();
        rect.add(new Vector(1,1,0));
        rect.add(new Vector(100, 0, 0));
        rect.add(new Vector(80,20,0));
        rect.add(new Vector(100,50,0));
        rect.add(new Vector(0,50,0));
        
        
        int split_num = 100; //split number of curves
        double angle = Math.PI/90;
//        double angle = 0;
        
        double[] scaleNum = {10, 10, 10};  
        Extrusion p = new Extrusion(curve, star, split_num, angle);
//        Extrusion p = new Extrusion(curve,5,split_num,scaleNum,angle);
        
         System.out.println(p);
        
        CADViewer.openWindow(p);
    }
*/    
    public static void main(String[] args) throws IOException{
        
        FileReader f = new FileReader(new File("test.stl"));
        STL.readSTL1(f);
//
//        Test t = multiSphereTest(4, 100);
//        System.out.println(t.time);
//        CADViewer.openWindow(t.p);
//        Polyhedron c1 = new Cylinder(30, 300, 3).rotateY(Math.PI/2);
//        CADViewer.openWindow(c1);
//        System.out.println(c1.numVertices());
    }

    
}
