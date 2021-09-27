/*
 */
package scad;

import static java.lang.Math.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Dov Kruger
 */
public final class Vector implements Serializable {
    /**
     * x,y,z are double and visible
     */
    public double x,y,z;
    public final static int dim = 3;

    public Vector(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }
    
    //TODO add int constructor
    public Vector(int x,int y, int z){
        this((double)x,(double)y,(double)z);
    }
    
    public Vector(double[] x){
        this.x = x[0];
        y = x[1];
        z = x[2];
    }
    
    /**
     * Vector {@code (1, 0, 0)}.
     */
    public final static Vector UNIT_X = new Vector(1, 0, 0);

    /**
     * Vector {@code (0, 1, 0)}.
     */
    public final static Vector UNIT_Y = new Vector(0, 1, 0);

    /**
     * Vector {@code (0, 0, 1)}.
     */
    public final static Vector UNIT_Z = new Vector(0, 0, 1);

    /**
     * Vector {@code (0, 0, 0)}.
     */
    public final static Vector ZERO = new Vector(0, 0, 0);
   



    
    /*
    * Return the double in the vector
    */
    public double get(int i){
        switch (i){
            case 1:
                return x;
            case 2:
                return y;
            case 3:
                return z;
            default:
                return 0;
        }
    }
    
    /*
    * set method
    */
    public void set(int i, double j){
        switch(i){
            case 1:
                x = j;
                break;
            case 2:
                y = j;
                break;
            case 3:
                z = j;
                break;
        }
    }
    
    /*
    * return (0,0)
    */
    public static Vector Origon(int dim){
        double[] x = new double[dim];
        for(int i=0;i<dim;i++){
            x[i] = 0;
        }
        return new Vector(x);
    }
    
 
    /**
     * Returns the distance between the specified point and this point.
     *
     * @param p point
     * @return the distance between the specified point and this point
     */
    public double distance(Vector p) {
        return minus(p).magnitude();
    }

    /**
     * Returns a negated copy of this vector.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return a negated copy of this vector
     */
    public Vector neg() {
        return new Vector(-x, -y, -z);
    }

    public Vector times(Vector b) {
        return new Vector(x*b.x, y*b.y, z*b.z);
    }
    public Vector times(double b) {
        return new Vector(x*b, y*b, z*b);
    }

