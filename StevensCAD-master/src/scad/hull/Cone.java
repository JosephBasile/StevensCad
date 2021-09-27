package scad.hull;

import java.util.List;
import primitives.Pyramid;
import scad.Edge;
import scad.Facet;
import scad.Polyhedron;
import scad.Vector;

/**
 *
 * @author dov
 */
public class Cone extends Polyhedron{

    public Cone(List<Facet> bases, Vector tip) {
        
        ClosedPath cp = new ClosedPath(bases);
        for(Edge edge: cp) add(new Facet(edge.a, edge.b, tip));

    }
    private boolean hasBadPoint(Facet testing, Facet surface){
        for(Vector p: surface.vertex) if(!testing.below(p)) return true;
        return false;
    }
        
}
