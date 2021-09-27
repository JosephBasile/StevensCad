package scad;

/**
 * Represent a solid shell composed of a Surface3d with a defined thickness
 * @author dkruger
 */
public interface Shell extends Surface3d {
    /**
     * Generate a shell where the thickness is a function of (u,v)
     * @param thickness
     * @return 
     */
    public Polyhedron generate(Func2Vars thickness);
    
    /**
     * Generate a shell where the thickness is a constant
     * @param thickness
     * @return 
     */
    public Polyhedron generate(double thickness);
    
    /**
     * Generate a shell where the thickness is a a 2d array
     * @param thickness
     * @return 
     */
    public Polyhedron generate(double dx, double dy, double[][] thickness);
    
}
