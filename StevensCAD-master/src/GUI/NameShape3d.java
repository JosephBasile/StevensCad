/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import primitives.Shape3d;
import scad.Polyhedron;

/**
 *
 * Name a polyhedron, user can name their polyhedron, and then push named
 * polyhedron to GetNamedPolyhedrons class
 */
public class NameShape3d {
    private String name;
    public Shape3d p;
    
    public NameShape3d(Shape3d p, String name) {
        this.p = p;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public Shape3d getShape3d() {
        return p;
    }
}
