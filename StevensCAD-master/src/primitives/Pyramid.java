package primitives;

import java.util.ArrayList;
import scad.Facet;
import scad.Polyhedron;
import scad.Vector;


/**
 *
 * @author dov
 */
public class Pyramid extends AnalyticalShape3d {
    private Facet base;
    
    public Pyramid(Facet base, Vector tip, boolean includeBase) {
        if(!base.below(tip)) base = base.flip();
        this.base = base;
        if(includeBase) poly.facets.add(base);
        for(int i = 0; i < base.size(); i++)
            poly.facets.add(new Facet(base.get(i), base.sGet(i - 1), tip));
    }
    public Pyramid(Facet base, Vector tip) {
        this(base, tip, true);
    }
    

    public Facet getBase() {
        return base;
    }        
    
}
