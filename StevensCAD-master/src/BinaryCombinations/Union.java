package BinaryCombinations;

import primitives.ConvexPoly;
import primitives.Sphere;
import scad.Edge;
import scad.Polyhedron;
import scad.Side;
import scad.Split;
import scad.Vector;

/**
 *
 * @author Dov
 */
public class Union extends BinaryPoly {

    public Union(Polyhedron a, Polyhedron b) {
        super(a, b);
        init();
    }
 
    @Override 
    protected final void init() {
        clear();
        Polyhedron unsureA = a.sameType(),
                unsureB = b.sameType();

        unsureA.ensureCapacity(a.size());
        unsureB.ensureCapacity(b.size());

        a.definitely(b, this, null, unsureB);
        b.definitely(a, this, null, unsureA);
        
        unsure(unsureA, unsureB);
    }
 

    private void unsure(Polyhedron unsureA, Polyhedron unsureB) {
        Split<Polyhedron> split;

        Polyhedron bCleaver = setCleaver(b, unsureB, a);
        Polyhedron aCleaver = setCleaver(a, unsureA, b);
        
        if (!unsureB.isEmpty()) {
            split = bCleaver.cleave(unsureA);//TODO: Maybe this can be unsureB instead of unsure B? Run Test.multiBinaryTest() on different resolutions.
            //unsures must only interact with their neigboring unsures and not the neigbors of forign convex parts.
            
            if (split.hasOutside())
                addAll(split.outside().facets);
        }
        if (!unsureA.isEmpty()) {
            split = aCleaver.cleave(unsureB);

            if (split.hasOutside())
                addAll(split.outside().facets);
        }
    }

    
    
    @Override
    public Side definitely(Vector v) {
        Side aS = a.definitely(v), bS;
        if (aS == Side.IN || (bS = b.definitely(v)) == Side.IN)
            return Side.IN;
        if (aS == Side.SURFACE || bS == Side.SURFACE)
            return Side.SURFACE;
        return Side.OUT;
    }

    @Override
    public Side definitely(Edge v) {
        Side aS = a.definitely(v), bS;
        if (aS == Side.IN || (bS = b.definitely(v)) == Side.IN)
            return Side.IN;
        if (aS == Side.SURFACE || bS == Side.SURFACE)
            return Side.SURFACE;
        return Side.OUT;
    }

    @Override
    public Split<Polyhedron> cleave(Polyhedron meat) {
        Polyhedron outside = new Polyhedron(), inside = new Polyhedron();
        Split<Polyhedron> aSplit = a.cleave(meat);
        if(aSplit.hasInside()) inside.addAll(aSplit.inside().facets);
        if(!aSplit.hasOutside()) return new Split<>(meat, null);
        Split<Polyhedron> bSplit = b.cleave(aSplit.outside());
        if(bSplit.hasInside()) inside.addAll(bSplit.inside().facets);
        if(bSplit.hasOutside()) outside.addAll(bSplit.outside().facets);
        return new Split<>(inside, outside);        
    }
    

}
