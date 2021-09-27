package BinaryCombinations;

import scad.Edge;
import scad.Polyhedron;
import scad.Side;
import scad.Split;
import scad.Vector;

/**
 *
 * @author Dov
 */
public class Intersection extends BinaryPoly {

    public Intersection(Polyhedron a, Polyhedron b) {
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

        a.definitely(b, null, this, unsureB);
        b.definitely(a, null, this, unsureA);

        unsure(unsureA, unsureB);
    }

    private final void unsure(Polyhedron unsureA, Polyhedron unsureB) {
        Split<Polyhedron> split;
        Polyhedron bCleaver = setCleaver(b, unsureB, a);
        Polyhedron aCleaver = setCleaver(a, unsureA, b);
        
        if (!unsureB.isEmpty()) {
            split = bCleaver.cleave(unsureA);

            if (split.hasInside())
                addAll(split.inside().facets);
        }
        if (!unsureA.isEmpty()) {
            split = aCleaver.cleave(unsureB);

            if (split.hasInside())
                addAll(split.inside().facets);
        }
    }

    @Override
    public Side definitely(Vector v) {
        Side aS = a.definitely(v), bS;
        if (aS == Side.OUT || (bS = b.definitely(v)) == Side.OUT)
            return Side.OUT;
        if (aS == Side.SURFACE || bS == Side.SURFACE)
            return Side.SURFACE;
        return Side.IN;
    }

    @Override
    public Side definitely(Edge v) {
        Side aS = a.definitely(v), bS;
        if (aS == Side.OUT || (bS = b.definitely(v)) == Side.OUT)
            return Side.OUT;
        if (aS == Side.SURFACE || bS == Side.SURFACE)
            return Side.SURFACE;
        return Side.IN;
    }

    @Override
    public Split<Polyhedron> cleave(Polyhedron meat) {
        Polyhedron outside = new Polyhedron(), inside = new Polyhedron();
        Split<Polyhedron> aSplit = a.cleave(meat);
        if (aSplit.hasOutside()) outside.addAll(aSplit.outside().facets);
        if(!aSplit.hasInside()) return new Split<>(null, meat);
        Split<Polyhedron> bSplit = b.cleave(aSplit.inside());
        if (bSplit.hasInside()) inside.addAll(bSplit.inside().facets);
        if (bSplit.hasOutside()) outside.addAll(bSplit.outside().facets);
        return new Split<>(inside, outside);
    }
}
