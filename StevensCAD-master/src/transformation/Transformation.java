package transformation;

import static java.lang.Math.*;
import scad.Angle;
import scad.Facet;
import scad.Vector;
/**
 * Optimized 3x4 matrix for 3d affine transformations
 * For speed, simplicity and clarity, all rotations are in radians.
 * The only proper place for degrees is in I/O and that is not here.
 * @author Dov Kruger
 * 
 */
public class Transformation {
    /*
        Matrix is stored as scalar elements for speed
        since java arrays are objects and would require indirection
        Also for many operations we optimize by hand and only modify some elements
    */
    private double
            a, b, c, d,
            e, f, g, h,
            i, j, k, l;
    /**
     * Constructor.
     *
     * Creates the identity transform
     */
    public Transformation() {
        a = 1; b = 0; c = 0; d = 0;
        e = 0; f = 1; g = 0; h = 0;
        i = 0; j = 0; k = 1; l = 0;
    }

    public Transformation(double[] elements) {
        a = elements[0]; b = elements[1]; c = elements[2]; d = elements[3];
        e = elements[4]; f = elements[5]; g = elements[6]; h = elements[7];
        i = elements[8]; j = elements[9]; k = elements[10]; l = elements[11];
    }
    
    public Transformation(Vector xaxis, Vector yaxis, Vector zaxis, Vector point) {
        a = xaxis.x; b = yaxis.x; c = zaxis.x; d = point.x;
        e = xaxis.y; f = yaxis.y; g = zaxis.y; h = point.y;
        i = xaxis.z; j = yaxis.z; k = zaxis.z; l = point.z;

    }
    
    public double[] getMatrix() {
        return new double[]{a, b, c, d,
                            e, f, g, h,
                            i, j, k, l}; 
    }
      /**
     * Compute point multiplication operation
     * @return new vector
     */
    public Vector apply(Vector v)/*point multiplication operation*/
    {
        return new Vector(
                a*v.x + b*v.y + c*v.z + d,
                e*v.x + f*v.y + g*v.z + h,
                i*v.x + j*v.y + k*v.z + l);
    }


    /**
     * Applies rotation operation around the x axis to this transform.
     *
     * @param radians 
     * @return this transform
     */
    public Transformation rotX(double radians) {
        final double cs = Math.cos(radians);
        final double si = Math.sin(radians);
        double ff = f*cs -g*si;
        double gg = f*si + g*cs;
        
        double jj = j*cs - k*si;
        double kk = j*si + k*cs;
        
        f = ff;  g = gg;
        j = jj;  k = kk;
        return this;
    }

    /**
     * Applies rotation operation around the y axis to this transform.
     *
     * @param radians
     *
     * @return this transform
     */
    public Transformation rotY(double radians) {
        double cs = cos(radians);
        double si = sin(radians);
        
        double aa = a*cs+ c*si;
        double cc = -a*si + c*cs;
        double ii = i*cs + k*si;
        double kk = i * -si + k*cs;
        a = aa; c = cc;
        i = ii; k = kk;
        return this;
    }

    /**
     * Applies rotation operation around the z axis to this transform.
     *
     * @param radians
     *
     * @return this transform
     */
    public Transformation rotZ(double radians) {
        double cs = cos(radians);
        double si = sin(radians);
        double aa = a*cs - b*si;
        double bb = a*si + b*cs;
        double ee = e*cs - f*si;
        double ff = e*si + f*cs;
        a = aa; b = bb;
        e = ee; f = ff;
        return this;
    }

    /**
     * Applies a rotation operation to this transform.
     *
     * @param x x axis rotation (degrees)
     * @param y y axis rotation (degrees)
     * @param z z axis rotation (degrees)
     *
     * @return this transform
     */
    public Transformation rot(double x, double y, double z) {
        return rotX(x).rotY(y).rotZ(z);
    }

    /**
     * Applies a rotation operation to this transform.
     *
     * @param v axis rotation for x, y, z (radians)
     *
     * @return this transform
     */
    public Transformation rot(Vector v) {
        return rotX(v.x).rotY(v.y).rotZ(v.z);
    }

