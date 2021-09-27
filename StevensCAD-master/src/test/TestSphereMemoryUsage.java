package test;
import primitives.Sphere;
/**
 *
 * @author dkruger
 */
public class TestSphereMemoryUsage {
    public static void main(String[] args) {
        for (int res = 50; res < 60000; res *= 2) {
            System.gc();
            Sphere s = new Sphere(30.0, res, res);
            System.out.println(res + ": " + s);
        }
    }
}
