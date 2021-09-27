/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil.test;

import gutil.BeanEditor;
import java.io.FileReader;
import java.util.Properties;
import primitives.Cube;
import primitives.Cylinder;
import viewer.CADEditor;

/**
 *
 * @author yangbai
 */
public class TestBeanEditor {
    public static void main(String[] args) {
        //Sphere s = new Sphere();
        //Cube c = new Cube();
//        Cylinder c = new Cylinder();
//        new BeanEditor(c);
        try {
            Properties p = new Properties();
            FileReader fr = new FileReader("conf/en"); 
            p.load(fr);
            
            System.out.println(p);
            
        }
        catch (Exception e){
            // pass
            System.out.println(e.getMessage());
        }
        
        
    }
}