    /**
     * Applies a transformation that rotates one vector into another.
     *
     * @param from source vector
     * @param to target vector
     * @return this transformation
     */
    public Transformation rot(Vector from, Vector to) {
        
        Vector cross = from.cross(to);

        double l = cross.magnitude(); // sine of angle

        if (l > 1e-9) {

            Vector axis = cross.normalized();
            double angle = from.angle(to);
            Angle.normal(from, to);

            rot(Vector.ZERO, axis, angle);
        }
        else if(from.dot(to) > 0){// nearly positively aligned, skip rotation
        }
        else{//nearly negative aligned, axis is any vector perpendicular
            // to either vector, angle is 180
            Vector axis;
            if((from.z) != 0){
                axis = from.cross(new Vector(0,1,0));
            }else{
                axis = from.cross(new Vector(0,0,1));
            }
            double angle = Math.PI;
            
            rot(Vector.ZERO,axis,angle);
        }

        return this;
    }

    /**
     * Applies a rotation operation about the specified rotation axis.
     *
     * @param axisPos axis point
     * @param axisDir axis direction (may be unnormalized)
     * @param radians rotantion angle in degrees
     * @return this transform
     */
    public Transformation rot(Vector axisPos, Vector axisDir, double radians) {
        Transformation tmp = new Transformation();

        axisDir = axisDir.normalized();

        Vector dir2 = axisDir.times(axisDir);

        double posx = axisPos.x;
        double posy = axisPos.y;
        double posz = axisPos.z;

        double dirx = axisDir.x;
        double diry = axisDir.y;
        double dirz = axisDir.z;

        double dirxSquare = dir2.x;
        double dirySquare = dir2.y;
        double dirzSquare = dir2.z;

        final double cosA = cos(radians);
        double oneMinusCosOfangle = 1 - cosA;
        final double sinA = sin(radians);

        tmp.a = dirxSquare + (dirySquare + dirzSquare) * cosA;
        tmp.b = dirx * diry * oneMinusCosOfangle - dirz * sinA;
        tmp.c = dirx * dirz * oneMinusCosOfangle + diry * sinA;
        tmp.d = (posx * (dirySquare + dirzSquare)
                - dirx * (posy * diry + posz * dirz)) * oneMinusCosOfangle
                + (posy * dirz - posz * diry) * sinA;

        tmp.e = dirx * diry * oneMinusCosOfangle + dirz * sinA;
        tmp.f = dirySquare + (dirxSquare + dirzSquare) * cosA;
        tmp.g = diry * dirz * oneMinusCosOfangle - dirx * sinA;
        tmp.h = (posy * (dirxSquare + dirzSquare)
                - diry * (posx * dirx + posz * dirz)) * oneMinusCosOfangle
                + (posz * dirx - posx * dirz) * sinA;

        tmp.i = dirx * dirz * oneMinusCosOfangle - diry * sinA;
        tmp.j = diry * dirz * oneMinusCosOfangle + dirx * sinA;
        tmp.k = dirzSquare + (dirxSquare + dirySquare) * cosA;
        tmp.l = (posz * (dirxSquare + dirySquare)
                - dirz * (posx * dirx + posy * diry)) * oneMinusCosOfangle
                + (posx * diry - posy * dirx) * sinA;

        mul(tmp);

        return this;
    }

    /**
     * Applies a translation operation to this transform.
     *
     * @param v translation vector (x,y,z)
     *
     * @return this transform
     */
    public Transformation translate(Vector v) {
        return translate(v.x, v.y,v.z);
    }

    /**
     * Applies a translation operation to this transform.
     *
     * @param x translation (x axis)
     * @param y translation (y axis)
     * @param z translation (z axis)
     *
     * @return this transform
     */
    public Transformation translate(double x, double y, double z) {
        d += x;
        h += y;
        l += z;
        return this;
    }

    /**
     * Applies a translation operation to this transform.
     *
     * @param val translation (x axis)
     *
     * @return this transform
     */
    public Transformation translateX(double val) {
        d += val;
        return this;
    }

    /**
     * Applies a translation operation to this transform.
     *
     * @param val translation (y axis)
     *
     * @return this transform
     */
    public Transformation translateY(double val) {
        h += val;
        return this;
    }

    /**
     * Applies a translation operation to this transform.
     *
     * @param val translation (z axis)
     *
     * @return this transform
     */
    public Transformation translateZ(double val) {
        l += val;
        return this;
    }