    public Vector negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Returns the sum of this vector and the specified vector.
     *
     * @param v the vector to add
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the sum of this vector and the specified vector
     */
    public Vector plus(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

     /**
     * Set this vector to the sum of a and b
     *
     * @param a left vector
     * @param b right vector
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the sum of this vector and the specified vector
     */
    public void plus(Vector a, Vector b) {
        x = a.x + b.x; y= a.y + b.y; z = a.z + b.z;
    }

    /**
     * Returns the difference of this vector and the specified vector.
     *
     * @param v the vector to subtract
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the difference of this vector and the specified vector
     */
    public Vector minus(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

     /**
     * Set this vector to the difference of a and b
     *
     * @param a left vector
     * @param b right vector
     *
     * <b>Note:</b> modifies this vector
     */
    public void minus(Vector a, Vector b) {
        x = a.x - b.x; y= a.y - b.y; z = a.z - b.z;
    }

    /**
     * Returns the product of this vector and the specified value.
     *
     * @param a the value
     *
     * <b>Note:</b> modifies this vector
     *
     * @return the product of this vector and the specified value
     */
    public Vector mult(double a) {
        return new Vector(x * a, y * a, z * a);
    }

    /**
     * Returns the dot product of this vector and the specified vector.
     *
     * @param a the vector
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the product of this vector and the specified vector
     */
    public double dot(Vector a) {
        return x * a.x + y * a.y + z * a.z;
    }
    
    public double abs(){
        return distance(Vector.ZERO);
    }

    /**
     * Returns the sum of this vector and the specified vector.
     *
     * @param v the vector to add
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the sum of this vector and the specified vector
     */
    public Vector added(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Returns the difference of this vector and the specified vector.
     *
     * @param v the vector to subtract
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the difference of this vector and the specified vector
     */
    public Vector subtracted(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Returns the product of this vector and the specified value.
     *
     * @param a the value
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the product of this vector and the specified value
     */
    public Vector multiplied(double a) {
        return new Vector(x * a, y * a, z * a);
    }

    /**
     * Returns the product of this vector and the specified vector.
     *
     * @param a the vector
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the product of this vector and the specified vector
     */
    public Vector multiplied(Vector a) {
        return new Vector(x * a.x, y * a.y, z * a.z);
    }


    /**
     * Returns this vector divided by the specified value.
     *
     * @param a the value
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return this vector divided by the specified value
     */
    public Vector divided(double a) {
        return new Vector(x / a, y / a, z / a);
    }

    /**
     * Linearly interpolates between this and the specified vector.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @param a vector
     * @param t interpolation value
     *
     * @return copy of this vector if {@code t = 0}; copy of a if {@code t = 1};
     * the point midway between this and the specified vector if {@code t = 0.5}
     */
    public Vector lerp(Vector a, double t) {
        return this.plus(a.minus(this).mult(t));
    }

    /**
     * Returns the magnitude of this vector.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the magnitude of this vector
     */
    public double magnitude() {
        return sqrt(x*x+y*y+z*z);
    }

    /**
     * Returns the squared magnitude of this vector
     * (<code>this.dot(this)</code>).
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the squared magnitude of this vector
     */
    public double magnitudeSq() {
        return x*x+y*y+z*z;
    }

    /**
     * Returns a normalized copy of this vector with length {@code 1}.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return a normalized copy of this vector with length {@code 1}
     */
    public Vector normalized() {
        return this.divided(this.magnitude());
    }

    /**
     * Returns the cross product of this vector and the specified vector.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @param a the vector
     *
     * @return the cross product of this vector and the specified vector.
     */
    public Vector cross(Vector a) {
        return new Vector(
                y * a.z - z * a.y,
                z * a.x - x * a.z,
                x * a.y - y * a.x
        );
        
    }

    
    /**
     * Returns this vector in STL string format.
     *
     * @param sb string builder
     * @return the specified string builder
     */
    public void stlString(StringBuilder sb) {
        sb.append(x).append(' ').append(y).append(' ').append(z);
    }

    public String toString() {
        return x + " " + y + " " + z;
    }
    public Vector clone() {
        return new Vector(x,y,z);
    }
    
    /**
     * Returns the sum of this vector and the specified vector.
     *
     * @param x x coordinate of the vector to add
     * @param y y coordinate of the vector to add
     * @param z z coordinate of the vector to add
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the sum of this vector and the specified vector
     */
    public Vector plus(double x, double y, double z) {
        return new Vector(x + this.x, y + this.y, z + this.z);
    }

    /**
     * Returns the difference of this vector and the specified vector.
     *
     * @param x x coordinate of the vector to subtract
     * @param y y coordinate of the vector to subtract
     * @param z z coordinate of the vector to subtract
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the difference of this vector and the specified vector
     */
    public Vector minus(double x, double y, double z) {
        return new Vector(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Returns the product of this vector and the specified vector.
     *
     * @param x x coordinate of the vector to multiply
     * @param y y coordinate of the vector to multiply
     * @param z z coordinate of the vector to multiply
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the product of this vector and the specified vector
     */
    public Vector times(double x, double y, double z) {
        return new Vector(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Projects the specified vector onto this vector.
     *
     * @param v vector to project onto this vector
     * @return the projection of the specified vector onto this vector
     */
    public Vector project(Vector v) {
        final double pScale = v.dot(this) / magnitudeSq();
        return this.mult(pScale);
    }

    public static Vector centroid(ArrayList<Vector> vertices) {
        Vector sum = Vector.ZERO;

        for (Vector v : vertices) {
            sum = sum.plus(v);
        }
        return sum.mult(1.0 / vertices.size());
    }
        
    public static final double EPSILON = 1e-7;//1e-7 Creating sphere's doens't work if this value gets much higher
        
    public boolean isNaN(){
        return x == Double.NaN || y == Double.NaN || z == Double.NaN;
    }
    
    public static Vector X(double x){
        return new Vector(x, 0, 0);
    }
    public static Vector Y(double y){
        return new Vector(0, y, 0);
    }
    public static Vector Z(double z){
        return new Vector(0, 0, z);
    }
    
    public boolean isCollinear(Vector a, Vector b){ 
        return Angle.normal(a, b, this).equals(Vector.ZERO);
    }
    
   
    public boolean equals(Vector v) {
        return Math.abs(x - v.x) + Math.abs(y - v.y) + Math.abs(z - v.z) 
                <= EPSILON;
    }
    
    public boolean counterClockwise(Vector other, Vector center){
        return (x - center.x) * (other.y - center.y) - (other.x - center.x) * (y - center.y) < 0;
    }

    public boolean parralell(Vector v){
        return cross(v).abs() <= EPSILON;
    }
    public final void setTo(Vector v){
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public double[] array() {
        double[] temp = new double[3];
        temp[0] = x;
        temp[1] = y;
        temp[2] = z;
        return temp;
    }
    
    public double angle(Vector v){
        return Math.acos((dot(v))/(magnitude()*v.magnitude()));
    }
    /**
     * get and set the new coordinates for the vector 
     * (this method is useful to reuse created vector rather than  create new one) 
     * @param a
     * @param b
     * @param c 
     * @author Alessandro Della Seta
     */
    public void setCoordinates(double a, double b, double c){
        this.x = a;
        this.y = b;
        this.z = c;
    }
}
