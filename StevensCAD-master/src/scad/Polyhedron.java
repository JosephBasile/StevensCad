package scad;

import BinaryCombinations.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import primitives.ConvexPoly;
import primitives.Shape3d;
import transformation.*;
/**
 * modified by yifan
*/
public class Polyhedron extends Shape3d {
    public ArrayList<Facet> facets;
    

    public ArrayList<Vector> getVertices() {
        return new VertsItr().all();
    }

    public class VertsItr {

        private void setVertices() {
            vertices = new ArrayList<>(facets.size() * 2);
            for (int i = 0; i < facets.size(); i++)
                for (Vector vec : facets.get(i).vertex)
                    if (!vertices.contains(vec))
                        vertices.add(vec);
        }

        int i = 0;

        public VertsItr() {
            if (vertices == null)
                setVertices();
        }

        public boolean done() {
            return i >= vertices.size();
        }

        public void next() {
            i++;
        }

        public Vector val() {
            return vertices.get(i);
        }

        public ArrayList<Vector> all() {
            return vertices;
        }

    }

    public VertsItr getVertsItr() {
        return new VertsItr();
    }

    public int numVertices() {
        VertsItr vi = new VertsItr();
        return vertices.size();
    }

    public Polyhedron(int size) {
        facets = new ArrayList(size);
        vertices = new ArrayList<>(size*3); //TODO: tweak number if average vertices/facet > 3
    }

    public Polyhedron() {
        this(10);
    }

    public Polyhedron flip() {
        Polyhedron flip = new Polyhedron(facets.size());
        for (Facet facet : this.facets)
            flip.add(facet.flip());
        return flip;
    }

    private boolean flatOnPlane(Facet cleavingPlane) {
        for (Facet facet : this.facets)
            if (!facet.onSamePlaneAs(cleavingPlane))
                return false;
        return true;
    }

    public boolean is3D(Facet cleavingPlane) {
        if (facets.isEmpty())
            return false;
        if (flatOnPlane(cleavingPlane))
            return false;
        return true;
    }

    protected BSPTree bspTree;
    protected boolean shuffled = false;

    public boolean shuffle() {
        if (shuffled)
            return false;
        shuffled = true;
        Collections.shuffle(this.facets);
        return true;
    }

    public Split<Polyhedron> cleave(Polyhedron meat) {
        shuffle();
        Polyhedron in = new Polyhedron(), out = new Polyhedron();
        getBspTree().polyCleave(meat, in, out);
        return new Split<>(in, out);
    }

    public Polyhedron difference(Polyhedron p) {
        return new Difference(this, p);
    }

    public Polyhedron union(Polyhedron p) {
        return new Union(this, p);
    }

    public Polyhedron intersect(Polyhedron p) {
        return new Intersection(this, p);
    }

    //TODO: use high speed STL class
    // not StringBuilder which is going to limit sizes to free RAM
    /**
     * Build an STL string in a StringBuilder
     * @param b 
     */
    public void stlString(StringBuilder b) {
        for (Facet f : this.facets)
            f.stlString(b);
    }

    //removes redundancies and empty facets.  Should never need to be called.
    private void clean() {
        for (int facet = 0; facet < facets.size(); facet++) {
            if (facets.get(facet).size() < 3)
                facets.remove(facet);
            facets.get(facet).clean();

        }
    }

    public ArrayList<Triangle> triangles() {
        //clean();
        ArrayList<Triangle> triangles = new ArrayList<>(2 * facets.size());
        for (Facet f : this.facets)
            if (f.isCoPlanar())
                triangles.addAll(f.triangles());
            else
                throw new ArithmeticException("bad facet " + f);
        return triangles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Facet f : this.facets)
            sb.append(f).append("\n");
        return sb.toString();
    }

    @Override
    public Polyhedron clone() {
        Polyhedron clone = sameType();
        clone.facets.ensureCapacity(facets.size());
        facets.forEach((f) -> {
            clone.add(f.clone());
        });
        return clone;
    }

    public boolean add(Facet e) {
        if (bspTree != null)
            bspTree.add(e);
        shuffled = false;
        return facets.add(e); //To change body of generated methods, choose Tools | Templates.
    }

    public BSPTree getBspTree() {
        if (bspTree == null)
            bspTree = new BSPTree(this);
        return bspTree;
    }
    
    public int size() {
        return facets.size();
    }
    
    public void ensureCapacity(int minCapacity) {
        facets.ensureCapacity(minCapacity);
    }
    
    public boolean addAll(List<Facet> c) {
        return facets.addAll(c);
    }
    
    public void clear() {
        facets.clear();
    }
    
    public Facet get(int index) {
        return facets.get(index);
    }
    
    public boolean isEmpty() {
        return facets.isEmpty();
    }
    /**
     * This function should be overriden whenever possible.
     *
     * @param v
     * @return
     */
    public Side definitely(Vector v) {
        return getBspTree().side(v);
    }

    public Side definitely(Edge e) {
        return Side.SURFACE;//TODO: This needs to be made better!
    }

    public final Side definitely(Facet f) {

        int i = 0;

        Side side = definitely(f.edge(i));

        while (i < f.size() && side == Side.IN) {
            i++;
            if (i < f.size()) side = definitely(f.edge(i));
        }

        if (i == f.size()) return Side.IN;

        if (i > 0) return Side.SURFACE;
 
       while (i < f.size() && side == Side.OUT) {
            i++;
            if (i < f.size()) side = definitely(f.edge(i));
        }

        if (i == f.size()) return Side.OUT;

        return Side.SURFACE;
    }

    public final void definitely(Polyhedron p, Polyhedron out, Polyhedron in, Polyhedron unsure) {
        for (Facet f : p.facets)
            switch (definitely(f)) {
                case OUT:
                    if (out != null)
                        out.add(f);
                    break;
                case IN:
                    if (in != null)
                        in.add(f);
                    break;
                case SURFACE:
                    if (unsure != null)
                        unsure.add(f);
            }
    }

    public Polyhedron sameType() {
        return new Polyhedron();
    }

    public int badFacets() {
        int numBadFacets = 0;
        for (Facet f : this.facets) if (!f.isCoPlanar()) numBadFacets++;
        return numBadFacets;
    }
    
    /**
     * Transform vectors of the polyhedron one by one
     *
     * @param t the transformation matrix
     * @return this transformation
     */
    public Polyhedron transform(Transformation t) {
        for(VertsItr v= getVertsItr();!v.done();v.next()){
            t.transform(v.val());
        }
        return this;
    }
    public void applyTransform() {
        for(Facet f : facets) {
            for(Vector v : f.vertex) {
                t.transform(v);
            }
        }
    }

}
