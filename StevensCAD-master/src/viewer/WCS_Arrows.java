package viewer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static processing.core.PConstants.GROUP;
import static processing.core.PConstants.LINES;
import static processing.core.PConstants.QUAD_STRIP;
import static processing.core.PConstants.TRIANGLE_FAN;
import static processing.core.PConstants.TWO_PI;
import processing.core.PShape;
import processing.core.PGraphics;

/**
 *
 * @author Fady
 *
 * creates WCS (World Coordinate System) X Y Z arrows in three different colors
 * the display position can be set at one of the corners of the display screen 
 * the WCS is created from three parts , heads, shafts and axis texts
 *
 */

 

public class WCS_Arrows extends PShape {
    
   public enum Pos {   // to set the display position of the WCS arrows
        topLeft, topRight , bottomLeft, bottomRight;
    }

    private PShape shafts, cone, axisText;
    private float scale;
    private final int xColor=0xFF0000; // red;
    private final int yColor= 0x00FF00; // green;
    private final int zColor = 0x0000FF; // blue
    private static final float DEG2RAD = (float)Math.PI / 180;
    final float headTranslate = 0.6f * scale;

    
    public WCS_Arrows(PGraphics g, int weight, float scale, Pos pos, float w, float h) {
        beginShape(GROUP);
        setDisplayPosition(pos,w,h);
        addChild(buildShafts(g, weight));
        
        addChild(arrowHeadCone(g, xColor, 0, -90 * DEG2RAD, headTranslate,0,0)); //x
        addChild(arrowHeadCone(g, yColor, 0, -180 * DEG2RAD, 0,headTranslate,0)); //y
        addChild(arrowHeadCone(g, zColor, -90 * DEG2RAD, 0,  0,0,-headTranslate)); //z
        
        addChild(axisText(g,"X", xColor, scale, 0,0));
        addChild(axisText(g,"Y", yColor, 0,-scale,0));
        addChild(axisText(g,"Z", zColor, 0,0,-scale));
        endShape(GROUP);
    }
    
    private void setDisplayPosition(Pos pos, float w, float h){
        float tx,ty;
        if (pos==Pos.bottomLeft){
            tx = w * 0.1f;
            ty = h * 0.9f;
        }
    }
    
        
    private PShape buildShafts(PGraphics g, int weight) {
        shafts = g.createShape();
        shafts.beginShape(LINES);

        shafts.strokeWeight(weight);
        shafts.scale(0.5f * scale);

        // X axis arrow
        shafts.stroke(xColor);
        shafts.vertex(0, 0, 0);
        shafts.vertex(scale, 0, 0);
        
        // Y axis arrow
        shafts.stroke(yColor);
        shafts.vertex(0, 0, 0);
        shafts.vertex(0, -scale, 0);
        //Z axis arrow
        shafts.stroke(zColor);
        shafts.vertex(0, 0, 0);
        shafts.vertex(0, 0, -scale);
        shafts.endShape();
        
        return (shafts);
    }
    
    
    /*
     * draw the cone arrow head for the WCS
     */
    private PShape arrowHeadCone(PGraphics g, int color, float angX, float angZ, float tx, float ty, float tz) {
        int sides = 20;
        int coneHeight = 5;
        int strokeW = 2;
        
        cone = g.createShape();

        cone.beginShape(TRIANGLE_FAN);
        cone.scale(0.05f * scale);
        if (angX != 0)
            cone.rotateX(angX);
        if (angZ != 0)
            cone.rotateZ(angZ);
        cone.translate(tx,ty,tz);

        cone.strokeWeight(strokeW);
        cone.stroke(color);
        cone.fill(color);

        float[] xx = new float[sides];
        float[] zz = new float[sides];

        for (int i = 0; i < xx.length; i++) {
            float angle = TWO_PI / sides * i;
            xx[i] = (float) (sin(angle) * scale);
            zz[i] = (float) (cos(angle) * scale);
        }

        cone.vertex(0, -coneHeight * scale / 2, 0);
        for (int i = 0; i < xx.length; i++) {
            cone.vertex(xx[i], -coneHeight * scale / 2, zz[i]);
        }
        cone.endShape();

        cone.beginShape(QUAD_STRIP);
        for (int i = 0; i < xx.length; i++) {
            cone.vertex(xx[i], -coneHeight * scale / 2, zz[i]);
            cone.vertex(0, coneHeight * scale / 2, 0);
        }
        cone.endShape();

        cone.beginShape(TRIANGLE_FAN);
        cone.vertex(0, coneHeight * scale / 2, 0);
        for (int i = 0; i < xx.length; i++) {
            cone.vertex(0, coneHeight * scale / 2, 0);
        }
        cone.endShape();
        
        return (cone);
    }

    /**
     * adds axis texts to the WCS shape
     * 
     * @param g
     * @param s
     * @param color
     * @param tx
     * @param ty
     * @param tz
     * @return 
     */
    
    private PShape axisText(PGraphics g, String s, int color, float tx, float ty, float tz) {
        
        axisText = g.createShape();
       // axisText.beginShape(LINES);
        axisText.beginShape();
        g.textMode(SHAPE);
    
        axisText.strokeWeight(5);
        axisText.scale(scale * 0.5f);
        axisText.stroke(color);
        g.text(s,tx, ty, tz);
        
        axisText.endShape();
        
        return (axisText);

    }

}
