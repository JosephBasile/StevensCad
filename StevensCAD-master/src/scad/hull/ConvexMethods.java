/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scad.hull;

/**
 *
 * @author Fady
 *
 *  this folder contains the operation applied on the imported arraylist of the selected polygon 
 * these operations used by the class file ConvexHull.java
 * 
 * notes
 * this part will be implemented in case the Seive algorithm won't be successful 
 * this function below is to remove the unnecessary points from inside the polyhedron before finding convex hull
 * to improve code's performance
 * by creating an virtual octahedron or tetrahedron inside the polyhedron using the extreme points
 * then remove all the inside points 
 * now we have six points for the most outer vertices of the selected polyhedron 
 * we can choose any three of them to start with the first facet of three vertices then build first tetrahedron
 * orientation points
 * first find simplex of k+1 vertices (3d+1=4 vertices) tetrahedron
 * then 
 * vector cross product is to find the direction of the third vector perpendicular
 * create a points outside each facet and select the furthest point 
 * other theory is to to use Jarvis theory for 2D polyhedron but instead of using line rotation around a vertex  
 * we can use a plane instead rotating around an edge 
 * to create the facets
 * 
*/


import java.util.ArrayList;
import java.util.Collections;
import scad.Edge;
import scad.Facet;
import scad.Vector;


public class ConvexMethods {
     public static enum Axis {x ,y ,z };
     private ArrayList <Vector> PolyhedronPointsSet, LoadPoints, EnhancedLoadPoints;
     private static ArrayList <Vector> SortedForX,SortedForY, SortedForZ;
     private static Vector LeastPointX, ExtremePointX,
                LeastPointY, ExtremePointY,
                LeastPointZ, ExtremePointZ; // will contain the extreme points of the selected shape
    
    public ConvexMethods(){
    LoadPoints = new ArrayList<>();
    EnhancedLoadPoints = new ArrayList<>();
    }
    
