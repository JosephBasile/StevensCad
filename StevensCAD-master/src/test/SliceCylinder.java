package test;

import primitives.Cylinder;
import scad.PlaneSurface;
import scad.Polyhedron;
import scad.STL;

/**
 *
 * @author dkruger
 */
public class SliceCylinder {
    public static void main(String[] args) {
        Cylinder c = new Cylinder(15.0, 50.0, 60);
        PlaneSurface cut = new PlaneSurface(15, 2, 1, 1); // TODO: define some orientation to cut cylinder on a bias
        Polyhedron slice = null; // TODO: cut.greaterThan(c);
        STL.writeBinarySTL(slice, "slicedcylinder.stlb");
    }
}
