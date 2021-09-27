package primitives;

import scad.Edge;
import scad.Facet;
import scad.Polyhedron;
import scad.Side;
import scad.Split;
import scad.Vector;

/**
 *
 * @author Dov
 */
public class ConvexPoly extends Polyhedron {

    public ConvexPoly(int i) {
        super(i);
    }

    public ConvexPoly() {
    }

    @Override
    public Side definitely(Vector v) {
        int i;
        boolean surface = true;
        for (i = 0; i < size(); i++) {
            Side s = get(i).side(v);
            if (s == Side.OUT)
                return Side.OUT;
            if (s == Side.SURFACE)
                surface = true;
        }
        if (surface)
            return Side.SURFACE;
        return Side.IN;
    }

    @Override
    public Polyhedron sameType() {
        return new ConvexPoly();
    }

    @Override
    public Split<Polyhedron> cleave(Polyhedron meat) {
        shuffle();
        Polyhedron outside = new Polyhedron(meat.size()),
                stillInside = new Polyhedron(meat.size());
        stillInside.addAll(meat.facets);

        for (Facet cleaver : this.facets) {
            if(stillInside.isEmpty()) break;
            
            Split<Polyhedron> split = cleaver.cleave(stillInside);
            
            if (split.hasOutside()) outside.addAll(split.outside().facets);
            if (split.hasInside()) stillInside = split.inside();
        }
        if(stillInside.isEmpty()) stillInside = null;
        if(outside.isEmpty()) outside = null;
        return new Split<>(stillInside, outside);
    }
    
    
    @Override
    public final Side definitely(Edge e) {
        
        boolean a = definitelyIn(e.a), b = definitelyIn(e.b);
        
        if(a && b) return Side.IN;
        if(a || b) return Side.SURFACE;
        if(definitelyOut(e)) return Side.OUT;
        
        return Side.SURFACE;
    }
    
    protected boolean definitelyOut(Edge e) {
        for(Facet f : this.facets){
            Side a = f.side(e.a), b = f.side(e.b);
            if(a == Side.OUT && b == Side.OUT) return true;
        }
        return false;
    }
    
    public boolean definitelyIn(Vector v) {
        for(Facet f: this.facets) if(f.above(v)) return false;
        return true;
    }

}
