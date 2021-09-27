/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import primitives.*;
import scad.*;
import scad.testOperations.HighDegreeTreeOperations;

/**
 *
 * @author dkruger
 */
public class TestUnion {
    public static void main(String[] args) {
        Shape s1 = new Sphere(100,100,100);
        Shape s2 = new Sphere(100,100,100, new Vector(100,0,0));
        Operations op = new HighDegreeTreeOperations();
        Shape s3 = op.union(s1, s2);
        System.out.println(s3);
    }
}
