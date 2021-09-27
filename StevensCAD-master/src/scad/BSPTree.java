package scad;

public class BSPTree {

    private BSPTree inside, outside;
    private Facet cleaver;

    public BSPTree(Polyhedron poly) {

        this.cleaver = poly.facets.remove(poly.size() - 1);

        Split<Polyhedron> split = cleaver.cleave(poly);

        if (split.hasInside())
            this.inside = new BSPTree(split.inside());
        if (split.hasOutside())
            this.outside = new BSPTree(split.outside());

        poly.add(cleaver);
    }

    public BSPTree() {
    }

    public void add(Facet facet) {
        if (cleaver == null)
            cleaver = facet;
        else {
            Split<Facet> split = cleaver.cleave(facet);
            if (split.hasInside()) {
                if (inside == null)
                    inside = new BSPTree();
                inside.add(split.inside());
            }
            if (split.hasOutside()) {
                if (outside == null)
                    outside = new BSPTree();
                outside.add(split.outside());
            }
        }
    }

    public void polyCleave(Polyhedron meat, Polyhedron insidePoly, Polyhedron outsidePoly) {
        
        Split<Polyhedron> cleaved = cleaver.cleave(meat);

        if (cleaved.hasInside())
            if (inside == null)
                insidePoly.addAll(cleaved.inside().facets);
            else
                inside.polyCleave(cleaved.inside(), insidePoly, outsidePoly);

        if (cleaved.hasOutside())
            if (outside == null)
                outsidePoly.addAll(cleaved.outside().facets);
            else
                outside.polyCleave(cleaved.outside(), insidePoly, outsidePoly);
    }


    public boolean contains(Vector point) {
        return side(point) == Side.IN;
    }

    public Side side(Vector point) {

        Side side = cleaver.side(point);
        if (side == Side.IN) {
            if (inside == null || inside.isEmpty())
                return Side.IN;
            return inside.side(point);
        } else if (side == Side.OUT) {
            if (outside == null || outside.isEmpty())
                return Side.OUT;
            return outside.side(point);
        } else
            return Side.SURFACE;
    }

    public boolean isEmpty() {
        return cleaver == null;
    }
}
