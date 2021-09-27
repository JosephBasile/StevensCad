/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import primitives.Sphere;
import scad.Triangle;

/**
 *
 * @author itay
 */
public class RayPicker {

    private Camera camera;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector3f currentRay;
    private Vector3f origin;
    private Vector3f end;
    private float length = 1000;

    public RayPicker(Camera cam) {
        this.camera = cam;
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.origin = new Vector3f(0, 0, 0);
        this.end = new Vector3f(0, 0, 0);
    }

    public void calculateRay(float mouseX, float mouseY) {
        this.viewMatrix = camera.getViewProjection();
        this.projectionMatrix = camera.getPerspective();

        //Get clip Coords
        Vector4f clipCoords = new Vector4f((2 * mouseX) / Window.WIDTH - 1f, 1f - (2 * mouseY) / Window.HEIGHT, 1, 1);

        //Unproject the point into the world
        this.viewMatrix.invert();
        Vector4f rayInWorld = new Vector4f(viewMatrix.transform(clipCoords));
        currentRay = new Vector3f(rayInWorld.x, rayInWorld.y, rayInWorld.z).normalize();

        //Calculate the origin and end of ray to draw
        this.origin = camera.getPos();
        this.end = new Vector3f(currentRay).mul(length);
    }

    public Vector3f getCurrentRay() {
        return this.currentRay;
    }

    public Vector3f getOrigin() {
        return this.origin;
    }

    public Vector3f getEnd() {
        return this.end;
    }

    //Intersection Code
    public ArrayList<Vector3f> sphereIntersection2(Sphere s) {
        Vector3f sOrigin = new Vector3f((float) s.t.getMatrix()[3], (float) s.t.getMatrix()[7], (float) s.t.getMatrix()[11]);
        float t = new Vector3f(sOrigin).sub(origin).dot(currentRay);

        Vector3f p = new Vector3f(origin).add(new Vector3f(currentRay).mul(t));

        double y = new Vector3f(sOrigin).sub(p).length();

        if (y < s.getRadius()) {
            double x = Math.sqrt((s.getRadius() * s.getRadius()) - (y * y));

            Vector3f p1 = new Vector3f(origin).add(new Vector3f(currentRay).mul((float)(t-x)));
            Vector3f p2 = new Vector3f(origin).add(new Vector3f(currentRay).mul((float)(t+x)));
            System.out.println(p1);
            System.out.println(p2);
            ArrayList<Vector3f> sol = new ArrayList<>();
            sol.add(p1);
            sol.add(p2);
            return sol;
        }
        return null;

    }

    public Vector3f rayTriangleIntersect(Triangle tri) {
        float EPSILON = 0.0000001f;
        Vector3f v0 = new Vector3f((float) tri.vertex.get(0).x, (float) tri.vertex.get(0).y, (float) tri.vertex.get(0).z);
        Vector3f v1 = new Vector3f((float) tri.vertex.get(1).x, (float) tri.vertex.get(1).y, (float) tri.vertex.get(1).z);
        Vector3f v2 = new Vector3f((float) tri.vertex.get(2).x, (float) tri.vertex.get(2).y, (float) tri.vertex.get(2).z);

        Vector3f edge1 = new Vector3f(v1).sub(v0);
        Vector3f edge2 = new Vector3f(v2).sub(v0);

        Vector3f h = new Vector3f(currentRay).cross(edge2);
        float a = new Vector3f(edge1).dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return null;
        }
        float f = 1 / a;
        Vector3f s = new Vector3f(origin).sub(v0);
        float u = f * (s.dot(h));
        if (u < 0.0 || u > 1.0) {
            return null;
        }

        Vector3f q = s.cross(edge1);
        float v = f * (new Vector3f(currentRay).dot(q));

        if (v < 0.0 || u + v > 1.0) {
            return null;
        }

        float t = f * edge2.dot(q);
        if (t > EPSILON) {
            return new Vector3f(origin).add(new Vector3f(currentRay).mul(t));
        } else {
            return null;
        }
    }
}
