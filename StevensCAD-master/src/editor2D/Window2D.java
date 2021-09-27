/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor2D;

import editorV2.Camera;
import extrusion.Bezier;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;
import primitives.Shape;
import scad.Vector;
import shaders.Shader;

/**
 *
 * @author itaybachar
 */
public class Window2D {

    protected int width, height;
    protected String name;
    protected long windowRef;
    ArrayList<Vector> points;
    ArrayList<Vector> pointsOnQueue;
    ArrayList<Vector> insertPointsQueue;
    ArrayList<Integer> offsetPointsQueue;
    protected ArrayList<Integer> v_IDs, i_IDs, vao_IDs, nv_IDs; //IDs of vertex and indices.
    protected ArrayList<Boolean> selected;
    protected float tx, ty, tz;
    protected float scale;
    protected DoubleBuffer x;
    protected DoubleBuffer y;
    protected float xPos, yPos;
    protected FloatBuffer vertBuff;
    protected IntBuffer indicesBuff;
    protected boolean changed;
    protected int pID;
    protected int pointCount;
    int currentSelected = -1;
    Bezier bezier;
    boolean shiftOn = false;

    boolean isMoving = false;
    int pointMoveIndex = -1;

    public Window2D() {
        this("LWJGL3 Window");
    }

    public Window2D(String name) {
        this(500, 500, name);
    }

    public Window2D(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
        initVariables();
        initWindow();
    }

    private void initVariables() {
        points = new ArrayList<>();
        pointsOnQueue = new ArrayList<>();
        insertPointsQueue = new ArrayList<>();
        offsetPointsQueue = new ArrayList<>();
        v_IDs = new ArrayList<>();
        i_IDs = new ArrayList<>();
        vao_IDs = new ArrayList<>();
        selected = new ArrayList<>();
        this.x = BufferUtils.createDoubleBuffer(1);
        this.y = BufferUtils.createDoubleBuffer(1);
        tx = 0;
        ty = 0;
        tz = 0;
        scale = 0.01f;
        pointCount = 0;
    }

    private void initWindow() {
        //Makes sure window can work
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to Initialize GLFW!");
        }

        //Create window object and set its hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        //glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        this.windowRef = glfwCreateWindow(width, height, name, NULL, NULL);

        if (windowRef == 0) {
            throw new IllegalStateException("Failed to create Window!");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowRef, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        inputHandler();

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowRef);
        // Enable v-sync
        glfwSwapInterval(1);

        //Make GL capabilites for window
        GL.createCapabilities();

