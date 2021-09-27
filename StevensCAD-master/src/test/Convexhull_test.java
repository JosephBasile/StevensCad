/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import viewer.CADEditor;
import primitives.PointCloud;
import scad.Vector;

/**
 * Contains the required steps to run a test on the convex hull algorithm data
 * folders contains three test files of 3D random generated points to construct
 * 3d shape
 *
 * @author Fady
 */
public class Convexhull_test {
    public ArrayList<Vector> vertexCloud;
    public PointCloud points;

    public Convexhull_test() {
        points = new PointCloud(1000, -40, 40, 2);   // -1 to leave the numbers without roundoff
        vertexCloud = points.PointCloud;
        
        
    }
    

    public static void main(String[] args) throws Exception {
        
        
        
        Convexhull_test convTest = new Convexhull_test();
         CADEditor v = CADEditor.openWindow(0, 0, 1024, 768);
        v.add(convTest.points);

        /**
         * import the points set file located in data folder inside the main
         * project it contains three .dat files has different number of points
         * set , 100, 1200 , 5000 points convex100.dat convex1200.dat
         * convex5000.dat
         */
        /**
         * format: importPointsFile (filename , precision) precision is used to
         * trim high precision double numbers if needed or in case of uneven
         * precision double numbers precision range is between 0 to 10 digits
         * where 0 precision will import the numbers as it is
         */
//         /**
//          * Filter , apply Sieve Algorithm to remove collinear points for each axis
//          */
//         ArrayList<Vector> EnhancedArrayList = operations.SieveAlgoritm(PointCloud);
//         System.out.println("Filter 2, Sieve Algorithm applied successfully");
//         System.out.println("Total Seive array points "+ EnhancedArrayList.size());
//         
//         for (scad.Vector v: PointCloud){
//           
//            System.out.println(v.x + " "+ v.y + " "+ v.z);
//            System.out.println("total Seive array points "+ PointCloud.size());
//        }
        /**
         * send the loaded points cloud the viewer second variable is for the
         * stroke weight /** pointCloud format either (filename, precision) or
         * generate random points (number of points , min value, max value,
         * precision of the generated numbers )
         */
        
        //  PointCloud points = new PointCloud(5000, 1, 20, 25,2);
        //PointCloud points = new PointCloud("data/convex1200.dat", 0);   // <-- to import an external file points cloud
        
//        PointCloud points = new PointCloud(1000, -40, 40, 2);   // -1 to leave the numbers without roundoff
//        vertexCloud = points.PointCloud;

      //  ConvexHull convex = new ConvexHull(VertexCloud);
      //CADEditor v = CADEditor.openWindow(0, 0, 1024, 768);
      


      
      
      
      
        //convex.getAlgoStatus(); // produces output message about the algortihm stataus like Successful, out of range, not matching ,..etc
    }

}
