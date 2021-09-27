package scad;

import java.io.Serializable;
import java.util.ArrayList;
import static scad.Vector.EPSILON;
import transformation.Transformation;

/**
 * counter clockwise. We assume unicity of vertices, i.e. no vertex coordinates
 * are duplicated and adjacent loops share common vertices.Facets are assumed
 * convex and described with a single loop. https://arxiv.org/pdf/1706.01558.pdf
 *
 * @author dov
 * modified by yifan
 */
public class Facet implements Serializable, Cloneable{
    public ArrayList<Vector> vertex;
    public Facet(int size) {
        vertex = new ArrayList<>(size);
    }

    public Facet(Vector v1, Vector v2, Vector v3) {
        this(3);
        vertex.add(v1);
        vertex.add(v2);
        vertex.add(v3);
    }

    public Facet(Vector v1, Vector v2, Vector v3, Vector v4) {
        this(4);
        vertex.add(v1);
        vertex.add(v2);
        vertex.add(v3);
        vertex.add(v4);
    }

    //TODO: for speed we should store the normal in the facet and not create each time
    public Vector normal() {
        return Angle.normal(vertex.get(1), 
                            vertex.get(vertex.size() - 1), 
                            vertex.get(0));
    }
    /**
     * warp add method for other classes
     * 
     */
    public void add(Vector v) {
        vertex.add(v);
    } 
    
    public Vector get(int index) {
        return vertex.get(index);
    }
    
    public int size() {
        return vertex.size();
    }
     /**
     * must be added in order.
     *
     * @param e
     * @return
     */
    public void add(Edge e) {
        if (vertex.isEmpty() || e.a != vertex.get(vertex.size() - 1)) {
            vertex.add(e.a);
            if (e.b != vertex.get(0))
                vertex.add(e.b);
        } else if (e.b != vertex.get(0))
            vertex.add(e.b);
    }

    public ArrayList<Triangle> triangles() {
        ArrayList<Triangle> triangles = new ArrayList<>(vertex.size() - 2);
        for (int i = 1; i < vertex.size() - 1; i++)
            triangles.add(new Triangle(vertex.get(0), 
                                       vertex.get(i), 
                                       vertex.get(i + 1)));
        return triangles;
    }

    public Edge edge(int i) {
        if (i == vertex.size() - 1)
            return new Edge(vertex.get(i), vertex.get(0));
        return new Edge(vertex.get(i), vertex.get(i + 1));
    }

    public Facet flip() {
        Facet flip = new Facet(vertex.size());
        for (int i = 0; i < vertex.size(); i++)
            flip.vertex.add(vertex.get(vertex.size() - 1 - i));
        return flip;
    }

    private boolean allSide(Side side, Facet meat) {
        for (Vector v : meat.vertex)
            if (side(v) != side) return false;
        return true;
    }

    private void addToBoth(Vector v, Facet a, Facet b) {
        a.vertex.add(v);
        b.vertex.add(v);
    }

    /**
     * The point relative to this facet.
     *
     * @param v
     * @return
     */
    public double compare(Vector v) {
        return v.minus(vertex.get(0)).dot(normal());
    }

    /**
     * The point relative to this facet.
     *
     * @param v
     * @return
     */
    public boolean below(Vector v) {
        return compare(v) < -EPSILON;
    }

    /**
     * The point relative to this facet.
     *
     * @param v
     * @return
     */
    public boolean above(Vector v) {
        return compare(v) > EPSILON;
    }

    /**
     * The point relative to this facet.
     *
     * @param v
     * @return
     */
    public Side side(Vector v) {
        double c = compare(v);
        if (c > EPSILON)
            return Side.OUT;
        if (c < -EPSILON)
            return Side.IN;
        return Side.SURFACE;
    }

    public boolean coPlanar(Vector v) {
        return Math.abs(compare(v)) < EPSILON;
    }

