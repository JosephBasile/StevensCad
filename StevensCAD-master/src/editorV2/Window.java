/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import static com.jogamp.newt.event.MouseEvent.PointerType.Mouse;
import glm.vec._3.Vec3;
import java.awt.Point;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
import shaders.Shader;
import java.nio.IntBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import static org.lwjgl.opengl.EXTGeometryShader4.GL_LINES_ADJACENCY_EXT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.NULL;
import primitives.Cube;
import primitives.Cylinder;
import primitives.Shape;
import primitives.Sphere;
import scad.Triangle;
import scad.Vector;

/**
 *
 * @author itay
 */
public class Window {

    protected static int WIDTH, HEIGHT;
    protected String name;
    protected long windowRef;
    protected ArrayList<Shape> shapes;
    protected ArrayList<Shape> shapesOnQueue;
    protected ArrayList<Shape> updateOnQueue;
    ArrayList<Vector3f> points;
    ArrayList<Vector3f> pointsOnQueue;
    protected ArrayList<Integer> v_IDs, i_IDs, vao_IDs, nv_IDs; //IDs of vertex and indices.
    protected ArrayList<Integer> indicesCount;
    protected ArrayList<Boolean> selected;
    protected ModeBase mode;
    protected int frameCount;
    long before;
    protected float tx, ty, tz;
    protected float scale;
    protected float startClickX, startClickY;
    protected float deltaX, deltaY;
    protected boolean isDrag;
    protected Camera camera;
    protected int objectCount;
    protected Shader shader;
    protected Vector3f rot;
    protected float startRotX, startRotY;
    protected DoubleBuffer x;
    protected DoubleBuffer y;
    protected FloatBuffer vertBuff, normalBuff;
    protected IntBuffer indicesBuff;
    protected boolean changed;
    protected InputEventMap inputEventMap;
    protected int pID;
    int currentSelected = -1;

    RayPicker rayPicker;
    //To Delete just for now to toggle Modes
    protected boolean shiftOn = false;

    protected final void initVariables() {
        this.frameCount = 0;
        this.before = System.nanoTime();
        this.deltaX = 0;
        this.deltaY = 0;
        this.camera = new Camera(new Vector3f(0, 0, -10), 70, (float) WIDTH / (float) HEIGHT, 0.1f, 1000f);
        this.x = BufferUtils.createDoubleBuffer(1);
        this.y = BufferUtils.createDoubleBuffer(1);
        this.rot = new Vector3f(0, 0, 0);
        this.isDrag = false;
        this.shader = new Shader();
        this.selected = new ArrayList();
        this.indicesCount = new ArrayList();
        this.v_IDs = new ArrayList();
        this.i_IDs = new ArrayList();
        this.vao_IDs = new ArrayList<>();
        this.nv_IDs = new ArrayList<>();
        this.shapes = new ArrayList();
        this.shapesOnQueue = new ArrayList();
        this.points = new ArrayList();
        this.pointsOnQueue = new ArrayList();
        this.tx = 0;
        this.ty = 0;
        this.tz = 1;
        this.objectCount = 0;
        this.mode = new InsertSphereMode(this);
        //String language = conf.defaulted("language");
        //String inputMapPath = conf.defaulted("3dinputmap." + language);
        //inputEventMap = new InputEventMap("conf/" + inputMapPath);
        this.changed = true;
        this.updateOnQueue = new ArrayList();
        rayPicker = new RayPicker(camera);
    }

    public synchronized void setChanged(boolean state) {
        changed = state;
    }

    public Window() {
        this("LWJGL3 Window");
    }

    public Window(String name) {
        this(500, 500, name);
    }

