package scad.testOperations;

import primitives.Shape;
import scad.Operations;
import scad.Polyhedron;
import scad.Vector;

/**
 *
 * @author itay
 */
public class DovOperations implements Operations {

    @Override
    public Shape union(Shape a, Shape b) {
        throw new UnsupportedOperationException();
//        return a.union(b);
    }

    @Override
    public Shape intersection(Shape a, Shape b) {
        throw new UnsupportedOperationException();
//        return a.intersect(b);
    }

    @Override
    public Shape difference(Shape a, Shape b) {
        throw new UnsupportedOperationException();
//        return a.difference(b);
    }

    @Override
    public Shape convexHull(Shape a, Shape b) {
        throw new UnsupportedOperationException();

        //        return new ConvexHull(a.union(b));
    }

    @Override
    public Shape multiunion(Shape... shapes) {
        throw new UnsupportedOperationException();
//        Shape multiUnion = new Polyhedron();
//        for (int i = 1; i < shapes.length; i++)
//            multiUnion = multiUnion.union(shapes[i]);
//        return multiUnion;
    }

    @Override
    public Shape multiintersection(Shape... shapes) {
        throw new UnsupportedOperationException();
//        Shape multiIntersection = new Polyhedron();
//        for (int i = 1; i < shapes.length; i++)
//            multiIntersection = multiIntersection.intersect(shapes[i]);
//        return multiIntersection;
    }

    @Override
    public Shape multiDifference(Shape[] shapes) {
        throw new UnsupportedOperationException();
//        Shape multiDif = new Polyhedron();
//        for (int i = 1; i < shapes.length; i++)
//            multiDif = multiDif.difference(shapes[i]);
//        return multiDif;
    }

    @Override
    public Shape mirror(Shape shape, boolean mirX, boolean mirY, boolean mirZ) {
        return shape.mirror(mirX, mirY, mirZ);
    }

    @Override
    public Shape mirror(Shape shape, Vector v) {
//        return shape.mirror(v);
        throw new UnsupportedOperationException();
    }

    @Override
    public Shape scale(Shape shape, double x, double y, double z) {
//        return shape.scale(x, y, z);
        throw new UnsupportedOperationException();
    }

}
