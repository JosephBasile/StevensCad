
package GUI;


import java.util.ArrayList;
import primitives.Shape3d;

/**
 *
 * All named polyhedron are listed in this class
 */
public class GetNamedShape3ds {
    private static ArrayList<NameShape3d> namedShape3ds = new ArrayList<>();
    
    public static void addNamedShape3ds(NameShape3d p) {
        namedShape3ds.add(p);
    }
    
    public static ArrayList<NameShape3d> getNamedShape3ds() {
        return namedShape3ds;
    }
}