    public Window(int width, int height, String name) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.name = name;
        initVariables();
        initWindow();
        
    }

    public void renderWindow() {
        synchronized (this) {
            if (changed) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                shader.bind(pID);
                shader.bindAttributes(pID, 0, "position");
                shader.bindAttributes(pID, 1, "normal");
                shader.setUniform(pID, "viewProjectionMatrix", camera.getViewProjection());
                render(pID);
                glfwSwapBuffers(windowRef);
                setChanged(false);
            }
        }

        checkUpdates();
        glfwPollEvents();
    }

    public synchronized Shape getCurrent() {
        return this.shapes.get(currentSelected);
    }

    private void initWindow() {
        //Makes sure window can work
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to Initialize GLFW!");
        }

        //Create window object and set its hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
       // glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        this.windowRef = glfwCreateWindow(WIDTH, HEIGHT, name, NULL, NULL);

        if (windowRef == 0) {
            throw new IllegalStateException("Failed to create Window!");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowRef, (videoMode.width() - WIDTH) / 2, (videoMode.height() - HEIGHT) / 2);
        inputHandler();

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowRef);
        // Enable v-sync
        glfwSwapInterval(1);

        //Make GL capabilites for window
        GL.createCapabilities();

        glfwShowWindow(windowRef);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LIGHTING);

        scale = 0.05f;
        pID = shader.loadShaders("shader", "shader");
    }

    protected void inputHandler() {
        //Keyboard key ref:
        //http://www.glfw.org/docs/latest/group__keys.html
        
        //Keyboard Listener
        glfwSetKeyCallback(windowRef, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                
                //System.out.println("Key: " + key + " Mod: "+action);
                
                if(action>0)
                    action = 1;
                int code = (key*10 + mods)*10 + action;
                
                System.out.println("Key Code : "+ code);
                
                
                int keyEvent = key + action;//(mods << 10) + (action << 9) + key;
                // System.out.println(keyEvent);
                //inputEventMap.exec(keyEvent);
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                }

                switch (scancode) {
                    case 114:
                        tx -= 0.01 * Math.abs(tz);
                        camera.moveRight(-1);
                        setChanged(true);
                        break;
                    case 113:
                        tx += 0.01 * Math.abs(tz);
                        camera.moveRight(1);
                        setChanged(true);
                        break;
                    case 116:
                        ty -= 0.01 * Math.abs(tz);
                        camera.moveUp(-1f);
                        setChanged(true);
                        break;
                    case 111:
                        ty += 0.01 * Math.abs(tz);
                        camera.moveUp(1f);
                        setChanged(true);
                        break;
                }
                //Shift Detection
                shiftOn = (key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS);
            }
        });

        //Mouse Listener
        glfwSetMouseButtonCallback(windowRef, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                //Button may be from 0 to 7, with respective buttons so up to mouse button 8 from 1   
                
                 if(action>0)
                    action = 1;
                int code = (button*10 + mods)*10 + action;
                
                System.out.println("Mouse Code : "+ code);
                
                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {

                    glfwGetCursorPos(windowRef, x, y);

                    startClickX = (float) x.get(0);
                    startClickY = (float) y.get(0);
                    tempx = startClickX;
                    tempy = startClickY;
                    startRotX = rot.x;
                    startRotY = rot.y;
                    isDrag = true;
                    if (shiftOn) {
                        mode.mousePressed((int) startClickX, (int) startClickY);
                    }
                }
                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                    glfwGetCursorPos(windowRef, x, y);

                    deltaX = (float) x.get(0) - startClickX;
                    deltaY = (float) y.get(0) - startClickY;
                    isDrag = false;
                }

                if (button == 2 && action == GLFW_RELEASE) {
                    rayPicker.calculateRay((float) x.get(0), (float) y.get(0));
                    selectShape();
                    setChanged(true);
                }
            }

        });

        //Scroll Wheel Listener
        glfwSetScrollCallback(windowRef, new GLFWScrollCallback() {
            @Override
            public void invoke(long win, double dx, double dy
            ) {
                if (shiftOn) {
                    camera.setZoomMultiplier(2);
                } else {
                    camera.setZoomMultiplier(1);
                }

                if (dy > 0) {
                    tz += 1.05;
                    camera.zoom(-1);
                } else {
                    tz -= 1.05;
                    camera.zoom(1);
                }
                setChanged(true);
            }
        });

        glfwSetCursorPosCallback(windowRef, new GLFWCursorPosCallbackI() {

            @Override
            public void invoke(long window, double xpos, double ypos) {
                glfwGetCursorPos(windowRef, x, y);
                if (isDrag) {
                    if (shiftOn) {
                        mode.mouseDragged((int) x.get(0), (int) y.get(0));
                    } else {
                        if ((float) (tempy - y.get(0)) < 0) {
                            camera.changePitch(-1);
                        } else if ((float) (tempy - y.get(0)) > 0) {
                            camera.changePitch(1);
                        }

                        if ((float) (tempx - x.get(0)) < 0) {
                            camera.changeYaw(-1);
                        } else if ((float) (tempx - x.get(0)) > 0) {
                            camera.changeYaw(1);
                        }

                        tempy = (float) y.get(0);
                        tempx = (float) x.get(0);

                        rot.y = (startRotY + (float) ((x.get(0) - startClickX) / (WIDTH) * Math.PI * 2)) % ((float) Math.PI * 2);
                        rot.x = (startRotX + (float) ((y.get(0) - startClickY) / (HEIGHT) * -Math.PI * 2)) % ((float) Math.PI * 2);
                        setChanged(true);
                    }
                }
            }
        });
    }

    float tempx;
    float tempy;

    protected synchronized void checkUpdates() {
        //Check for new Shapes
        if (shapesOnQueue.size() > 0) {
            for (Shape s : shapesOnQueue) {
                shapes.add(s);
                addOneShape(s);
            }
            shapesOnQueue.clear();
            currentSelected = shapes.size() - 1;
            selectShape(currentSelected);
        }
//Check for new Points
        if (pointsOnQueue.size() > 0) {
            for (Vector3f p : pointsOnQueue) {
                points.add(p);
                addOnePoint(p);
            }
            shapesOnQueue.clear();
            currentSelected = shapes.size() - 1;
            selectShape(currentSelected);
        }
        //Check for Updates
        if (updateOnQueue.size() > 0) {
            for (Shape s : updateOnQueue) {
                updateOneShape(s);
            }
            updateOnQueue.clear();
        }
    }

    public void addOneShape(Shape s) {
        this.objectCount++;
        //Add to selection list
        selected.add(false);

        //Reset index Count
        indicesCount.add(s.triangles().size() * 3);
        int vertexCount = s.getVertices().size() * 3;

        //Set Hash Map
        s.setHashMap();

        //Initialize the Buffers
        vertBuff = BufferUtils.createFloatBuffer(vertexCount);
        normalBuff = BufferUtils.createFloatBuffer(vertexCount);
        indicesBuff = BufferUtils.createIntBuffer(indicesCount.get(this.objectCount - 1));

        //Place all the indices into the ByteBuffer
        ArrayList<Triangle> tempTriangles = s.triangles();
        
        
        for (Triangle t : tempTriangles) {
            for (int i = 0; i < 3; i++) {
                indicesBuff.put((s.getHashMap().get(t.vertex.get(i))));
            }
        }

        //Place all the vertices in the FloatBuffer
        for (Vector v : s.getVertices()) {
            vertBuff.put((float) v.x);
            vertBuff.put((float) v.y);
            vertBuff.put((float) v.z);
            normalBuff.put((float) v.normalized().x);
            normalBuff.put((float) v.normalized().y);
            normalBuff.put((float) v.normalized().z);
        }

        //Close the Buffer for input
        vertBuff.flip();
        indicesBuff.flip();
        normalBuff.flip();

        generateOneVBO();
        setChanged(true);
    }

    protected void generateOneVBO() {

        vao_IDs.add(glGenVertexArrays());
        glBindVertexArray(vao_IDs.get(this.objectCount - 1));
        i_IDs.add(glGenBuffers());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_IDs.get(this.objectCount - 1));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuff, GL_STATIC_DRAW);
        // glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        //Generate Buffers and get IDs
        v_IDs.add(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, v_IDs.get(this.objectCount - 1));
        glBufferData(GL_ARRAY_BUFFER, vertBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        nv_IDs.add(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, nv_IDs.get(this.objectCount - 1));
        glBufferData(GL_ARRAY_BUFFER, normalBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    protected void updateOneVBO(int index) {

        //Generate Buffers and get IDs
        glBindBuffer(GL_ARRAY_BUFFER, v_IDs.get(index));
        glBufferData(GL_ARRAY_BUFFER, vertBuff, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_IDs.get(index));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuff, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, nv_IDs.get(index));
        glBufferData(GL_ARRAY_BUFFER, normalBuff, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    //TODO: this would be more efficient, but it's a lot of work!
    // wait until the end of each
    protected void updateOneShape(Shape s) {
        int index = shapes.indexOf(s);
        //Reset index Count
        indicesCount.set(index, s.triangles().size() * 3);
        int vertexCount = s.getVertices().size() * 3;

        //Set Hash Map
        s.setHashMap();

        //Initialize the Buffers
        vertBuff = BufferUtils.createFloatBuffer(vertexCount);
        normalBuff = BufferUtils.createFloatBuffer(vertexCount);
        indicesBuff = BufferUtils.createIntBuffer(indicesCount.get(index));

        //Place all the indices into the ByteBuffer
        ArrayList<Triangle> tempTriangles = s.triangles();

        for (Triangle t : tempTriangles) {

            for (int i = 0; i < 3; i++) {
                indicesBuff.put((s.getHashMap().get(t.vertex.get(i))));

            }
        }

        //Place all the vertices in the FloatBuffer
        for (Vector v : s.getVertices()) {
            vertBuff.put((float) v.x);
            vertBuff.put((float) v.y);
            vertBuff.put((float) v.z);
            normalBuff.put((float) v.normalized().x);
            normalBuff.put((float) v.normalized().y);
            normalBuff.put((float) v.normalized().z);
        }

        //Close the Buffer for input
        vertBuff.flip();
        indicesBuff.flip();
        normalBuff.flip();

        updateOneVBO(index);
        setChanged(true);
    }

    protected synchronized void render(int p_Id) {
        Vector4f selectedColor = new Vector4f(1, 0, 0, 1f);
        Vector4f normalColor = new Vector4f(0.5f, 0.5f, 0.5f, 1f);

        //<editor-fold desc="Ray Drawing">
        IntBuffer rayiBuf = BufferUtils.createIntBuffer(2);
        FloatBuffer rayvBuf = BufferUtils.createFloatBuffer(6);
        rayiBuf.put(0).put(1);

        //ray length
        int length = 1000;
        Vector3f origin = rayPicker.getOrigin();
        Vector3f end = rayPicker.getEnd();
        rayvBuf.put(origin.x);
        rayvBuf.put(origin.y);
        rayvBuf.put(origin.z);
        //End of ray
        rayvBuf.put(end.x);
        rayvBuf.put(end.y);
        rayvBuf.put(end.z);
        //Origin of ray
        // origin.rotateX(camera.getXRot()).rotateY(camera.getYRot());

        rayiBuf.flip();
        rayvBuf.flip();

        int vaoid = glGenVertexArrays();
        glBindVertexArray(vaoid);
        int iid = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iid);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, rayiBuf, GL_STATIC_DRAW);

        int vid = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vid);
        glBufferData(GL_ARRAY_BUFFER, rayvBuf, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        shader.setUniform(p_Id, "shapeColor", new Vector4f(0f, 1f, 0f, 1f));
        glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        //</editor-fold>
        for (int index = 0; index < v_IDs.size(); index++) {

            glBindVertexArray(vao_IDs.get(index));
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            //  glPointSize(3.0f);
            // glEnable(GL_POINT_SMOOTH);
            //Draw triangles
            if (selected.get(index)) {
                shader.setUniform(p_Id, "shapeColor", selectedColor);
            } else {
                shader.setUniform(p_Id, "shapeColor", normalColor);
            }

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            for (int i = 0; i < indicesCount.get(index); i += 3) {
                glDrawElements(GL_POLYGON, 3, GL_UNSIGNED_INT, i * 4);
            }

            //Draw Wireframe
            glLineWidth(1.5f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            shader.setUniform(p_Id, "shapeColor", new Vector4f(1, 1, 1, 0.75f));
            for (int i = 0; i < indicesCount.get(index); i += 3) {
                glDrawElements(GL_POLYGON, 3, GL_UNSIGNED_INT, i * 4);
            }

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

        }
        frameCount++;
        if (frameCount % 20 == 0) {
            long now = System.nanoTime();
            long elapsed = now - before;
            //System.out.println("Frame rate = " + 20 * 1.0e9 / elapsed);
            before = now;
        }
    }

    public synchronized void addShape(Shape s) {
        shapesOnQueue.add(s);
    }

    public synchronized void addPoint(Vector3f point) {
        pointsOnQueue.add(point);
    }

    public synchronized void updateShape(Shape s) {
        updateOnQueue.add(s);
    }

    public void removeShape(Shape s) {
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i) == s) {
                shapes.remove(i);
                break;
            }
        }
    }

    public void selectShape(int index) {
        for (int i = 0; i < selected.size(); i++) {
            selected.set(i, index == i);
        }
    }

    public ArrayList<Shape> getShapes() {
        return this.shapes;
    }

    //Clears Screen
    public synchronized void clearScene() {
        this.selected.clear();
        this.indicesCount.clear();
        this.v_IDs.clear();
        this.i_IDs.clear();
        this.shapes.clear();
        this.shapesOnQueue.clear();
        this.objectCount = 0;
        setChanged(true);
    }

    public long getWindowRef() {
        return this.windowRef;
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public void setDimensions(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    private void addOnePoint(Vector3f p) {
        this.objectCount++;
        //Add to selection list
        selected.add(false);

        //Initialize the Buffers
        vertBuff = BufferUtils.createFloatBuffer(3);
        indicesBuff = BufferUtils.createIntBuffer(3);

        indicesBuff.put(0);

        //Place all the vertices in the FloatBuffer
        vertBuff.put((float) p.x);
        vertBuff.put((float) p.y);
        vertBuff.put((float) p.z);

        //Close the Buffer for input
        vertBuff.flip();
        indicesBuff.flip();

        generateOneVBO();
        setChanged(true);
    }

    public void selectShape() {
        HashMap<Vector3f,Integer > indexToVertex = new HashMap<>();

        for (int i = 0; i < shapes.size(); i++) {
            Shape currentShape = shapes.get(i);

            if (currentShape instanceof Sphere) {
                ArrayList<Vector3f> res = rayPicker.sphereIntersection2((Sphere) currentShape);
                if (res != null) {
                    indexToVertex.put(res.get(0),i);
                    indexToVertex.put(res.get(1),i);
                }

            } else if (currentShape instanceof Cube) {
                Vector3f res;
                for (Triangle t : currentShape.triangles()) {
                    res = rayPicker.rayTriangleIntersect(t);
                    if (res != null) {
                        indexToVertex.put(res,i);
                    }
                }
            } else if (currentShape instanceof Cylinder) {
                Vector3f res;
                for (Triangle t : currentShape.triangles()) {
                    res = rayPicker.rayTriangleIntersect(t);
                    if (res != null) {
                        indexToVertex.put(res,i);
                    }
                }
            }
        }
        
        float bestDist = 100000000;
        int bestIndex = -1;
        
        for(int i = 0; i<indexToVertex.size(); i++){
            Vector3f currentVec = (Vector3f)indexToVertex.keySet().toArray()[i];
            float currDist = currentVec.distance(rayPicker.getOrigin());
            
            if(currDist<bestDist){
                bestDist = currDist;
                bestIndex = indexToVertex.get(currentVec);
            }
        }
        currentSelected = bestIndex;
        selectShape(currentSelected);
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
