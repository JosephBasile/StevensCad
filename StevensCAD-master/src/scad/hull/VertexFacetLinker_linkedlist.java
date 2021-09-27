///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package scad.hull;
//
//import java.util.ArrayList;
//import scad.Facet;
//
//
///**
// * 
// * @author Fady
// * 
// *To initiate a link between a vertex and facing facet
// *also it contains some necessary methods for linked list operations 
// * 
// *  head <--- VerVertexFacetLinker (v,f,next,prev) ---> tail
// *  prev --> head
// *  next --> tail
// * 
// */
//public class VertexFacetLinker{
//    
//     /**
//     * link to next and previous nodes of type ConflictGraphList
//     */
//    protected static scad.Vector vector;
//    protected static Facet facet;
//    protected static ArrayList<Facet> facetArray;
//    protected static ArrayList<scad.Vector> vectorArray;
//    protected VertexFacetLinker next, prev;
//   
//   /**
//    * initialize the default constructor to null values
//    */
//    public VertexFacetLinker (){
//        prev = null;
//        next = null;
//    }
//    
//    public VertexFacetLinker (scad.Vector v, Facet f ){ // this is for points conflict G
//        this();
//        vector =v;
//        facet =f;
//    }
//    
//    
//    public VertexFacetLinker (Facet f, scad.Vector v ){ // this is for points conflict G
//        this();
//        vector =v;
//        facet =f;
//    }
//    
//    
//     public VertexFacetLinker (scad.Vector v, ArrayList<Facet> f ){ // this is for points conflict G
//        this();
//        vector =v;
//        facetArray =f;
//    }
//     
//     public VertexFacetLinker (Facet f, ArrayList<scad.Vector> v ){  // this is for facet conflict G
//        this();
//        facet =f;
//        vectorArray =v;
//    }
//     
//     public VertexFacetLinker (scad.Vector v, Facet f,VertexFacetLinker next, VertexFacetLinker prev ){
//        vector =v;
//        facet =f;
//        this.next=next;
//        this.prev=prev;
//    }
//     
//     
//    
//    public VertexFacetLinker (scad.Vector v,  ArrayList<Facet> f,VertexFacetLinker next, VertexFacetLinker prev ){
//        vector =v;
//        facetArray =f;
//        this.next=next;
//        this.prev=prev;
//    }
//    
//    
//    /**
//         * Below are some necessary method (getters) to transfer data to other classes 
//         * @return 
//         */
//     
//   public scad.Vector getVector(){
//        return vector;
//    }
//   
//    public  Facet getFacet(){
//        return facet;
//    }
//    
//       
//    public  ArrayList<Facet> getFacetArray(){
//        return facetArray;
//    }
//    
//    public  ArrayList<scad.Vector> getVectorArray(){
//        return vectorArray;
//    }
//   
//     
//     /**
//      * Add an element after a selected link from the linked list 
//     * @param link
//     * @param v
//     * @param f
//      */
//     
////     public void putAfterLink (VertexFacetLinker link, scad.Vector v, Facet f){
////            VertexFacetLinker temp  = new VertexFacetLinker(v, f,next,prev);
////            temp.next = link.next;
////      //      newTemp.nextFacet = link.nextFacet;
////            link.prev = temp;
////            temp.prev = link;
////             
////            if (temp.next != null){
////                temp.next.prev = link;
////            }
////        size--;
////    }
//     
//     /**
//      * Remove the first element from the linked list 
//      */
//     
////      public void removeStart (){
////          if (Head== null){
////              return;
////          }
////        VertexFacetLinker temp  = Head;
////        Head=Head.next;
////        Head.prev=null;
////        size--;
////        temp=null;
////      }
//      /**
//      * Remove the last element from the linked list 
//      */
//      
////       public void removeEnd  (){
////           if (Tail==null){
////               return;
////           }
////        VertexFacetLinker temp  = Tail;
////        Tail=temp.prev;
////        Tail.prev=null;
////        size--;
////        temp=null;
////    }
//       
//       /**
//      * Remove an element after a selected link from the linked list 
//     * @param link
//     * @param v
//     * @param f
//      */
//    
////        public void removeAfter (VertexFacetLinker link, scad.Vector v, Facet f){
////            VertexFacetLinker temp  = new VertexFacetLinker(v, f,prev,next);
////            temp.next = link.next;
////            link.next=temp;           
////            temp.prev= link;
////            if (temp.next != null){
////                temp.next.prev = temp;
////            }
////            
////        size++;
////    }
//        
//        
//    
//}
