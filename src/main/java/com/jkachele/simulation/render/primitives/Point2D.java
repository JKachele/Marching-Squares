/******************************************
 *Project-------Marching-Squares
 *File----------Point2D.java
 *Author--------Justin Kachele
 *Date----------10/27/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.render.primitives;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Point2D {
    private Vector2f position;
    private Vector4f color;
    private int lifetime;

    private boolean constant = false;

    public Point2D(Vector2f position, Vector4f color, int lifetime) {
        this.position = position;
        this.color = color;
        if (lifetime == -1) {
            constant = true;
        } else {
            this.lifetime = lifetime;
        }
    }

    public Point2D(Vector2f position) {
        this.position = position;
    }

    public int beginFrame() {
        if (constant) {
            return 1;
        }
        this.lifetime--;
        return lifetime;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector4f getColor() {
        return color;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }
}
