
package viewer;

/**
 *
 * @author dov
 */
public class Color {
    public final int r, g, b;

    public Color(int red, int green, int blue) {
        this.r = red;
        this.g = green;
        this.b = blue;
    }
    
    public static Color red(){
        return new Color(255, 0, 0);
    }
    public static Color green(){
        return new Color(0, 255, 0);
    }
    public static Color blue(){
        return new Color(0, 0, 255);
    }
    public static Color black(){
        return new Color(0, 0, 0);
    }
    public static Color white(){
        return new Color(255, 255, 255);
    }
    public static Color yellow(){
        return new Color(255, 255, 0);
    }
    public static Color purple(){
        return new Color(255, 0, 255);
    } 
    public static Color cyan(){
        return new Color(0, 255, 255);
    }
}