    public void cleave(Facet meat, Polyhedron in, Polyhedron out) {
        if (allSide(Side.OUT, meat)) out.add(meat);
        else if (allSide(Side.IN, meat)) in.add(meat);
        else {
            Split<Facet> split = cleave(meat);
            if (split.hasInside()) in.add(split.inside());
            if (split.hasOutside()) out.add(split.outside());
        }
    }

    public Split<Facet> cleave(Facet meat) {

        Facet in = new Facet(vertex.size()), out = new Facet(vertex.size());

        Side compare, nextCompare = side(meat.vertex.get(0));

        for (int i = 0; i < meat.vertex.size(); i++) {

            compare = nextCompare;
            nextCompare = side(meat.sGet(i + 1));

            switch (compare) {
                case OUT:
                    out.vertex.add(meat.vertex.get(i));
                    if (nextCompare == Side.IN)
                        addToBoth(intersect(meat.edge(i)), out, in);
                    break;
                case IN:
                    in.vertex.add(meat.vertex.get(i));
                    if (nextCompare == Side.OUT)
                        addToBoth(intersect(meat.edge(i)), out, in);

                    break;
                case SURFACE:
                    Side prevCompare = side(meat.sGet(i - 1));

                    if (prevCompare == Side.OUT && nextCompare == Side.OUT)
                        out.vertex.add(meat.vertex.get(i));
                    else if (prevCompare == Side.IN && nextCompare == Side.IN)
                        in.vertex.add(meat.vertex.get(i));
                    else if (prevCompare == Side.SURFACE && nextCompare == Side.SURFACE)
                        return new Split<>(this, this);
                    else addToBoth(meat.vertex.get(i), out, in);

            }
        }

        if (in.vertex.size() < 3) in = null;
        if (out.vertex.size() < 3) out = null;
        return new Split<>(in, out);

    }

    public Vector intersect(Edge e) {
        double den = (normal().dot(e.a) - normal().dot(e.b));
        return e.at((normal().dot(vertex.get(1)) - normal().dot(e.b)) / den);
    }

    public Split<Edge> cleave(Edge e) {

        Edge inside = null, outside = null;
        Side a = side(e.a), b = side(e.b);

        if (a == Side.SURFACE && b == Side.SURFACE)
            inside = outside = e;
        else if (a == Side.IN && b == Side.IN)
            inside = e;
        else if (a == Side.OUT && b == Side.OUT)
            outside = e;
        else {
            Vector intersection = intersect(e);
            if (a == Side.IN) {
                inside = new Edge(e.a, intersection);
                outside = new Edge(intersection, e.b);
            } else {
                outside = new Edge(e.a, intersection);
                inside = new Edge(intersection, e.b);
            }
            if (inside.length() < EPSILON)
                inside = null;
            if (outside.length() < EPSILON)
                outside = null;
        }
        return new Split(inside, outside);
    }

    public Split<Polyhedron> cleave(Polyhedron meat) {//warning, these are not complete polyhedron
        Polyhedron inside = new Polyhedron(vertex.size());
        Polyhedron outside = new Polyhedron(vertex.size());

        for (Facet meatFacet : meat.facets) cleave(meatFacet, inside, outside);

        if (!inside.is3D(this)) inside = null;
        if (!outside.is3D(this)) outside = null;

        return new Split<>(inside, outside);
    }

    public void stlString(StringBuilder sb) {
        ArrayList<Triangle> triangles = triangles();
        for (Triangle tri : triangles) {
            sb.append("\nfacet normal");
            normal().stlString(sb);
            sb.append("\nouter loop");
            for (Vector v : tri.vertex) {
                sb.append("\nvertex");
                v.stlString(sb);
            }
            sb.append("\nendloop\nendfacet\n");
        }
    }

    public Facet clean() {

        for (int i = 0; i < vertex.size(); i++)
            for (int j = i + 1; j < vertex.size(); j++)
                if (vertex.get(i).equals(vertex.get(j)))
                    vertex.remove(j);

        for (int i = 0; i < vertex.size(); i++)
            if (sGet(i + 1).isCollinear(vertex.get(i), sGet(i + 2)))
                vertex.remove(sIndex(i));

        if (vertex.size() < 3 || !isCoPlanar())
            return null;

        return this;
    }

