/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editorV2;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author itay
 */
public class Camera {

    Vector3f pos;
    Vector3f offset;

    Vector3f front;
    Vector3f up;
    Vector3f target;

    Matrix4f perspective;
    Matrix4f view;

    float moveSpeed = 1f;
    float zoomSpeed = 1.5f;
    float zoomSpeedMultiplier = 1f;
    float pitch = 0;
    float yaw = 0;
    float distanceFromTarget = 10;

    public Camera(Vector3f pos, float fov, float aspect, float nearZ, float farZ) {
        this.pos = pos;
        this.front = new Vector3f(0, 0, 1);
        this.up = new Vector3f(0, 1, 0);
        this.target = new Vector3f(0, 0, 0);
        this.offset = new Vector3f(0, 0, 0);
        this.perspective = new Matrix4f().perspective(fov, aspect, nearZ, farZ);
    }
    
    public void setZoomMultiplier(float amnt){
        this.zoomSpeedMultiplier = amnt;
    }

    public Matrix4f getView() {
        return this.view;
    }

    public Matrix4f getPerspective() {
        return new Matrix4f(this.perspective);
    }

    public Vector3f getPos() {
        return new Vector3f(this.pos).add(this.front);
    }

    public Matrix4f getViewProjection() {
        calculatePos();
        view = new Matrix4f().lookAt(pos, new Vector3f(pos).add(front), up);
        return new Matrix4f(perspective).mul(view);
    }

    public void moveUp(float amnt) {
        this.offset.add(new Vector3f(up).mul(amnt * moveSpeed));
    }

    public void moveRight(float amnt) {
        Vector3f right = new Vector3f(up).cross(front).normalize();
        this.offset.add(right.mul(amnt * moveSpeed));
    }

    private void calculatePos() {
        float horizontalDist = (float) (distanceFromTarget * Math.cos(Math.toRadians(pitch)));
        float verticleDist = (float) (distanceFromTarget * Math.sin(Math.toRadians(pitch)));

        float offsetX = (float) (horizontalDist * Math.sin(Math.toRadians(yaw)));
        float offsetZ = (float) (horizontalDist * Math.cos(Math.toRadians(yaw)));
        pos.x = offset.x - offsetX;
        pos.z = target.z - offsetZ;
        pos.y = offset.y + verticleDist;

        front = new Vector3f(target).sub(pos).normalize();
    }

    public void zoom(float amnt) {
        distanceFromTarget += amnt * zoomSpeed*zoomSpeedMultiplier;
    }

    public void changePitch(float amnt) {
        float angleChange = amnt * 5;
        //pitch -= angleChange;
        pitch = clipValue(-90, 90, pitch - angleChange);
    }

    public void setTarget(Vector3f target) {
        this.target = target;
    }

    public void changeYaw(float amnt) {
        float angleChange = amnt * 5;
        yaw += angleChange;

    }

    private float clipValue(float min, float max, float val) {
        if (val > max) {
            return max;
        } else if (val < min) {
            return min;
        }

        return val;
    }

}
