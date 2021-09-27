package primitives;

import scad.Vector;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;
import scad.Edge;
import scad.Facet;
import scad.Polyhedron;
import scad.Side;
import scad.Vector;
import transformation.Transformation;
import static scad.Vector.EPSILON;
import scad.hull.ConvexHull;

/**
 * Represent a spherical shell with an inside radius
 * and an outside radius that depends on a map.
 * This allows construction of a sphere with raised (or lowered)
 * features.
 *
 * @author dkruger
 */
//TODO: This class exposes the flaws in the object hierarchy.
// this needs to be rethought.
// the easy implementation would be two spheres: outer and inner
// with outer - inner.  But this will not work with the current hierarchy.
public class BumpMapSphericalShell extends Sphere implements Cloneable {
    /**
     * Create a spherical shell with a matrix of elevations modifying the radius
     * The matrix is in cylindrical coordinates, so the poles need only a single value
     * while every row requires res entries
     * @param radius  base radius of sphere
     * @param res     resolution of grid
     * @param h       
     */
    public BumpMapSphericalShell(double radius, int res, double[][] h) {
        this(radius, res, res, h);
    }

    /**
     * Create a spherical shell with a matrix of elevations modifying the radius
     * The matrix is in cylindrical coordinates, so the poles need only a single value
     * while every row requires res entries
     * @param radius   base radius of sphere
     * @param longres  resolution of grid
     * @param latres   resolution of grid
     * @param h       
     */
    public BumpMapSphericalShell(double radius, int longRes, int latRes, double[][] h) {
        super(radius, longRes, latRes);
        double chord = 2 * r * sin(Math.PI / Math.min(longRes, latRes));
        rMin = Math.sqrt(r * r - chord * chord / 4);
        ArrayList<Vector> verts = new ArrayList<>(longRes * latRes + 2);

        latRes /= 2;

        // first compute a broad band around the sphere excluding the poles
        final double dphi = PI / latRes; // vertically only +90 to -90 degrees
        double phi = -PI / 2 + dphi;

        for (int j = 0; j < latRes - 1; ++j, phi += dphi) {
            double theta = 0;
            final double dtheta = 2 * PI / longRes;
            for (int i = 0; i < longRes; ++i, theta += dtheta) {
                double zInner = radius * sin(phi);
                double r2Inner = radius * cos(phi);
                double xInner = r2Inner * cos(theta), yInner = r2Inner * sin(theta);
                verts.add(new Vector(xInner, yInner, zInner)); // first compute all points
                
                double rOuter = radius + h[j][i];
                double z = rOuter * sin(phi);
                double r2 = rOuter * cos(phi);

                double x = r2 * cos(theta), y = r2 * sin(theta);
                verts.add(new Vector(x, y, z)); // first compute all points
            }
        }
/*
        // then create quads with all the points created
        for (int i = 0; i < verts.size() - longRes; i++)
            addSquare(i + longRes,
                    i,
                    (i % longRes == longRes - 1
                            ? -longRes : 0) + i + 1, //bottom right
                    (i % longRes == longRes - 1
                            ? -longRes : 0) + i + longRes + 1,//top right 

                    verts);
*/
        final Vector south = new Vector(0, 0, -r),
                north = new Vector(0, 0, r);
        for (int i = 0; i < longRes - 1; i++) {
            poly.add(new Facet(verts.get(i), south, verts.get(i + 1)));
            poly.add(new Facet(verts.get(i + verts.size() - longRes), verts.get(i + verts.size() - longRes + 1), north));
        }
        poly.add(new Facet(verts.get(longRes - 1), south, verts.get(0)));
        poly.add(new Facet(verts.get(verts.size() - longRes), north, verts.get(verts.size() - 1)));

        poly.vertices = verts;
        poly.vertices.add(north);
        poly.vertices.add(south);

    }
    
    //TODO: Figure out where this goes
    /**
     * Add a cube to this polyhedron.  This probably needs to be implemented
     * further up the hierarchy
     * @param c1
     * @param c2
     * @param c3
     * @param c4
     * @param c5
     * @param c6
     * @param c7
     * @param c8 
     */
    public void addRectPrism
        (Vector c1, Vector c2, Vector c3, Vector c4,
         Vector c5, Vector c6, Vector c7, Vector c8) {
            ArrayList<Vector> v = new ArrayList<>();
            v.add(c1);v.add(c2);v.add(c3);v.add(c4);v.add(c5);v.add(c6);
            v.add(c7);v.add(c8);
            poly.addAll(new ConvexHull(v).facets);
    }
    
    @Override
    public BumpMapSphericalShell clone() {
        BumpMapSphericalShell clone = (BumpMapSphericalShell)super.clone();
        return clone;
    }

}