    /**
     * Applies a mirror operation to this transform.
     *
     * @param facet the plane that defines the mirror operation
     *
     * @return this transform
     */
    public Transformation mirror(Facet facet) {
        double nx = facet.normal().x;
        double ny = facet.normal().y;
        double nz = facet.normal().z;
        double w = - new Vector(nx,ny,nz).dot(facet.get(0));
        final double elements[] = {                
            (1.0 - 2.0 * nx * nx),(-2.0 * nx * ny),(-2.0 * nx * nz),(-2.0 * nx * w),
            (-2.0 * ny * nx),(1.0 - 2.0 * ny * ny),(-2.0 * ny * nz),(-2.0 * ny * w),
            (-2.0 * nz * nx),(-2.0 * nz * ny),(1.0 - 2.0 * nz * nz),(-2.0 * nz * w),
            0,0,0,1
        };

        mul(new Transformation(elements));
        return this;
    }

    /**
     * Applies a scale operation to this transform.
     *
     * @param v vector that specifies scale (x,y,z)
     *
     * @return this transform
     */
    public Transformation scale(Vector v) {
        if (v.x == 0 || v.y == 0 || v.z == 0) {
            throw new IllegalArgumentException("scale by 0 not allowed!");
        }
        a *= v.x;
        f *= v.y;
        k *= v.z;
        return this;
    }

    /**
     * Applies a scale operation to this transform.
     *
     * @param x x scale value
     * @param y y scale value
     * @param z z scale value
     *
     * @return this transform
     */
    public Transformation scale(double x, double y, double z) {
        if (x == 0 || y == 0 || z == 0) {
            throw new IllegalArgumentException("scale by 0 not allowed!");
        }
        a *= x;
        f *= y;
        k *= z;
        return this;
    }

    /**
     * Applies a scale operation to this transform.
     *
     * @param s s scale value (x, y and z)
     *
     * @return this transform
     */
    public Transformation scale(double s) {
        if (s == 0) {
            throw new IllegalArgumentException("scale by 0 not allowed!");
        }
        a *= s;
        f *= s;
        k *= s;
        return this;
    }

    /**
     * Applies a scale operation (x axis) to this transform.
     *
     * @param s x scale value
     *
     * @return this transform
     */
    public Transformation scaleX(double s) {
        if (s == 0) {
            throw new IllegalArgumentException("scale by 0 not allowed!");
        }
        a *= s;
        return this;
    }

    /**
     * Applies a scale operation (y axis) to this transform.
     *
     * @param s y scale value
     *
     * @return this transform
     */
    public Transformation scaleY(double s) {
        if (s == 0) {
            throw new IllegalArgumentException("scale by 0 not allowed!");
        }
        f *= s;
        return this;
    }

    /**
     * Applies a scale operation (z axis) to this transform.
     *
     * @param s z scale value
     *
     * @return this transform
     */
    public Transformation scaleZ(double s) {
        if (s == 0) {
            throw new IllegalArgumentException("scale by 0 not allowed!");
        }

        k *= s;
        return this;
    }

    /**
     * Applies this transform to the specified vector.
     *
     * @param v vector to transform
     *
     * @return the specified vector
     */
    public Vector transform(Vector v) {
        final double x = a * v.x + b * v.y + c * v.z + d;
        final double y = e * v.x + f * v.y + g * v.z + h;
        v.z = i * v.x + j * v.y + k * v.z + l;
        v.x = x;
        v.y = y;
        return v;
    }

    /**
     * Applies this transform to the specified vector.
     *
     * @param v vector to transform
     * @param amt transform amount (0 = 0 %, 1 = 100%)
     *
     * @return the specified vector
     */
    public Vector transform(Vector v, double amt) {
        final double x = a * v.x + b * v.y + c * v.z + d;
        final double y = e * v.x + f * v.y + g * v.z + h;
        final double z = i * v.x + j * v.y + k * v.z + l;
        v.x += (x - v.x) * amt;
        v.y += (y - v.y) * amt;
        v.z += (z - v.z) * amt;
        return v;
    }

    /**
     * Indicates whether this transform performs a mirror operation, i.e., flips
     * the orientation.
     *
     * @return <code>true</code> if this transform performs a mirror operation;
     * <code>false</code> otherwise
     */
    public boolean isMirror() {
        return determinant() < 0;
    }

