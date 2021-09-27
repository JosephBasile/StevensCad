/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scad.hull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import primitives.ConvexPoly;
import scad.Edge;
import scad.Facet;
import java.util.NoSuchElementException;
import scad.Polyhedron;
import scad.Side;
import scad.Vector;

/**
 *
 * @author Fady
 *
 * Contains unprocessed points to implement point insert for the convex hull
 * arrayList This method will randomizes the incremental method principle to
 * achieve O(n log n) It's generated for facet of the convex hull to keep track
 * of the conflicts
 *
 * It generates two lists One for each facet and the visible vertices to it
 * Other one for each vertex which it'll contain all the visible facets to it
 * the we check for conflicts where facet and vertex cannot coexist on the same
 * convex hull Doubly linked lists technique is used to build these lists
 *
 *
 */
public class ConflictGraphList {

    private Facet facet;
    private ArrayList<Edge> HorizonLine;
    private ArrayList<Facet> VisibleFacetArray;// = new ArrayList<>();
    private LinkedHashMap<scad.Vector, ArrayList<Facet>> pointToFacetList;  // for point --> facets array graph
    private LinkedHashMap<Facet, ArrayList<scad.Vector>> facetToPointsList; // for facet --> points array graph
    private ArrayList<Facet> facetsList;
    private ArrayList<scad.Vector> verticesList;
    private scad.Vector vertex;
    

   

    public ConflictGraphList(ArrayList<scad.Vector> points) {
        int arraySize = points.size() * 2;
        vertex = null;
        facet = null;
        facetsList = new ArrayList<>(arraySize);
        verticesList = new ArrayList<>(arraySize);
        pointToFacetList = new LinkedHashMap<>(arraySize);
        facetToPointsList = new LinkedHashMap<>(arraySize);
    }

    /**
     * initialize the conflict list for the first run after creating the first
     * tetrahedron
     *
     * @param points
     * @param facets
     */
    
    
     
   
    public void appendTopointToFacetList(scad.Vector v, ArrayList<Facet> f) {

        pointToFacetList.put(v, f);
    }

    public void appendTofacetToPointsList(Facet f, ArrayList<scad.Vector> v) {

        facetToPointsList.put(f, v);
    }

    public void printConflictlist() {
        if (pointToFacetList.isEmpty()) {
            System.out.println("Conflict List is empty, no items to print");
        } else {
            for (scad.Vector v : pointToFacetList.keySet()) {
                System.out.println(v + " --->");
                for (Facet f : pointToFacetList.get(v)) {
                    System.out.println(f);
                }
            }
        }
    }

    public void printReverseConflictlist() {
        if (facetToPointsList.isEmpty()) {
            System.out.println("Reverse Conflict List is empty, no items to print");
        } else {
            for (Facet f : facetToPointsList.keySet()) {
                System.out.println(f + " --->");
                for (scad.Vector v : facetToPointsList.get(f)) {
                    System.out.println(v);
                }
            }
        }
    }

    /**
     * remove a link by index
     *
     * @param index
     */
    public void removeFromPointToFacetList(int index) {
        if (pointToFacetList.isEmpty()) {
            throw new NoSuchElementException();
        }

        pointToFacetList.remove(index);
        System.out.println("Link #" + index + " is removed");
    }

    /**
     * remove a link by key value
     *
     * @param vertex
     */
    public void removeFromPointToFacetList(scad.Vector vertex) {
        if (pointToFacetList.isEmpty()) {
            throw new NoSuchElementException();
        }

        pointToFacetList.remove(vertex);
        System.out.println("Link of key " + vertex + " is removed");
    }

    /**
     * print the visible vertices from the selected facet
     *
     * @param facet
     */
    public void printVisiblePoints(Facet facet) {
        if (facetToPointsList.isEmpty()) {
            throw new NoSuchElementException();
        }
        System.out.println("visible points of the selected facet are:");
        for (scad.Vector v : facetToPointsList.get(facet)) {
            System.out.println(v);
        }
    }

    /**
     * print the visible facets from the selected vertex
     *
     * @param vector
     */
    public void printVisibleFacets(scad.Vector vector) {
        if (pointToFacetList.isEmpty()) {
            throw new NoSuchElementException();
        }
        System.out.println("visible points of the selected facet are:");
        for (Facet f : pointToFacetList.get(vector)) {
            System.out.println(f);
        }
    }

    
    public int getSize() {
        return pointToFacetList.size();
    }

    public LinkedHashMap<scad.Vector, ArrayList<Facet>> getpointToFacetList() {
        return pointToFacetList;
    }

    public LinkedHashMap<Facet, ArrayList<scad.Vector>> getfacetToPointsList() {
        return facetToPointsList;
    }

}
