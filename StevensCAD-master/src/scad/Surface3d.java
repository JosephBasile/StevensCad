package scad;

/**
 * Represent a 3d surface which can be used to divide space (inside/outside)
 * and also to create a solid shell by mapping a thickness at each point
 * @author dkruger
 * @modified by Alessandro Della Seta
 */
public interface Surface3d {
    public double getMinU();
    public double getMaxU();
    public double getMinV();
    public double getMaxV();
    public Vector mapSurface(int u, int v);
    public boolean greaterThan(Vector v);
}
 