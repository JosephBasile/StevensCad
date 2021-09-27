//TODO:  save polyhedrons as list of facets similar to stl triangles
//also save constructor (analytical) information.
//make a demo/test file
//write method to test how long something takes
/**
 * This project is licensed under GPL 2.1. See @link License.html
 */
package viewer;

import gutil.test.GUI;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON2;
import java.awt.event.MouseListener;
import processing.core.*;

import java.io.*;
import processing.core.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import mode.ModeBase;
import mode.MoveMode;
import primitives.Cube;
import primitives.Cylinder;
import primitives.Shape;
import primitives.Shape3d;
import primitives.Sphere;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import scad.Facet;
import scad.Operations;
import scad.Polyhedron;
import scad.STL;
import static scad.STL.writeBinarySTL;
import static scad.STL.writeTextSTL;
import scad.Triangle;
import static scad.hull.ConvexMethods.Axis.*;

/**
 *
 * @author dkruger Draw SCAD shapes as solids, as wire frame, and show volumes
 * being subtracted also for ease of debugging models.
 */
public class CADEditor extends PApplet {
    public enum Mode {
        WIREFRAME, TRIANGLE, DOUBLEWALL, VERTEX, POINTS
    };
    
    private static Color STROKE_COLOR, INSIDE_COLOR, OUTSIDE_COLOR, SELECTED_INSIDE_COLOR, SELECTED_OUTSIDE_COLOR;
   
    static {
        INSIDE_COLOR = Color.purple();
        STROKE_COLOR = new Color(180, 150, 0);
        //OUTSIDE_COLOR = new Color(180, 150, 0); // yellow
        OUTSIDE_COLOR = Color.blue();
        SELECTED_OUTSIDE_COLOR = Color.green();
        SELECTED_INSIDE_COLOR = Color.red();
    }
    
    
    
    private WCS_Arrows wcs; // world coordinate systems axis to draw
    private PShape axisNumbers; // the number scale along the axes
    private PShape cursorPos; // display text of mouse location
    private boolean changed; // true any time the display must be updated

    
    //private ArrayList<PShape> display;
    private ArrayList<Shape> shapes;
    private HashMap<Shape, PShape> shapeMap;
    private ArrayList<Shape> selected;
    private Operations op;
    
    private int xX, yY, W, H;

    private int origx, origy;
    private float corX, corY, corZ;
    private float rotx, roty;
    private float translatex, translatey;
    private float zoom;
    private Mode regularMode;
    private Mode selectedMode;
    private ModeBase mode;
    
    public ArrayList<Shape> getShapes() { return shapes; }
    
