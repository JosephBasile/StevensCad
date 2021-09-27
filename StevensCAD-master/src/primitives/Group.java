/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;
import java.util.*;
import scad.*;
/**
 *
 * @author dkruger
 */
public class Group extends Shape3d {
    private ArrayList<Shape3d> components;
    
    public Group() {
        components = new ArrayList<>();
    }
    public void add(Shape3d s) {
        components.add(s);
    }
    //TODO: Figure out what to do with size
    /*
      We should not be asking a shape what its size is.  This makes sense for a single shape, not for  a compound one.
      We should use an iterator instead and keep going while hashNext()
    */
    public int size() {
        return 0;
    }
    public ArrayList<Triangle> triangles() {
        throw new RuntimeException("Unimplemented");
    }
    
    /**
     * Apply the transform to all members of this group
     */
    public void applyTransform() {
      for (Shape3d s : components) {
          s.applyTransform();
      }   
    }
//    public BoundingSphere getBoundingSphere() {
//        return boundingSphere;
//    }
}
