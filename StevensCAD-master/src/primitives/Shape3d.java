/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import java.io.*;

/**
 *
 * @author yifan
 */
public abstract class Shape3d extends Shape {  
//    public abstract Cube boundingSphere();
//    public abstract Sphere boundingCube();
//    public String getInfo();
//    public double volume();
//    public double surfaceArea();
//    public boolean isInside();
//    
//    public abstract void writeObject(ObjectOutputStream oos);
    public static Shape3d readObject3D(ObjectInputStream ois) {
        //TODO: if cube then return Cube.readObject(ois)
        // cube(center=true, t=[....], Cube.readObject(ois))
        return null;
    }
}
