/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import gutil.test.GUI2;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import primitives.Shape;
import scad.Operations;
import scad.Polyhedron;
import scad.STL;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

/**
 *
 * @author itay
 */
public class CADEditor2 extends Window {

    Operations op;
    GUI2 gui2;

    public CADEditor2(int width, int height, String name, GUI2 gui2) {
        super(width, height, name);
        this.op = null;
        this.gui2 = gui2;
       // w2d = new Window2D(width, height, name);
        windowChangeHandler();
    }


    //Set Opperations for project
    public void setOperations(Operations op) {
        this.op = op;
    }

    //Show Window... This must be called after the other window is opened. 
    public void openWindow() {
        while (!glfwWindowShouldClose(windowRef)) {
            this.renderWindow();
            //w2d.renderWindow();
        }

    }

    //Adds Shape to window
    public Shape addShapeR(Shape shape) {
        addShape(shape);
        return shape;
    }

    //Get Focus
    public void requestFocus() {
        glfwRequestWindowAttention(getWindowRef());
    }

    //Load an STL file to scene
    public void loadSTL(String filename) {
        try {
            FileReader f = new FileReader(filename);
            Shape s = STL.readSTL1(f);
            this.addShape(s);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    //Save scene as an STL File
    public void saveSTL(String filename) {
        FileWriter f = null;
        try {
            f = new FileWriter(filename);
            for (Shape s : this.getShapes()) {
                STL.writeSTL1(s, f);
            }
            f.close();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    //Replaces a shape
    public void replaceShape(Shape shapeObj) {
        updateShape(shapeObj);
    }

    //Builds Actions
    private final void buildActions() {
        new DisplayChangingAction("TRANSLATEX", this) {
            public void doIt() {

            }
        };
    }

    //Set location of Window
    public void setWindowLocation(int x, int y) {
        glfwSetWindowPos(getWindowRef(), x, y);
        setChanged(true);
    }

    //Handle window changes
    private void windowChangeHandler() {
        //Window Resize
        glfwSetWindowSizeCallback(getWindowRef(), new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width_in, int height_in) {
              
                /*setDimensions(width_in, height_in);
                gui2.setSize(gui2.getWidth(), height_in);
                gui2.repaint();
                setChanged(true);*/
            }
        });

        glfwSetWindowPosCallback(getWindowRef(), new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
                //Clip editor to main window
                gui2.reshapeView();
            }
        });

    }

    //Set height of Window
    public void setWindowHeight(int height) {
        glfwSetWindowSize(getWindowRef(), getWidth(), height);
        setChanged(true);
    }
    
    public void setFocused(boolean isFocused){
        if(isFocused)
        glfwFocusWindow(windowRef);
        else
        glfwHideWindow(windowRef);
    }

}
