package scad.hull;

import java.util.ArrayList;
import java.util.List;
import scad.Edge;
import scad.Facet;
import scad.Vector;

/**
 *
 * @author dov
 */
public class ClosedPath extends ArrayList<Edge>{

    /**
     * creates a closed path around the outer edges of the facets.
     * The path has no useful order.  Also, very slow.
     * @param facets
     */
    public ClosedPath(List<Facet> facets) {
        
        for(int i = 0; i < facets.get(0).size(); i++)
            add(facets.get(0).edge(i));
        
        for(int facet = 1; facet < facets.size(); facet++)
            addFacet(facets.get(facet));
        
    }
    
    private void addFacet(Facet facet){
        for(int i = 0; i < facet.size(); i++)
            addEdge(facet.edge(i));
    }
    private void addEdge(Edge possibleEdge){
        for(int i = 0; i < size(); i++)
            if(possibleEdge.nonDirectionalEquals(get(i))){
                remove(i); return;
            }
        add(possibleEdge);
    }
    
    public boolean isClosed(){
        Edge start = get(0);
        int count = 0;
        for(Edge travel = nextEdge(start); !travel.equals(start);travel = nextEdge(travel)){
            count++;
            if(count > size()) return false;
        }
        return true;
    }
    public Edge nextEdge(Edge currentEdge){
        boolean fork = false;
        Edge nextEdge = null;
        for(Edge checkEdge: this){
            if(checkEdge.a == currentEdge.b){
                if(fork == true) throw new ArithmeticException("closed path has a fork");
                fork = true;
                nextEdge = checkEdge;
            }
        }
        return nextEdge;
    }
}
