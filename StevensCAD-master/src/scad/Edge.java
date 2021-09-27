package scad;
/**
 *
 * @author dov
 */
public class Edge {

    public Vector a, b;

    public Edge() {
    }

    public Edge set(Vector a, Vector b) {
        this.a = a;
        this.b = b;
        return this;
    }

    public Edge(Vector a, Vector b) {
        this.a = a;
        this.b = b;
    }

    public Vector at(double t) {
        return a.mult(t).plus(b.mult(1 - t));
    }


    @Override
    public String toString() {
        return "" + a + '\t' + b;
    }

    public boolean equals(Edge edge) {
        return a.equals(edge.a) && b.equals(edge.b);
    }

    public boolean nonDirectionalEquals(Edge edge) {
        return (a.equals(edge.a) && b.equals(edge.b)) || (a.equals(edge.b) && b.equals(edge.a));
    }

    public boolean contains(Vector in) {
        if (!in.isCollinear(a, b)) return false;
        return containsCL(in);
    }

    /**
     * checks to see if a colinear point is on the edge.
     *
     * @param in
     * @return
     */
    public boolean containsCL(Vector in) {
        double segLength = a.distance(b);
        return !(in.distance(a) > segLength || in.distance(b) > segLength);
    }

    public double length() {
        return a.distance(b);
    }
    
    public Vector nearest(Vector v){
        double t = (b.minus(a)).dot(b.minus(v))/(b.minus(a)).magnitudeSq();
        if(t <= 0) return a;
        if(t >= 1) return b;
        return at(t); 
    }
    public void setTo(Edge e){
        a.setTo(e.a);
        b.setTo(e.b);
    }
    
}