/******************************************
 *Project-------Marching-Squares
 *File----------Camera.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.simulation.display;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector2f projectionSize;

    public Camera(Vector2f projectionSize) {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        this.projectionSize = new Vector2f(projectionSize);
        setProjectionSize();
    }

    public void setProjectionSize() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x,
                            0.0f, projectionSize.y,
                             0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        // Camera is located at the origin , 20 units back
        Vector3f cameraPosition = new Vector3f(0, 0, 20.0f);
        // camera looks at the origin
        Vector3f cameraLookAt = new Vector3f(0, 0, 0);
        // camera up is in the positive y direction
        Vector3f cameraUp = new Vector3f(0, 1, 0);

        viewMatrix.identity();
        viewMatrix.lookAt(cameraPosition, cameraLookAt, cameraUp);

        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }
}
