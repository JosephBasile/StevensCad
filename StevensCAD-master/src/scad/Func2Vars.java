package scad;

/**
 * A function of 2 variables that returns doubles
 * for use in creating attributes of 3d surfaces
 * @author dkruger
 */
public interface Func2Vars {
    public double f(double u, double v);
}
