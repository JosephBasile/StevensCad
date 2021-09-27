/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import static java.lang.Math.round;
import static processing.core.PConstants.GROUP;
import static processing.core.PConstants.LINES;
import static processing.core.PConstants.SHAPE;
import processing.core.PGraphics;
import processing.core.PShape;
import static viewer.Color.blue;
import static viewer.Color.green;
import static viewer.Color.*;

/**
 *
 * @author Fady
 *
 *
 * draws the coordinates system
 */
public final class CoordinatesSystem extends PShape {
    
    private PShape gradeLine,gradeNumber;

    public CoordinatesSystem(PGraphics g, int start, float size, int strokeWeight, int resolution) {

        beginShape(GROUP);
        addChild(drawGradeLines(g,size, strokeWeight, resolution));
        addChild(setGradeNumbers(g, start, size,resolution)); // for the three axis

        endShape(GROUP);

    }

    /**
     *
     * draws the grade line for each axis , every grade line consist of small
     * lines to represent the grade resolution
     *
     *
     * @param g
     * @param scale
     * @param strokeWeigh
     * @param resolution ==> for the grades split distance, ex , if resolution =100 that means the grade number on each axis will be increased by the amount of the resolution
     * @return
     */
    private PShape drawGradeLines(PGraphics g, float size, float strokeWeight, int resolution) { 

        float s;
        float alpha = 80;
        size = size*2;
        gradeLine = g.createShape();

        gradeLine.beginShape(LINES);
        gradeLine.strokeWeight(strokeWeight);

        // X axis 
        gradeLine.stroke(red().r, red().g, red().b, alpha);
        gradeLine.vertex(0, 0, 0);
        gradeLine.vertex((float) -size, 0, 0);
        for (int i = 4; i <= size; i += resolution) {
            if ((i / 10f) == round(i / 10)) {
                s = 4;
            } else {
                s = 2;
            }
            gradeLine.vertex(-i, 0, 0);
            gradeLine.vertex(-i, -s, 0);
        }

        gradeLine.vertex(0, 0, 0);
        gradeLine.vertex((float) size, 0, 0);
        for (int i = 1; i <= size; i += resolution) {
            if ((i / 10f) == round(i / 10)) {
                s = 4;
            } else {
                s = 2;
            }
            gradeLine.vertex(i, 0, 0);
            gradeLine.vertex(i, -s, 0);
        }

        // Y axis 
        gradeLine.stroke(green().r, green().g, green().b, alpha);
        gradeLine.vertex(0, 0, 0);
        gradeLine.vertex(0, (float) -size, 0);
        for (int i = 1; i <= size; i += resolution) {
            if ((i / 10f) == round(i / 10)) {
                s = 4;
            } else {
                s = 2;
            }
            gradeLine.vertex(0, -i, 0);
            gradeLine.vertex(-s, -i, 0);
        }
        gradeLine.vertex(0, 0, 0);
        gradeLine.vertex(0, (float) size, 0);
        for (int i = 0; i <= size; i += resolution) {
            if ((i / 10f) == round(i / 10)) {
                s = 4;
            } else {
                s = 2;
            }
            gradeLine.vertex(0, i, 0);
            gradeLine.vertex(-s, i, 0);
        }
        
        //Z axis 
        gradeLine.stroke(blue().r, blue().g, blue().b, alpha);
        gradeLine.vertex(0, 0, 0);
        gradeLine.vertex(0, 0, (float) -size);
        for (int i = 1; i <= size; i += resolution) {
            if ((i / 10f) == round(i / 10)) {
                s = 4;
            } else {
                s = 2;
            }
            gradeLine.vertex(0, 0, -i);
            gradeLine.vertex(-s, 0, -i);
        }
        gradeLine.vertex(0, 0, 0);
        gradeLine.vertex(0, 0, (float) size);
        for (int i = 1; i <= size; i += resolution) {

            if ((i / 10f) == round(i / 10)) {
                s = 4;
            } else {
                s = 2;
            }
            gradeLine.vertex(0, 0, i);
            gradeLine.vertex(-s, 0, i);
        }
        gradeLine.endShape();

        return gradeLine;
    }

    
    public PShape setGradeNumbers(PGraphics g, int startNum, float size, int res ) {
        
        gradeNumber=g.createShape();
        gradeNumber.beginShape(SHAPE);
        g.textMode(SHAPE);
        g.textSize(3);
        
        for (int i = startNum; i < size; i += res) {
            // X axis
            setMarkingNumber(g,i, i, 3, 0, Color.red()); // positive axis
            setMarkingNumber(g,-i, -i, 3, 0, Color.red()); // nigatove axis 
            // Y axis
            setMarkingNumber(g,i, 1, i, 0, Color.green());
            setMarkingNumber(g,-i, 1, -i, 0, Color.green());
            // Z axis
            setMarkingNumber(g,i, 1, 0, i, Color.blue());
            setMarkingNumber(g,-i, 1, 0, -i, Color.blue());
        }
        gradeNumber.endShape();
        
        return gradeNumber;
    }

    private void setMarkingNumber(PGraphics g, int s, int axisX, int axisY, int axisZ, Color c) {
        g.fill(c.r, c.g, c.b, 80);
        g.text(Integer.toString(s), axisX, axisY, axisZ);
    }

}