   /**
    * Sieve algorithm
    * the idea is to sort the arraylist for x then after remove the collinear points we sort the resultant 
    * array for y and so on
    *
    * @param arraylist
    * @return EnhancedLoadPoints
    */
     public ArrayList<Vector> SieveAlgoritm (ArrayList<Vector> arraylist){ // to remove the collinear points from the arraylist of the selected shape 
          
            Axis x=Axis.x;
            Axis y=Axis.y;
            Axis z=Axis.z;
             Vector LastItemCollinear; // to store the end element of the collinear points
             Vector FirstVertex = arraylist.get(0);
             EnhancedLoadPoints.add(FirstVertex);
             
             LastItemCollinear = Vector.ZERO; // we start the comparison with vector of zero points
             sortArrayList(arraylist, x);
             Double checkX=FirstVertex.x;
             for (int i=1;i<arraylist.size();i++){
                 if (checkX == arraylist.get(i).x){
                     LastItemCollinear = arraylist.get(i);
                 }else{
                     checkX = arraylist.get(i).x;
                     EnhancedLoadPoints.add(arraylist.get(i));
                     if (LastItemCollinear != Vector.ZERO){
                         EnhancedLoadPoints.add(LastItemCollinear);
                         LastItemCollinear = Vector.ZERO;
                     }
                 }
             }
             
            LastItemCollinear = Vector.ZERO;
            sortArrayList(EnhancedLoadPoints, y);
            Double checkY=EnhancedLoadPoints.get(0).y;
            for (int i=1;i<arraylist.size();i++){
                 if (checkY == arraylist.get(i).y ){
                     LastItemCollinear = arraylist.get(i);
                 }else{
                     checkY = arraylist.get(i).y;
                     EnhancedLoadPoints.add(arraylist.get(i));
                     if (LastItemCollinear != Vector.ZERO){
                         EnhancedLoadPoints.add(LastItemCollinear);
                         LastItemCollinear = Vector.ZERO;
                     }
                 }
             }
            
            
            LastItemCollinear = Vector.ZERO;
            sortArrayList(EnhancedLoadPoints, z);
            Double checkZ=arraylist.get(0).z;
            for (int i=1;i<arraylist.size();i++){
                 if (checkZ == arraylist.get(i).z ){
                     LastItemCollinear = arraylist.get(i);
                 }else{
                     checkZ = arraylist.get(i).y;
                     EnhancedLoadPoints.add(arraylist.get(i));
                     if (LastItemCollinear != Vector.ZERO){
                         EnhancedLoadPoints.add(LastItemCollinear);
                         LastItemCollinear = Vector.ZERO;
                     }
                 }
            }
            
            return EnhancedLoadPoints;
        }
  /**
   * sort the points array to find the extreme points
   * @param PointsArray
   * @param axis , to define the desired axis
   */
  private static ArrayList<scad.Vector> sortArrayList (ArrayList<scad.Vector> PointsArray, Axis axis){
      
          int LastElement = PointsArray.size()-1;
          ArrayList<scad.Vector> SortedArray = new ArrayList<>();
             
             switch (axis){  // sort array by request 
                 
                 case x:
                    Collections.sort(PointsArray, (Vector v1, Vector v2) -> Double.valueOf(v1.x).compareTo(v2.x)); // sort the given ArrayList for x
                    SortedForX = PointsArray ;
                    LeastPointX = PointsArray.get(0);
                    ExtremePointX = PointsArray.get(LastElement);
                    SortedArray = SortedForX;
                    
                    break;
                    
                 case y:
                     Collections.sort(PointsArray, (Vector v1, Vector v2) -> Double.valueOf(v1.y).compareTo(v2.y)); // sort the given ArrayList for y
                     SortedForY=PointsArray;
                     LeastPointY = PointsArray.get(0);
                     ExtremePointY = PointsArray.get(LastElement);
                     SortedArray = SortedForY;
                     
                     break;
                     
                 case z:
                     Collections.sort(PointsArray, (Vector v1, Vector v2) -> Double.valueOf(v1.z).compareTo(v2.z)); // sort the given ArrayList for z
                     SortedForZ = PointsArray;
                     LeastPointZ = PointsArray.get(0);
                     ExtremePointZ = PointsArray.get(LastElement);
                     SortedArray = SortedForZ;
                     
                     break;
             default:
                 throw new AssertionError(axis.name());
                 
        
             }
          return SortedArray;
             
    }
  
  
   public static ArrayList<scad.Vector> getSorted (ArrayList<scad.Vector> points, Axis axis){
      return sortArrayList(points,axis);
  }
   
   
//   public static ArrayList<Vector> getSortedForY (){
//      return SortedForY;
//  }
//   
//   public static Vector getExtremePointX (){
//      return ExtremePointX; 
//  }
//   
//   public static Vector getExtremePointY (){
//      return ExtremePointY; 
//  }
//   
//   public static Vector getExtremePointZ (){
//      return ExtremePointZ; 
//  }
//   
//   public static Vector getLeastPointX (){
//      return LeastPointX; 
//  }
  
    
  public  ArrayList<Vector> CheckforEqualPoints(ArrayList<Vector> arraylist){
      int size = arraylist.size();
                 for (int i=0; i<size;i++){
                     for (int j=i+1; j<size; j++)
                     if (arraylist.get(i).equals(arraylist.get(j))){
                             arraylist.remove(j);
                             size = arraylist.size();
                     }
                 }
                 return arraylist;
             }
}
  


//  drafties!

//  private boolean isItInside(Vector V, Facet F){   // nothing is impelented here 
//      
//      
//      
//      Edge E1=new Edge(F.get(v0),F.get(v1));
//      Edge E2=new Edge(F.get(v0),F.get(v2));
//      
//       double Distance1 = E1.length();
//       double Distance2 = E2.length();
//         
//      Double  Distance  = Distance 2- Distance 1;
//      
//      Edge newEdge = new Edge (F.get(v0), Distance);
//      
//      return (V.x * newEdge.a + V.y * newEdge.b+ 
//                V.z * newEdge.c);
//      
//     
//  }
  
  
//  private ArrayList<Vector> RemoveInside(ArrayList <Vector> V , ArrayList <Facet> F){ // we check if the selected point is behind all the facets of the tetrahedron
//      ArrayList<Vector> TempArrayList = V;
//      int VSize = V.size();
//      int NumberOfFaces = F.size();
//      int j=0, faceCount=0;
//      
//      for (int i=0 ; i<VSize; i++){
//          faceCount = 0;
//          while (isItInside(V.get(i), F.get(j))){      
//                  faceCount++;
//                  j++;
//              }
//          if (faceCount == NumberOfFaces){
//                  TempArrayList.remove(i);
//          }
//          j=0;faceCount=0;
//          
//      }
//      
//      return TempArrayList;
//              
//  }
  