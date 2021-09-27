package BinaryCombinations;

import primitives.ConvexPoly;
import scad.Polyhedron;
import transformation.Transformation;

/**
 *
 * @author Dov
 */
public abstract class BinaryPoly extends Polyhedron {

    final protected Polyhedron a, b;

    public BinaryPoly(Polyhedron a, Polyhedron b) {
        super(a.size() + b.size());
        this.a = a; this.b = b;
    }

    protected abstract void init();
    
    @Override
    public Polyhedron transform(Transformation t) {
//TODO: There has to be a better way to do this.
        a.transform(t);
        b.transform(t);
        init();
        return this;
    }
    
    protected Polyhedron setCleaver(Polyhedron base, Polyhedron edge, Polyhedron meat){
        if(base instanceof ConvexPoly && meat instanceof ConvexPoly) return edge;
        else return base;
    }
    
}
