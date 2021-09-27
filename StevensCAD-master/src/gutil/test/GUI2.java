/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package gutil.test;

/**
 *Z
 * @author itay
 */
import customShape.ProcessOverlaps;
import customTesting.paddle.Paddle;
import editorV2.CADEditor2;
import gutil.Action;
import gutil.EditedAction;
import gutil.App;
import gutil.BeanEditor;
import gutil.Conf;
import gutil.IrreversibleAction;
import gutil.ConfEditor;
import gutil.FileTypeFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.PI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import primitives.Cylinder;
import primitives.Sphere;
import primitives.Cube;
import primitives.Shape;
import processing.core.PApplet;
import scad.Operations;
import scad.STL;
import primitives.SphereSurface;
import scad.testOperations.DovOperations;
import primitives.SphericalShell;
import scad.hull.ConvexHull;
import scad.testOperations.HighDegreeTreeOperations;
import test.Convexhull_test;
import viewer.CADEditor;

public class GUI2 extends App {
    
    private CADEditor2 view2;
    //private CADEditor view;
    
    public static enum Commands {
        MOVE, SCALE, ROTATEX, ROTATEY, ROTATEZ, ROTATE,
        GROUP, UNGROUP,
        ZOOM_IN, ZOOM_OUT
    };
    
    private static BeanEditor sphereEditor, cylinderEditor, cubeEditor, surfaceEditor;
    private static BeanEditor unionEditor, intersectionEditor, diffEditor, convexHullEditor;
    private static BeanEditor extrusionEditor, shellEditor;
    private static ConfEditor confEditor;
    
    private Container c;
    private JPanel EditorPanel;
    private JPanel EditorPanelPos;
    // all initialization of this class should go here.
    static {}
    
    //Adjust Windows Alignment
    private final int windowsAlignment = 75;
    