    public boolean isCoPlanar() {
        if (vertex.size() < 3)
            return false;

        for (int i = 1; i < vertex.size(); i++)
            if (normal().cross(Angle.normal(sGet(i + 1),
                                            vertex.get(i - 1),
                                            vertex.get(i)))
                    .abs() > Math.max((edge(0).length()) * EPSILON, EPSILON)) {
                System.err.println("facet is not coplanar, the descrepancy is = " +
                        normal().cross(Angle.normal(sGet(i + 1),
                                                    vertex.get(i - 1),
                                                    vertex.get(i))).abs());
                return false;
            }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!isCoPlanar())
            sb.append("not vallid\n");
        sb.append("size = ").append(vertex.size()).append('\n');
        for (Vector vec : this.vertex)
            sb.append(vec).append('\n');
        return sb.append('\n').toString();
    }

    @Override
    public Facet clone() {
        Facet clone = new Facet(vertex.size());
        for (Vector v : this.vertex)
            clone.vertex.add(v.clone());
        return clone;
    }

    /**
     * merges in a facet that shares to consecutive points with this one.
     *
     * @param f
     */
    public void mergeIn(Facet f) {
        //TODO:This can be made faster!  And, needs to be tested.  I suspect it does not work.

        if (f.contains(vertex.get(0)) &&
            f.contains(vertex.get(vertex.size() - 1)))
            vertex.add(0, vertex.get(vertex.size() - 1));
        for (int i = 0; i < vertex.size(); i++)
            if (f.contains(vertex.get(i))) {
                for (int j = (f.vertex.indexOf(vertex.get(i)) + 1) % f.vertex.size(), k = 0; 
                     k < f.vertex.size() - 2; j = (j + 1) % f.vertex.size(), k++)
                    vertex.add(i + k, f.vertex.get(j));
                break;
            }
        if (clean() == null)
            throw new ArithmeticException("Facet cleaned to elimination.");
    }

    public boolean equals(Facet o) {
        if (!o.contains(vertex.get(0)))
            return false;
        clean();
        o.clean();
        for (int i = 0, j = o.vertex.indexOf(vertex.get(0));
             i < vertex.size(); 
             i++, j = (j + 1) % vertex.size())
            if (!vertex.get(i).equals(o.vertex.get(j)))
                return false;
        return true;
    }