        glfwShowWindow(windowRef);
        changed = true;
    }

    public synchronized void setChanged(boolean state) {
        changed = state;
    }

    private void setMousePos() {
        xPos = ((2 * (float) x.get(0)) / width - 1f)/scale;
        yPos = (1f - (2 * (float) y.get(0)) / height)/scale;
    }

    private void inputHandler() {
        //Keyboard Listener
        glfwSetKeyCallback(windowRef, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                int keyEvent = key + action;//(mods << 10) + (action << 9) + key;

                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                }

                switch (scancode) {
                    case 114:
                        tx += 0.01 * Math.abs(tz);
                        setChanged(true);
                        break;
                    case 113:
                        tx -= 0.01 * Math.abs(tz);
                        setChanged(true);
                        break;
                    case 116:
                        ty -= 0.01 * Math.abs(tz);
                        setChanged(true);
                        break;
                    case 111:
                        ty += 0.01 * Math.abs(tz);
                        setChanged(true);
                        break;
                }
                shiftOn = (key == GLFW.GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS);
            }
        });

        //Mouse Listener
        glfwSetMouseButtonCallback(windowRef, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                int LEFT_MOUSE = 0, RIGHT_MOUSE = 1, MIDDLE_MOUSE = 2;

                if (button == RIGHT_MOUSE && action == GLFW_PRESS) {
                    glfwGetCursorPos(windowRef, x, y);
                    setMousePos();

                    for (int i = 0; i < pointCount; i++) {
                        float offset = 4.5f;
                        if ((points.get(i).x - offset) <= xPos && (points.get(i).x + offset) >= xPos
                                && (points.get(i).y - offset) <= yPos && (points.get(i).y + offset) >= yPos) {
                            isMoving = true;
                            pointMoveIndex = i;
                        }
                    }
                }

                if (button == RIGHT_MOUSE && action == GLFW_RELEASE) {
                    isMoving = false;
                }

                if (button == MIDDLE_MOUSE && action == GLFW_PRESS) {
                    bezier = null;
                    points.clear();
                    pointCount = 0;
                    setChanged(true);
                }

                if (button == LEFT_MOUSE && action == GLFW_RELEASE) {
                    glfwGetCursorPos(windowRef, x, y);
                    setMousePos();

                    if (shiftOn) {

                        int offset = bezier.findNearestSegment(0.01f, new Vector(xPos, yPos, 0));
                        insertPoint(offset+1, new Vector(xPos, yPos, 0));

                    } else {
                        addPoint(new Vector(xPos, yPos, 0));
                    }

                }
            }
        });

        glfwSetWindowSizeCallback(windowRef, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
            }

        });

        //Scroll Wheel Listener
        glfwSetScrollCallback(windowRef, new GLFWScrollCallback() {
            @Override
            public void invoke(long win, double dx, double dy) {

            }
        });

        glfwSetCursorPosCallback(windowRef, new GLFWCursorPosCallbackI() {

            @Override
            public void invoke(long window, double xpos, double ypos) {
                glfwGetCursorPos(windowRef, x, y);
                setMousePos();

                if (isMoving) {
                    points.get(pointMoveIndex).x = xPos;
                    points.get(pointMoveIndex).y = yPos;
                    updateBezier(pointMoveIndex);
                }

            }
        });
    }

    private void updateBezier(int index) {
        if (bezier != null) {
            bezier.setPoint(index, points.get(index));
            setChanged(true);
        }
    }

    private void updateWholeBezier() {
        for (int i = 0; i < points.size(); i++) {
            updateBezier(i);
        }
    }

    private void setBezier() {
        if (bezier == null) {
            if ((pointCount % 4) == 0) {
                bezier = new Bezier(new Vector((double) points.get(0).x, (double) points.get(0).y, 0),
                        new Vector((double) points.get(1).x, (double) points.get(1).y, 0),
                        new Vector((double) points.get(2).x, (double) points.get(2).y, 0),
                        new Vector((double) points.get(3).x, (double) points.get(3).y, 0));
                setChanged(true);

            }
        } else {
            if ((pointCount % 3) == 1) {
                bezier.add(points.get(points.size() - 3), points.get(points.size() - 2), points.get(points.size() - 1));
                setChanged(true);
            }
        }
    }

    private void addOnePoint(Vector2f p) {
        //Add to selection list
        selected.add(false);

        //Initialize the Buffers
        vertBuff = BufferUtils.createFloatBuffer(3);
        indicesBuff = BufferUtils.createIntBuffer(1);

        vertBuff.put(p.x);
        vertBuff.put(p.y);
        vertBuff.put(0);

        indicesBuff.put(0);

        vertBuff.flip();
        indicesBuff.flip();

        generateOneVAO();
        pointCount++;
    }

    private void generateOneVAO() {
        vao_IDs.add(glGenVertexArrays());
        glBindVertexArray(vao_IDs.get(pointCount));

        i_IDs.add(glGenBuffers());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_IDs.get(pointCount));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuff, GL_STATIC_DRAW);

        v_IDs.add(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, v_IDs.get(pointCount));
        glBufferData(GL_ARRAY_BUFFER, vertBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    private void addPoint(Vector p) {
        pointsOnQueue.add(p);
        pointCount++;
    }

    private void insertPoint(int offset, Vector p) {
        insertPointsQueue.add(new Vector(p.x - 2, p.y, p.z));
        offsetPointsQueue.add(offset);

        insertPointsQueue.add(p);

        insertPointsQueue.add(new Vector(p.x + 2, p.y, p.z));

        pointCount += 3;
    }

    private void checkUpdates() {
        //Check for new Points
        if (pointsOnQueue.size() > 0) {
            for (Vector p : pointsOnQueue) {
                points.add(p);
            }
            setBezier();
            pointsOnQueue.clear();
            currentSelected = points.size() - 1;
            selectShape(currentSelected);
            setChanged(true);
        }

        //Check for new Points
        if (insertPointsQueue.size() > 0) {
            for (int i = 0; i < insertPointsQueue.size(); i++) {
                points.add(offsetPointsQueue.get(0) + i, insertPointsQueue.get(i));
            }
            bezier.insert(offsetPointsQueue.get(0), points.get(offsetPointsQueue.get(0)), points.get(offsetPointsQueue.get(0) + 1), points.get(offsetPointsQueue.get(0) + 2));
            updateWholeBezier();
            insertPointsQueue.clear();
            offsetPointsQueue.clear();
            currentSelected = points.size() - 1;
            selectShape(currentSelected);
            setChanged(true);
        }

    }

    private void selectShape(int currentSelected) {
        for (int i = 0; i < selected.size(); i++) {
            selected.set(i, currentSelected == i);
        }
    }

    public synchronized void clearScene() {
        this.selected.clear();
        this.v_IDs.clear();
        this.i_IDs.clear();
        this.points.clear();
        this.pointsOnQueue.clear();
        this.pointCount = 0;
        setChanged(true);
    }

    protected synchronized void render() {
        Vector4f selectedColor = new Vector4f(1, 0, 0, 1f);
        Vector4f normalColor = new Vector4f(0.1f, 0.1f, 0.5f, 1f);

        glPointSize(8.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_LINE_SMOOTH);

        glLoadIdentity();
        glScalef(scale, scale, scale);
        glBegin(GL_POINTS);
        glColor3f(1, 1, 1);

        for (int i = 0; i < points.size(); i++) {
            glVertex2d(points.get(i).x, points.get(i).y);
        }
        glEnd();

        if (bezier != null) {

            double dt = 0.01;
            glBegin(GL_LINE_STRIP);
            glColor3f(1, 0, 0);
            Vector temp;
            /*          for (int i = 0; i < bezier.size()-1; i += 3) {
                for (double t = 0; t < 1; t += dt) {
                    temp = bezier.getPoint(i, t);
                    glVertex2d(temp.x, temp.y);
                }
            }
            glVertex2d(bezier.getLastX(), bezier.getLastY());
             */
            for (double t = 0; t <= 1; t += dt) {
                temp = bezier.getPoint(t);
                glVertex2d(temp.x, temp.y);
            }
            glVertex2d(bezier.getLastX(), bezier.getLastY());

            glEnd();

        }
    }

    public void renderWindow() {
        while (!glfwWindowShouldClose(windowRef)) {
            synchronized (this) {
                if (changed) {
                    glClear(GL_COLOR_BUFFER_BIT); // clear the framebuffer
                    render();
                    setChanged(false);
                    glfwSwapBuffers(windowRef);
                }
            }
            checkUpdates();
            glfwPollEvents();
        }

    }

    public long getWindowRef() {
        return this.windowRef;
    }

    public int getWidth() {
        return this.width;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static void main(String[] args) {
        Window2D w = new Window2D(1200, 1000, "2D View");
        w.renderWindow();
    }
}