    private void buildEditors() {
        int x = getX()+20, y = getY()+30; // get the location of this window with a temporary offset
        int w = 400, h = 500;
        String[] transformationAttributes = {"x", "y", "z"};
        String[] sphereAttributes = {"Radius", "LatRes", "LongRes"};
        sphereEditor = new BeanEditor(this, view2, Sphere.class, sphereAttributes, transformationAttributes);
        String[] cylinderAttributes = {"Radius", "Height", "Res"};
        cylinderEditor = new BeanEditor(this, view2, Cylinder.class, cylinderAttributes, transformationAttributes);
        String[] cubeAttributes = {"Size"};
        cubeEditor = new BeanEditor(this, view2, Cube.class, cubeAttributes, transformationAttributes);
        String[] sphereSurfaceAttributes = {"MinUAsDegrees", "MaxUAsDegrees", "MinVAsDegrees", "MaxVAsDegrees", "URes", "VRes", "Radius"};
        surfaceEditor = new BeanEditor(this, view2, SphereSurface.class, sphereSurfaceAttributes, transformationAttributes);
        confEditor = new ConfEditor(getConf());
    }
    private void buildFileCommands() {
        new IrreversibleAction("NEW") {
            public void doIt(ActionEvent e) {
                //TODO: if not saved, ask to save.  Then clear display and reset transforms
            }
        };
        new IrreversibleAction("OPEN") {
            public void doIt(ActionEvent e) {
            }
        };
        new IrreversibleAction("IMPORT_STL") {
            public void doIt(ActionEvent e) {
                String filename = createFileDialog("IMPORT_STL", ".stl");
                if (filename != null) {
                    view2.loadSTL(filename);
                }
            }
        };
        new IrreversibleAction("EXPORT_STL") {
            public void doIt(ActionEvent e) {
                String filename = showSaveDialog();
                //filename = createFileDialog("EXPORT_STL", ".stl");
                if (filename != null) {
                    view2.saveSTL(filename);
                    
                }
            }
            
        };
        new IrreversibleAction("IMPORT_STLB") {
            public void doIt(ActionEvent e) {
                String filename = createFileDialog("IMPORT_BINARY_STL", ".stlb");
                try {
                    STL.parseBinary(new File(filename));
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        };
        new IrreversibleAction("EXPORT_STLB") {
            public void doIt(ActionEvent e) {
                //    String filename = createFileDialog("EXPORT_BINARY_STL", ".stlb");
                String filename= showSaveDialog();
                //TODO: This currently only writes a single shape out to binary STL.  We should write all?  Or at least suport selecting and writing out not the first one.
                if (filename != null)
                    STL.writeBinarySTL(view2.getShapes().get(0), filename);
            }
        };
        new IrreversibleAction("IMPORT_BIF") {
            public void doIt(ActionEvent e) {
            }
        };
        new IrreversibleAction("EXPORT_BIF") {
            public void doIt(ActionEvent e) {
                String filename = createFileDialog("EXPORT_BIF", ".bif");
                STL.writeBIF(view2.getShapes(), filename);
            }
        };
    }
    
    private void setEditor(BeanEditor editor, Object obj) {
        c.remove(EditorPanel);
        EditorPanel = editor;
        editor.edit(obj);
        c.add(EditorPanel, EditorPanelPos);
        c.validate();
        c.repaint();
    }
    
    private void buildInsertCommands() {
        new EditedAction("INSERT_SPHERE", sphereEditor) {
            public void doIt(ActionEvent e) {
                setEditor(sphereEditor, view2.addShapeR(new Sphere(30, 30, 30)));
            }
        };
        new EditedAction("INSERT_CYLINDER", cylinderEditor) {
            public void doIt(ActionEvent e) {
                setEditor(cylinderEditor, view2.addShapeR(new Cylinder()));
            }
        };
        new EditedAction("INSERT_CUBE", cubeEditor) {
            public void doIt(ActionEvent e) {
                setEditor(cubeEditor, view2.addShapeR(new Cube()));
            }
        };
        new IrreversibleAction("INSERT_2SPHERE_SEP") {
            public void doIt(ActionEvent e) {
                view2.addShape(new Sphere(30, 40, 40));
                Sphere s = new Sphere(30, 40, 40);
                s.translate(35, 0, 0);
                view2.addShape(s);
            }
        };
        new IrreversibleAction("INSERT_3SPHERE") {
            public void doIt(ActionEvent e) {
                view2.addShape(new Sphere(30, 100, 100));
                Sphere s = new Sphere(30, 100, 100);
                s.translate(65, 0, 0);
                view2.addShape(s);
                Sphere s2 = new Sphere(30, 100, 100);
                s2.translate(0, 65, 0);
                view2.addShape(s2);
            }
        };
    }
    
    private void buildEditCommands() {
        new Action("COPY") {
            public void doIt(ActionEvent e) {
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("CUT") {
            public void doIt(ActionEvent e) {
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("PASTE") {
            public void doIt(ActionEvent e) {
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("CLEAR") {
            public void doIt(ActionEvent e) {
                view2.clearScene();
            }
            public void undo(ActionEvent e) {
            }
        };
        
        new Action("EDIT_CONFIG") {
            public void doIt(ActionEvent e) {
                
            }
            public void undo(ActionEvent e) {
            }
        };
        
        new IrreversibleAction("SAVE_CONFIG") {
            public void doIt(ActionEvent e) {
                
            }
        };
        new IrreversibleAction("LOAD_CONFIG") {
            public void doIt(ActionEvent e) {
                
            }
        };
        new IrreversibleAction("CONVEX_HULL") {
            public void doIt(ActionEvent e) {
                System.out.println("TODO: create convex hull out of the currently selected objects");
            }
        };
    }
    
    private void buildSelectCommands() {
        new Action("SELECT_ONE") {
            public void doIt(ActionEvent e) {
            }
            public void undo(ActionEvent e) {
            }
        };
        
        new Action("SELECT_ADD") {
            public void doIt(ActionEvent e) {
            }
            public void undo(ActionEvent e) {
            }
        };
        
        new Action("SELECT_RECT") {
            public void doIt(ActionEvent e) {
            }
            public void undo(ActionEvent e) {
            }
        };
        
    }
    
    private void buildOperationCommands() {
        new Action("UNION") {
            public void doIt(ActionEvent e){
                System.out.println("UNION operation.");
                new ProcessOverlaps(view2, "remove");
            }
            public void undo(ActionEvent e){
                
            }
        };
        new Action("INTERSECTION"){
            public void doIt(ActionEvent e){
                System.out.println("INTERSECTION operation.");
                new ProcessOverlaps(view2, "remain");
            }
            public void undo(ActionEvent e){
                
            }
        };
        new Action("DIFFERENCE"){
            public void doIt(ActionEvent e){
                System.out.println("DIFFERENCE operation.");
                new ProcessOverlaps(view2, "biRemove");
            }
            public void undo(ActionEvent e){
                
            }
        };
        new Action("CONVEX_HULL"){
            public void doIt(ActionEvent e){
                System.out.println("CONVEX_HULL operation.");
            }
            public void undo(ActionEvent e){
                
            }
        };
    }
    
    private void buildLanguageCommands() {
        new Action("English") {
            public void doIt(ActionEvent e) {
                setLanguage("en");
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("中文") {
            public void doIt(ActionEvent e) {
                setLanguage("中文");
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("Español") {
            public void doIt(ActionEvent e) {
                setLanguage("es");
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("Italiano") {
            public void doIt(ActionEvent e) {
                setLanguage("it");
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("عربى") {
            public void doIt(ActionEvent e) {
                setLanguage("ar");
            }
            public void undo(ActionEvent e) {
            }
        };
        new Action("עברית"){
            public void doIt(ActionEvent e){
                setLanguage("he");
            }
            public void undo(ActionEvent e){
                
            }
        };
        
    }
    
    private void buildSurfaceCommands() {
        new EditedAction("INSERT_SPHERICAL", surfaceEditor) {
            public void doIt(ActionEvent e) {
                
                setEditor(surfaceEditor, view2.addShapeR(new SphereSurface(50, 50, 50)));
            }
            
            public void undo(ActionEvent e) {
                
            }
        };
        new Action("INSERT_CYLINDRICAL") {
            public void doIt(ActionEvent e) {
                
            }
            
            public void undo(ActionEvent e) {
                
            }
        };
        new Action("INSERT_PARABOLOID") {
            public void doIt(ActionEvent e) {
                
            }
            
            public void undo(ActionEvent e) {
                
            }
        };
        new Action("INSERT_PLANE") {
            public void doIt(ActionEvent e) {
                
            }
            
            public void undo(ActionEvent e) {
                
            }
        };
        new Action("GENERATE_SHELL") {
            public void doIt(ActionEvent e) {
                SphericalShell s = new SphericalShell(5,1,20,20);
                
                File f = new File("earth1000x500.png");
                
                try {
                    s.setTickness(f);
                } catch (IOException ex) {
                    Logger.getLogger(GUI2.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                setEditor(surfaceEditor, view2.addShapeR(s));
            }
            
            public void undo(ActionEvent e) {
                
            }
        };
    }
    
    private void buildTestCommands(){
        new Action("KAYAK"){
            public void doIt(ActionEvent e){
                System.out.println("Hi");
                Paddle paddle;
                try {
                    DovOperations o = new DovOperations();
                    paddle = new Paddle(o);
                    view2.addShape(paddle.full());
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            public void undo(ActionEvent e){
                
            }
        };
        /// added by Fady
        /// it wont work for now because the working version is in ConvexHull branch
        // it'll work after performing a full merge from Convexhull branch to this master branch
        new IrreversibleAction("ConvexHull"){
            public void doIt(ActionEvent e){
                System.out.println("ConvexHull test mode");
                //Convexhull_test convTest;
                try {
                    Convexhull_test.main(null);
                    //convTest = new Convexhull_test();
                    //  view.add(convTest.points);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            public void undo(ActionEvent e){
            }
        };
    }
    
    private void buildCommands() {
        super.buildStandardCommands();
        buildFileCommands();
        buildInsertCommands();
        buildEditCommands();
        buildSelectCommands();
        buildOperationCommands();
        buildLanguageCommands();
        buildSurfaceCommands();
        buildTestCommands();
    }
    
    private void buildLayout() {
        
    }
    
    public void reshapeView() {
        view2.setWindowLocation(getX() + getWidth(), getY()+windowsAlignment);
        view2.setWindowHeight(getHeight()-windowsAlignment);
    }
    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                reshapeView();
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
                reshapeView();
            }
            
            @Override
            public void componentShown(ComponentEvent e) {
            }
            
            @Override
            public void componentHidden(ComponentEvent e) {
            }
            
        });
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent we) {
                view2.setFocused(true);
                System.out.println("GAINED FOCUS");
            }
            
            @Override
            public void windowLostFocus(WindowEvent we) {
                //view2.setFocused(false);
                System.out.println("LOST FOCUS");
                
            }
        });
    }
    
    private void setDefaultPanel(){
        EditorPanel = new JPanel();
        EditorPanel.setBackground(Color.black);
        c.add(EditorPanel,EditorPanelPos);
    }
    
    private void selectOperations() {
        String operationsName = getConf().defaulted("op", "Dov");
        Operations op = null;
        if (operationsName.equals("Dov")) {
            op = new DovOperations();
        } else if (operationsName.equals("XXX")) {
            op = null; // TODO: put in your operations type here!
        } else if (operationsName.equals("Kruger"))
            op = new HighDegreeTreeOperations();
        view2.setOperations(op);
    }
    
    public GUI2(Conf conf) throws FileNotFoundException {
        super(conf);
        setDocsURL("https://www.nytimes.com");
        setLocalDocsURL("file:://home/dkruger");
        view2 = new CADEditor2(1000,getHeight()-windowsAlignment,"CAD Editor",this);
        buildEditors(); // must be before commands!
        buildCommands();
        buildLayout();
        addListeners();
        c = getContentPane();
        setDefaultPanel();
        selectOperations();
        setVisible(true);
        reshapeView();
        view2.openWindow();
        dispose();
    }
    
    public static void main(String[] args) throws Exception {
        //Hey dov, So I was bored and i looked in here again,
        //The problem with the filechooser that crashes with OpenGL is related to X11 which is
        //apperantly a window managemnt  system? Hope this helps...
        Conf conf = new Conf("conf/stevenscad.conf");
        GUI2 gui2 = new GUI2(conf);
    }
}

