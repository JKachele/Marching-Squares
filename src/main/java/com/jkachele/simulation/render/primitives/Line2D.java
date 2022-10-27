/******************************************
 *Project-------Marching-Squares
 *File----------Line2D.java
 *Author--------Justin Kachele
 *Date----------10/27/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.render.primitives;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Line2D {
    private Vector2f start;
    private Vector2f end;
    private Vector4f color;
    private int lifetime;

    private boolean constant = false;

    public Line2D(Vector2f start, Vector2f end, Vector4f color, int lifetime) {
        this.start = start;
        this.end = end;
        this.color = color;
        if (lifetime == -1) {
            constant = true;
        } else {
            this.lifetime = lifetime;
        }
    }

    public Line2D(Vector2f start, Vector2f end) {
        this.start = start;
        this.end = end;
    }

    public int beginFrame() {
        if (constant) {
            return 1;
        }
        this.lifetime--;
        return lifetime;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector4f getColor() {
        return color;
    }

    public float lengthSquared() {
        return new Vector2f(end).sub(start).lengthSquared();
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }
}