//    public boolean safeAdd(Vector vec){
//        if(size() >= 3 && !vec.coPlanar(this)) return false;
//        
//    }
    public Vector midPoint() {
        Vector mid = new Vector(0, 0, 0);
        for (Vector vec : this.vertex)
            mid = mid.plus(vec);
        return mid.times(1.0 / vertex.size());
    }

    public boolean contains(Vector p) {
        if (!coPlanar(p))
            return false;
        for (int i = 0; i < vertex.size(); i++)
            if (edgeWall(i).above(p))
                return false;
        return true;
    }

    public boolean onEdge(Vector p) {
        for (int i = 0; i < vertex.size() - 1; i++)
            if (edge(i).contains(p))
                return true;
        return false;
    }

    /**
     * safe get treats the facet as a circle
     *
     * @param i
     * @return
     */
    public Vector sGet(int i) {
        return vertex.get(sIndex(i));
    }

    private int sIndex(int i) {
        if (i < 0)
            return sIndex(vertex.size() + i);
        return i % vertex.size();
    }

    public static Facet testFacet() {
        return new Facet(new Vector(0, 0, 0), new Vector(1, 0, 0), new Vector(0, 1, 0));
    }

    public boolean convexCheck() {
        Vector test = normal().normalized();
        for (int i = 1; i < vertex.size(); i++)
            if (!test.equals(Angle.normal(vertex.get(i - 1), vertex.get(i), sGet(i + 1)).normalized()))
                return false;
        return true;
    }

    public Angle getAngle(int i) {
        return new Angle(sGet(i + 1), sGet(i - 1), sGet(i));
    }

    public Facet edgeWall(int i) {
        Vector out = Angle.normal(sGet(i - 1), sGet(i + 1), sGet(i));
        return new Facet(sGet(i), out.plus(sGet(i)), sGet(i + 1));
    }

    private int belowFacet(Vector insertion) {
        int i = 0;
        for (int j = 0; j < vertex.size() && edgeWall(i).above(insertion); j++, i--);
        if (i == -vertex.size())
            throw new ArithmeticException("Bad Facet");
        return i;
    }

    private int firstAboveFacet(Vector insertion, int i) {
        for (int j = 0; j < vertex.size() && edgeWall(i).below(insertion); j++, i++);
        return i;
    }

    /**
     * insert a point into a facet when it may disrupt the shapes convexity.
     *
     * @param insertion
     */
    public void sInsert(Vector insertion) {
        if (vertex.size() < 3) {
            vertex.add(insertion);
            return;
        }

        if (!coPlanar(insertion))
            throw new ArithmeticException("not coplanar");

        int i = firstAboveFacet(insertion, belowFacet(insertion));
        if (i == vertex.size())
            return;

        while (edgeWall(i + 1).above(insertion) && vertex.size() > 2)
            vertex.remove(sIndex(i + 1));

        vertex.add(sIndex(i + 1), insertion);
    }

    /**
     * Use to insert a point when it is known the point will not disrupt the
     * shapes convexity.
     *
     * @param insertion
     */
    public void insert(Vector insertion) {

        Angle max = new Angle(vertex.get(0), vertex.get(1), insertion);
        int maxInd = 0;

        for (int i = 1; i < vertex.size(); i++) {
            Angle temp = new Angle(vertex.get(i), sGet(i + 1), insertion);
            if (temp.compare(max) > 0) {
                max = temp;
                maxInd = i;
            }
        }

        vertex.add(maxInd + 1, insertion);

    }

    private static final int DEF_SIZE = 3;

    public Facet() {
        this(DEF_SIZE);
    }

    public static void main(String[] args) {
        Facet f = new Facet();
        f.vertex.add(new Vector(0, 0, 0));
        f.vertex.add(new Vector(1, 0, 0));
        f.vertex.add(new Vector(1, 1, 0));

        f.sInsert(new Vector(2, 3, 0));
        f.sInsert(new Vector(0, 1, 0));
        f.sInsert(new Vector(1.1, .5, 0));

        System.out.println(f);

//        Angle a = new Angle(new Vector(1, 0, 0), new Vector(0, 1, 0));
//
//        for (double theta = 0; theta < 2 * Math.PI; theta += .1){
//            System.out.println("theta = " + (float)(theta/Math.PI) + " pi\tz = " + new Angle(new Vector(1, 0, 0), new Vector(Math.cos(theta), Math.sin(theta), 0)).compare(a));
//        }
    }

    public boolean onSamePlaneAs(Facet plane) {
        if (normal() != plane.normal())
            return false;
        return plane.coPlanar(vertex.get(0));
    }

    public Facet backSide() {//TODO:this should be moved to parent class

        final double EPSILON = .1;
        Vector shift = normal().normalized().times(-EPSILON);
        Facet backside = new Facet(vertex.size());
        for (Vector v : this.vertex)
            backside.vertex.add(v.plus(shift));
        return backside;
    }



    public boolean sharesEdgeWith(Facet f) {
        for (int i = 0; i < vertex.size(); i++)
            for (int j = 0; j < f.vertex.size(); j++)
                if (edge(i).nonDirectionalEquals(f.edge(j)))
                    return true;
        return false;
    }
    
        
    public static Facet regular(int numVerticies, double r){

        Transformation rot = new Transformation().rotZ(2*Math.PI/numVerticies);
        
        Facet reg = new Facet(numVerticies);
        
        Vector temp = new Vector(0, r, 0);
        
        for(int i = 0; i < numVerticies; i++){
            reg.vertex.add(temp.clone());
            rot.transform(temp);
        }
        
        return reg;
    }
}
