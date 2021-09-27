package scad.hull;

import java.util.ArrayList;
import scad.Polyhedron;
import primitives.ConvexPoly;
import primitives.Pyramid;
import scad.Facet;
import scad.Side;
import scad.Vector;

/**
 * @author Dov
 * @author Fady
 *
 * Added conflict list algorithm, where this class will create the first
 * tetrahedron and its conflict list graph
 *
 */
public class ConvexHull extends ConvexPoly {

    ConflictGraphList conflictList, FacetToPointsList;
    ArrayList<Polyhedron> ConvexHull;  // the resultant convexhull
    public static boolean Initialized;
    private static ArrayList<Vector> cloudPoints;
    private int indexPointerToTetrahedron; // points to the last vertx of the created tetrahedron's arrayList

    public ConvexHull(ArrayList<Vector> points) {

        super(points.size() * 3);

        if (points.size() < 3) {
            System.out.println("Insuffcient points to start a tetrahedron");
            return;
        } else {
            System.out.println("Convex Hull Algorithm is selected...");
        }

        cloudPoints = points;
        /**
         * the vertex sort method below is uncommented to find the smallest
         * tetrahedron
         */
        // --> loadPoints = ConvexMethods.getSorted(loadPoints, Axis.x);  

        indexPointerToTetrahedron = expandTri(firstTirangle());    // will return the valid index for last valid point of the tetrahedron

        indexPointerToTetrahedron = firstTetraHedron(indexPointerToTetrahedron);  // will build the first tetrahedron and save it in Tetrahedron variable above
        
    //    conflictList = new ConflictGraphList(cloudPoints);
        
     //   conflictList = initilaizeConflictList(points, this);

//        conflictList.printConflictlist();
//        System.out.println("========================================================");
//        System.out.println("Total size = " + conflictList.getSize());

        //ConvexHull = new ArrayList<>(points.size()*2);  // reserve a memory size approximatly equivalent to the size of the total facets array
    }

    public ConvexHull(Polyhedron p) {
    }
    
     public ConflictGraphList initilaizeConflictList(ArrayList<scad.Vector> points, ArrayList<Facet> facets) {
         
          conflictList = new ConflictGraphList(cloudPoints);

        ArrayList<Facet> tempFacet ;
        ArrayList<ArrayList<scad.Vector>> tempVert;

        tempVert = new ArrayList<>();
        tempFacet = new ArrayList<>();

        for (int v = 4; v < points.size(); v++) {   // here we start from the fifth point , also used the convential for loop because the enahced loop causes unnecessary overhead
            for (int f = 0; f < facets.size(); f++) {
                
                if (definitely(points.get(v)) == Side.OUT && !(facets.get(f).below(points.get(v)))) {
                    System.out.println("found a link!");
                    System.out.println(points.get(v) + " ---->");
                    System.out.println(facets.get(f));
                    System.out.println("========================================");

                    tempFacet.add(facets.get(f));
                    tempVert.get(f).add(points.get(v));
                }
            }

            if (!tempFacet.isEmpty()) {
                
                
              conflictList.appendTopointToFacetList(points.get(v), tempFacet);
            }
        }

        if (!tempVert.isEmpty()) {
            for (int ff = 0; ff < tempVert.size(); ff++) {
                FacetToPointsList.appendTofacetToPointsList(facets.get(ff), tempVert.get(ff));
            }
        }
        return conflictList;
    }

    private int firstTirangle() {
        int i = 1;

        boolean collinear = true; // at the first atteration we assume the first three points are collinear
        while (collinear) {

            for (; i < cloudPoints.size() - 1; i++) { // take index 0 as referrence point then check for collinear vectors for the following indices 
                if (!(cloudPoints.get(0).isCollinear(cloudPoints.get(i), cloudPoints.get(i + 1)))) {
//                    //TODO remove from front is slow
//                    cloudPoints.remove(i);
//                    cloudPoints.size();

                    collinear = false; // we found first three non collinear points
                    break;
                }
            }
        }

      //  add(0, new Facet(cloudPoints.get(0), cloudPoints.get(i), cloudPoints.get(i+1))); // add first facet of the terahedron to index 0
        
        return i; // referes to the next index of the loaded cloud points after the tetrahedron points  
    }

    private int expandTri(int i) {

        while (get(0).contains(cloudPoints.get(i)) && i < cloudPoints.size()) {
            cloudPoints.remove(i);
        }

        while (get(0).coPlanar(cloudPoints.get(i))) {
            get(0).sInsert(cloudPoints.get(i++));
        }

        return i;
    }

    /**
     * create the smallest plane of three points p1,p2,p3
     *
     */
    private int firstTetraHedron(int i) {
    //    addAll(new Pyramid(remove(0), cloudPoints.get(i)));

        return i + 1;  // return next index 
    }

    public static ArrayList<Vector> getLoadPoints() {
        return cloudPoints;
    }
    
    public int getSize (){
        return size();
    }
   

}

//    /**
//     * will pick the first point in conflictfacet then find the facing facets for that point from the conflictlist
//     * @return 
//     */
//   private ArrayList<Facet> getFacingFacets(){
//       ArrayList <Facet> temp = new ArrayList<>();
//       
//       
//       
//       return
//       
//       
//   }

