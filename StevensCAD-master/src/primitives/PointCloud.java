/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import processing.core.PShape;
import scad.Vector;
import viewer.CADEditor;

/**
 *
 * @author Fady
 *
 * It creates PointCloud shape using one of two methods one by creating 3D
 * points from a selected range other is by importing an external .dat file
 * contains the points cloud coordinates
 *
 * TODO - make is extends SHAPES class - add x,y,z center point of the created
 * point cloud shape (translate)
 *
 */
public class PointCloud extends AnalyticalShape3d {

    private double point;
    private double precision = 1;

    Random rand;
    public ArrayList<Vector> PointCloud;

    /**
     * used incase of exiting points external file
     *
     * @param filename
     * @param precision
     */
    public PointCloud(String filename, int precision) {
        PointCloud = vertices;

        checkForPrecisionRange();

        try {
            Scanner scan = new Scanner(new File(filename));
            String line = null;

            while (scan.hasNext()) {
                if (!scan.equals("")) {
                    // Vector tempVect = new Vector(0,0,0);

                    double x = roundOff(scan.nextDouble(), precision);   // the round off method is used in case the imported points are double numbers with percision more than 3 digits after dot (becaus CADViewer wasnt able to take high percision numbers!)
                    double y = roundOff(scan.nextDouble(), precision);
                    double z = roundOff(scan.nextDouble(), precision);
                    PointCloud.add(new Vector(x, y, z));
                    //System.out.println(PointCloud.get(PointCloud.size()-1));
                }
            }
        } catch (FileNotFoundException e1) {
        }
    }

    private double roundOff(double d, double precision) { // here can control the percision of the double numbers by changing the division number for d

        return ((Math.round(d * precision)) / precision);
    }

    private void checkForPrecisionRange() {
        try {
            if (precision < 10 && precision > 0 && precision != 0) {
                precision = (int) Math.pow(10, precision);
            } else {
                precision = 1; // this value won't affect the actual number 
            }
        } catch (Exception e0) {
            System.out.println("The precision value is out of range");
        }
    }

    /**
     * to generate a random double number pointcloud [minRange, maxRange]
     *
     * @param numberOfpoints
     * @param minRange
     * @param maxRange
     * @param precision
     */
    public PointCloud(int numberOfpoints, int minRange, int maxRange, int precision) {

        checkForPrecisionRange();

        PointCloud = vertices;
        rand = new Random();

        for (int i = 1; i <= numberOfpoints; i++) {
            double x = roundOff(minRange + (maxRange - minRange) * rand.nextDouble(), precision);
            double y = roundOff(minRange + (maxRange - minRange) * rand.nextDouble(), precision);
            double z = roundOff(minRange + (maxRange - minRange) * rand.nextDouble(), precision);
            PointCloud.add(new scad.Vector(x, y, z));
            //  System.out.println(PointCloud.get(i-1));

        }
    }

    /**
     * to generate a random double number pointcloud
     *
     * @param numberOfpoints
     * @param minRange
     * @param maxRange
     * @param radius
     * @param precision
     */
    public PointCloud(int numberOfpoints, int minRange, int maxRange, int radius, int precision) {
        double u1, u2, r, theta, phi;
        PointCloud = getVertices();
        rand = new Random();

        //==========wierd shape !       
//        for (int i=1; i <= numberOfpoints; i++){
//             theta = 360* rand.nextDouble();
//            phi = 360* rand.nextDouble();
//            
//           PointCloud.add(new scad.Vector(radius*Math.sin(theta)*Math.cos(phi),radius*Math.sin(theta)*Math.sin(phi), radius*Math.cos(phi)));
//==========================            
        for (int i = 1; i <= numberOfpoints; i++) {
            u1 = (maxRange - minRange) * rand.nextDouble();
            u2 = maxRange * rand.nextDouble();
            r = Math.sqrt(maxRange - (u1 * u2));
            theta = 2 * Math.PI * u2;
            PointCloud.add(new scad.Vector(r * Math.sin(theta), r * Math.cos(theta), u1));
            //System.out.println(PointCloud.get(i-1));
//            x = r*Math.cos(theta);
//            y = r*Math.sin(theta);
//            z = u1;

            // ----> supernova shape !   PointCloud.add(new scad.Vector(r*Math.cos(theta), r*Math.sin(theta), u1*Math.tan(theta)));
            // ---->  UFO shape!   PointCloud.add(new scad.Vector(r*Math.cos(theta), r*Math.sin(theta), Math.sin(theta)));
//       // ---->  taco shape!  PointCloud.add(new scad.Vector(r*Math.cos(theta)*Math.sin(theta), r*Math.sin(theta)*Math.sin(theta), r*Math.cos(theta)));
        }
    }

    
    
//    public PShape convertToDisplayable(CADEditor edit, viewer.Color stroke) {
//        return edit.generatePoint(this, stroke);
//    }

}