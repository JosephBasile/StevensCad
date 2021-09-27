/**
 * Triangle.java
 */
package scad;
import static scad.Vector.EPSILON;

/**
 * Represent a triangle face.
 *
 */

public final class Triangle extends Facet {

    /**
     * Constructor. Creates a new triangle with 3 vertices
     *
     * @param a
     * @param b
     * @param c
     */
    public Triangle(Vector a, Vector b, Vector c) {
        super(a, b, c);
    }

    @Override
    public Triangle flip() {
        return (Triangle)super.flip(); //To change body of generated methods, choose Tools | Templates.
    }

}
