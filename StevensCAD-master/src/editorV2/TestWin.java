/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import static com.jogamp.newt.event.MouseEvent.PointerType.Mouse;
import gutil.Action;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.joml.Matrix4f;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
import shaders.Shader;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.NULL;
/**
 *Based on Itay's window, just strip down to bare bones
 * @author Dov Kruger
 */
public class TestWin {
    private int width, height;
    private String name;
    private long windowRef;
    private float tx,ty,tz;
    private float s;
    private Matrix4f proj;
    private float[] projectionMatrixArray;
    public TestWin(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
        initWindow();
    }

    private void initWindow() {
        //Makes sure window can work
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to Initialize GLFW!");
        }

        //Create window object and set its hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        this.windowRef = glfwCreateWindow(width, height, name, NULL, NULL);

        if (windowRef == 0) {
            throw new IllegalStateException("Failed to create Window!");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowRef, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

        //Keyboard Listener
        glfwSetKeyCallback(windowRef, (window, key, scancode, action, mods) -> {
            int keyEvent = (mods << 10) + (action << 9) + scancode;
//            System.out.println(key + "," + scancode + "," + action + "," + mods);
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
            
            switch(key) {
                case GLFW_KEY_RIGHT: tx++; break;
                case GLFW_KEY_LEFT: tx--; break;
                case GLFW_KEY_UP: ty++; break;
                case GLFW_KEY_DOWN: ty--; break;
            }
            proj.identity();
            proj.scale(s);
            proj.translate(tx, ty, tz);
            if(key == GLFW_KEY_F1 && action == GLFW_PRESS){
                System.out.println("F1 Pressed!");
            }
            
        });
          //Scroll Wheel Listener
        glfwSetScrollCallback(windowRef, new GLFWScrollCallback() {
            @Override
            public void invoke(long win, double dx, double dy) {
                if (dy > 0) {
                    s *= 1.125; //tz++;// += Math.pow(dy, tz);
                } else {
                    s /= 1.125; //tz--; // -= Math.pow(dy, tz);
                }
                System.out.println(tz);
                proj.identity();
                proj.scale(s);
                proj.translate(tx, ty, tz);
            }
        });

        //Mouse Listener
        glfwSetMouseButtonCallback(windowRef, GLFWMouseButtonCallback.create((window, button, action, mods) -> {
            System.out.println(action + "," + mods);
            /*if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(windowRef, x, y);

                System.out.println("X: " + x.get() + " Y: " + y.get());
            }
            */
        }));

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowRef);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(windowRef);
        tx = ty = tz = 0;
        s = 0.01f;
    }
    public void mainLoop() {
        //Make GL capabilites for window
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        buildGL();
//        proj = new Matrix4f().ortho(-width/2, width/2, -height/2,height/2, 0, 100);
        proj = new Matrix4f().perspective(-1, +1, -1, +1);
        projectionMatrixArray = new float[16];
        //Shader shader = new Shader();  //NO SHADERS, simple old OpenGL
        //int pID = shader.loadShaders("shader", "shader");

        while (!glfwWindowShouldClose(windowRef)) {
            //Render Stuff here
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                //shader.bind(pID);
                render();
                glfwSwapBuffers(windowRef);
            glfwPollEvents();
            
        }
    }

    private void buildGL() {
    }

    private void render() {
        glMatrixMode(GL_PROJECTION_MATRIX);
        proj.get(projectionMatrixArray);
        glLoadMatrixf(projectionMatrixArray);
//        glLoadIdentity();
//        glScaled(0.01,0.01,0.01);
//        glTranslated(tx,ty,tz);
        
        glBegin(GL_TRIANGLES);
        glVertex2f(0,0);
        glVertex2f(100,0);
        glVertex2f(0,100);
        glEnd();
    }
    public static void main(String[] args) {

        TestWin w = new TestWin(800, 600, "Test");
                JFrame c = new JFrame();
        JButton b = new JButton("Hi");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser h = new JFileChooser();
                h.showSaveDialog(c);
               
            }
            
        });
        c.add(b,BorderLayout.CENTER);
        c.setSize(300,400);
        c.setVisible(true);
        w.mainLoop();
        
    }
}
/* 
Draw Types:

        GL_POINTS         = 0x0,
        GL_LINES          = 0x1,
        GL_LINE_LOOP      = 0x2,
        GL_LINE_STRIP     = 0x3,
        GL_TRIANGLES      = 0x4,
        GL_TRIANGLE_STRIP = 0x5,
        GL_TRIANGLE_FAN   = 0x6,
        GL_QUADS          = 0x7,
        GL_QUAD_STRIP     = 0x8,
        GL_POLYGON        = 0x9;

 */