    /**
     * calculate the determinant.  Since this is a 3x4 matrix, the last row is 0 0 0 1
     * and therefore the determinant is really a 3d one
     * a    b   c   d
     * e    f   g   h
     * i    j   k   l
     * 0    0   0   1
     * @return the determinant of this matrix
     */
    public double determinant() {
        return a*(f*k-g*j)-b*(e*k-g*i)+c*(e*j-f*i);
    }
    
    /**
     * Applies the specified transform to this transform.
     *
     * @param t transform to apply
     *
     * @return this transform
     */
    
    /*
        a   b   c   d
        e   f   g   h
        i   j   k   l
    */
    public Transformation mul(Transformation t) {
        double aa = a*t.a + b*t.e + c*t.i;
        double bb = a*t.b + b*t.f + c*t.j;
        double cc = a*t.c + b*t.g + c*t.k;
        d += a*t.d + b*t.h + c*t.l;
        a = aa;
        b = bb;
        c = cc;
        
        double ee = e*t.a + f*t.e + g*t.i;
        double ff = e*t.b + f*t.f + g*t.j;
        double gg = e*t.c + f*t.g + g*t.k;
        h += e*t.d + f*t.h + g*t.l;
        e = ee;
        f = ff;
        g = gg;
        
        double ii = i*t.a + j*t.e + k*t.i;
        double jj = i*t.b + j*t.f + k*t.j;
        double kk = i*t.c + j*t.g + k*t.k;
        l += i*t.d + j*t.h + k*t.l;
        i = ii;
        j = jj;
        k = kk;
        
        return this;
    }
    
       public Vector dot(Vector source) {
        Vector target = new Vector(a*source.x + b*source.y + c*source.z + d,
                                   e*source.x + f*source.y + g*source.z + h,
                                   i*source.x + j*source.y + k*source.z + l);
         return target;
    }
    
    /**
     * Reflection between two coordinates
     * [m00 m01 m02 m03][x]   [m00*x + m01*y + m02*z + m03]       [x]               [x]
     * [m10 m11 m12 m13][y] = [m10*x + m11*y + m12*z + m13] = new [y] -> normalized [y]
     * [m20 m21 m22 m23][z]   [m20*x + m21*y + m22*z + m23]       [z]               [z]
     * [m30 m31 m32 m33][1]   [m30*x + m31*y + m32*z + m33]       [norm]            [1]
     * @param arr
     * @param target 
     */
    public Vector reflection3D(Vector arr) {
        Vector target;
        target = dot(arr);
        double norm = i*arr.x + j*arr.y + k*arr.z + l;
        if (norm != 1) {
            target.x /= norm;
            target.y /= norm;
            target.z /= norm;
        }
        return target;
    }
    
    /**
     * get the inverse of the transformation matrix.
     * the transformation is a 3*4 matrix, so for left 3 row we treat it as 
     * 3*3 matrix and get the inverse of this matrix. For the right row, it is
     * used for translation, so we put the negative value.
     * The original matrix multiply its inverse looks like this
     *   1    0    0    0
     *   0    1    0    0
     *   0    0    1    0
     *
     * @return the inverse transformation
     */
    public Transformation inverse(){
        double det = determinant();
        double aa, bb, cc, dd,
               ee, ff, gg, hh,
               ii, jj, kk, ll;
        dd = -d;
        hh = -h;
        ll = -l;
        double a2 = a, b2 = e, c2 = i,
               e2 = b, f2 = f, g2 = j,
               i2 = c, j2 = g, k2 = k;
        aa = (f2*k2-j2*g2)/det;
        bb = -(e2*k2-g2*i2)/det;
        cc = (e2*j2-f2*i2)/det;
        ee = -(b2*k2-c2*j2)/det;
        ff = (a2*k2-i2*c2)/det;
        gg = -(a2*j2-b2*i2)/det;
        ii = (b2*g2-f2*c2)/det;
        jj = -(a2*g2-e2*c2)/det;
        kk = (a2*f2-b2*e2)/det;
        
        double [] inv = {aa ,bb ,cc ,dd,
                         ee, ff ,gg, hh,
                         ii, jj, kk, ll};
        Transformation inver = new Transformation(inv);
        return inver;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(12*7);
        b.append(a).append('\t').append(b).append('\t').append(c).append('\t').append(d).append('\n')
                .append(e).append('\t').append(f).append('\t').append(g).append('\t').append(h).append('\n')
                .append(i).append('\t').append(j).append('\t').append(k).append('\t').append(l).append('\n');
        return b.toString();
    }
}