    public void removeShape(Shape s) {
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i) == s) {
                shapes.remove(i);
                //display.remove(i);
                shapeMap.remove(shapes.get(i));
            }
        }
        if(changed) redraw();
    }

    public void replaceShape(Shape s) {
        
        if (selected.contains(s)) {
           shapeMap.put(s, create(s, selectedMode, STROKE_COLOR, SELECTED_INSIDE_COLOR, SELECTED_OUTSIDE_COLOR));
        }   else {
            shapeMap.put(s, create(s, regularMode, STROKE_COLOR, INSIDE_COLOR, OUTSIDE_COLOR));
        }
        redraw();
    }

    
    private final PShape create(Shape shape3d, Mode m, Color stroke, Color inColor, Color outColor) {

        PShape visual = createShape();

        ArrayList<Triangle> triangles = shape3d.triangles();
        System.out.println("test 1: " + triangles.size());
        switch (m) {
            case DOUBLEWALL:
                visual.beginShape(TRIANGLE);
                visual.stroke(stroke.r, stroke.g, stroke.b);
                visual.fill(inColor.r, inColor.g, inColor.b);

                for (Triangle t : triangles) {
                    Facet backSide = t.backSide();
                    for (scad.Vector v : backSide.vertex)
                        visual.vertex((float) v.x, (float) v.y, (float) v.z);
                }
                visual.endShape();
                
            case TRIANGLE:
                visual.beginShape(TRIANGLE);
                visual.stroke(stroke.r, stroke.g, stroke.b);
                visual.fill(outColor.r, outColor.g, outColor.b);
                for (Triangle t : triangles)
                    for (scad.Vector v : t.vertex)
                        visual.vertex((float) v.x, (float) v.y, (float) v.z);
                break;
            
            case WIREFRAME:
                visual.beginShape(LINES);
                visual.stroke(stroke.r, stroke.g, stroke.b);

                for (Triangle t : triangles) {
                    for (scad.Vector v : t.vertex)
                        visual.vertex((float) v.x, (float) v.y, (float) v.z);
                    visual.vertex((float) t.get(0).x, (float) t.get(0).y, (float) t.get(0).z);
                }
                break;
        }
        visual.endShape();
        return visual;
    }

    public Shape add(Shape s) {
        shapes.add(s);
        //display.add(ShapeView.create(this, s));
        //shapeMap.put(s, ShapeView.create(this, s));
        setSelected(s);
        if(changed) redraw();
        return s;
    }
    
    

    /*private PShape createShape(Shape s) {
        
        
    }*/
    
    
    public void setSelected(Shape s) {
        shapeMap.put(s, create(s, selectedMode, STROKE_COLOR, SELECTED_INSIDE_COLOR, SELECTED_OUTSIDE_COLOR));
        for (Shape s2 : selected) {
            shapeMap.put(s2, create(s2, regularMode, STROKE_COLOR, INSIDE_COLOR, OUTSIDE_COLOR));
        }
        selected.clear();
        selected.add(s);
    }

    public void setMySize(int w, int h) {
        this.W = w;
        this.H = h;
    }

    public CADEditor() {
        //display = new ArrayList<>(1024);
        shapes = new ArrayList<>(1024);
        shapeMap = new HashMap<>(1024);
        selected = new ArrayList<>(1024);
        regularMode = Mode.DOUBLEWALL; // Mode.WIRE
        selectedMode = Mode.DOUBLEWALL;
        changed = true;
        mode = new DefaultMode();
        //mode = new MoveMode();
    }

    public void setOperations(Operations op) { this.op = op; }
    @Override
    public void settings() {
        size(1024, 768, P3D);
        resetView();
    }

    public void resetView() {
        System.out.println("reset view");
        rotx = 0;
        roty = 0;
        translatex = 0;
        translatey = 0;
        zoom = 1;
    }

    @Override
    public void clear() {
        shapeMap.clear();
        shapes.clear();
        selected.clear();
        redraw();
    }

    @Override
    public void setup() {
        //TODO: This will be much more efficient but we will have to put redraw() everywhere we want the screen to redraw
        if(changed) {
            noLoop();
        }
         
    }

    @Override
    public void draw() {
        if (g != null) {
           if (wcs == null)
               wcs = new WCS_Arrows(g, 3, 20, WCS_Arrows.Pos.topLeft, width, height);
        }
        draw(g);
    }

    private final void conditionalDraw(PGraphics g, PShape p) {
        if (p != null) {
            g.pushMatrix();
            g.shape(p);
            g.popMatrix();
        }
    }
    
    public void draw(PGraphics g) {
        background(0);
        cursor(CROSS);
        //mouseCoords.set(mouseX, mouseY); 
        conditionalDraw(g, wcs);
        conditionalDraw(g, axisNumbers);
 /*
        for (PShape p : decoration) {
            shape(p);
        }
        */
        translate(width/2, height/2);
        translate(translatex, translatey);
        //System.out.println(translatex + "," + translatey);
        scale(zoom);
        rotateX(rotx);
        rotateY(roty);
        
        for(HashMap.Entry<Shape, PShape> entry : shapeMap.entrySet()) {
            PShape p = entry.getValue();
            g.shape(p);
        }
/*        
        for(Shape s : selected) {
            PShape p = shapeMap.get(s);
            g.shape(p);
        }
        */
        /*for (PShape p : display) {
            //TODO: do we have to use pushmatrix() here?
            g.shape(p);
        }*/
//        pushMatrix();   // dispalys the mouse cursor position on the draw screen
        
        /*
        textMode(SHAPE);

        translate(25, height - 15);
        scale(4);
        fill(255, 0, 0);
        text("X: " + mouseX, 0, 0);
        fill(0, 255, 0);
        text("Y: " + mouseY, 15, 0);
        fill(255, 255, 0);
        //text("Coordinate (" + modelX(mouseX,mouseY,0) +", "+ modelY(mouseX,mouseY,0)+", "+modelZ(mouseX,mouseY,0)+")", 150,0);
        text("Coordinate (" + corX + ", " + corY + ", " + corZ + ")", 40, 0);

        popMatrix();


        pushMatrix();   // draws the WCS arrows 

        translate(transX, transY, 0);
        scale(50);  // the size of the arrows
        rotateX(rotx);
        rotateY(roty);
        for (PShape p : WCS) {
            shape(p);
        }

        popMatrix();
*/
    }

    /**
     * Save into native StevensCAD format
     *
     * @param filename
     * @throws java.io.IOException
     */
    public void saveCAD(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeInt(shapes.size());
            for (Shape s : shapes) {
                //s.writeObject(oos);
            }
        }

    }

    /**
     * Load from native StevensCAD format
     *
     */
    public void loadCAD(String filename) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
        int numShapes = ois.readInt();
        for (int i = 0; i < numShapes; i++) {
            shapes.add(Shape3d.readObject3D(ois));
        }
        ois.close();
    }

    public void saveBinarySTL(String filename) {
        DataOutputStream d = null;
        try {
            d = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
            d.writeBytes("Binary STL: Generated by Stevens CAD");
            for (Shape s : shapes) {
                STL.writeBinarySTL1(s, d);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (d != null) {
                    d.close();
                }
            } catch (IOException e2) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e2);
            }
        }
    }

    public void saveSTL(String filename) {
        FileWriter f = null;
        try {
            f = new FileWriter(filename + ".stl");
            for (Shape s : shapes) {
                STL.writeSTL1(s, f);
            }
            f.close();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
    public void loadSTL(String filename) {
        try {
            FileReader f = new FileReader(filename);
            Polyhedron p = STL.readSTL1(f);
            add(p);
        } catch(IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
    @Override
    public void keyPressed() {   // reset screen current view
        if (key == 'r') {
            resetView();
        }
    }

    @Override
    public void mousePressed() {
        origx = mouseX;
        origy = mouseY;
        mode.mousePressed(mouseX, mouseY);
        
        
        /*MoveMode m = new MoveMode(selected);
        m.mouseDragged(800, 800, origx, origy);*/
    }

    private final float rotationSpeed = 6;

    @Override
    public void mouseDragged(MouseEvent e) {
        /*int dx = (mouseX - origx), dy = (mouseY - origy);
        origx = mouseX;
        origy = mouseY;

        if (mouseButton == LEFT) {

            roty += rotationSpeed * dx / width;
            rotx += -1 * rotationSpeed * dy / height;
        } else if (mouseButton == RIGHT) {
            translatex += dx;
            translatey += dy;
        }*/
        mode.mouseDragged(e);
        redraw();
    }

    @Override
    public void mouseWheel(MouseEvent e) {
        float c = e.getCount();
        if (c > 0) {
            zoom /= 1.05f;
        } else {
            zoom *= 1.05f;
        }
        redraw();
    }
//   public void keyPressed(KeyEvent event) {
//        if(38 == event.getKeyCode()) zoom *= 1.05f;
//        if(40 == event.getKeyCode()) zoom /= 1.05f;
//    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (38 == event.getKeyCode()) {
            zoom *= 1.05f;
        }
        if (40 == event.getKeyCode()) {
            zoom /= 1.05f;
        }
    }

    public static PApplet main(String[] args) {
        CADEditor ed = (CADEditor)CADEditor.main("viewer.CADViewer");
        return ed;
    }

    public static void openWindow(Shape3d s) {
        CADEditor ed = CADEditor.openWindow();
        ed.add(s);
    }

    public static CADEditor openWindow() {
        return openWindow(0, 0, 1024, 768);
    }

    public static CADEditor openWindow(int x, int y, int w, int h) {
        CADEditor editor = (CADEditor) CADEditor.main("viewer.CADEditor");
        //editor.setMySize(w, h);
        editor.setLocation(x,y);
        //editor.setWindowSize(w,h);
        try {
            Thread.currentThread().sleep(200); 
        } catch (Exception e) {
            e.printStackTrace();
    }
        return editor;
    }

    public class DefaultMode extends ModeBase {
    
    public DefaultMode() {
        super();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println("mouseDragged");
        int dx = (e.getX() - origx), dy = (e.getY() - origy);
        //System.out.println(dx);
        origx = e.getX();
        origy = e.getY();
        //System.out.println(dy);
        if (e.getButton() == 37) {
            roty += rotationSpeed * dx / width;
            rotx += -1 * rotationSpeed * dy / height;
        } else if (e.getButton() == BUTTON2) {
            translatex += dx;
            translatey += dy;
        }
        redraw();
    }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("mouseReleased");
        }
    }
    
    public class MoveMode extends ModeBase {

        
        public MoveMode() {
            super();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            translatex = e.getX() - origx;
            translatey = e.getY() - origy;
        }
        
    }
    
    public class SelectMoveMode extends ModeBase {

        public SelectMoveMode() {
            super();
        }
        
        public void mouseClicked(MouseEvent e) {
            for(HashMap.Entry<Shape, PShape> entry : shapeMap.entrySet()) {
                Shape p = entry.getKey();
                //if(shape p is clicked) {
                    selected.add(p);
                //}
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            for(Shape s : selected) {
                s.translate(e.getX() - origx, e.getY() - origy, 0.0);
            }
        }
        
    }
    
}