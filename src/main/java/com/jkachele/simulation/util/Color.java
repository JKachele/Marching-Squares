/******************************************
 *Project-------Marching-Squares
 *File----------Color.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.util;

import org.joml.Vector4f;

public class Color {

    public static final Color WHITE         = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color LIGHT_GRAY    = new Color(0.75f, 0.75f, 0.75f, 1.0f);
    public static final Color GRAY          = new Color(0.5f, 0.5f, 0.5f, 1.0f);
    public static final Color DARK_GRAY     = new Color(0.25f, 0.25f, 0.25f, 1.0f);
    public static final Color BLACK         = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED           = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN         = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE          = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color YELLOW        = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color CYAN          = new Color(0.0f, 1.0f, 1.0f, 1.0f);
    public static final Color MAGENTA       = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    public static final Color ORANGE        = new Color(1.0f, 0.8f, 0.0f, 1.0f);
    public static final Color PINK          = new Color(1.0f, 0.7f, 0.7f, 1.0f);

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1.0f;
    }

    public Color(float alpha) {
        this.red = alpha;
        this.green = alpha;
        this.blue = alpha;
        this.alpha = 1.0f;
    }

    public Color darker() {
        return new Color(Math.max(red - 0.01f, 0.0f), Math.max(green - 0.01f, 0.0f),
                Math.max(blue - 0.01f, 0.0f), alpha);
    }

    public Color darker(float d) {
        return new Color(Math.max(red - d, 0.0f), Math.max(green - d, 0.0f), Math.max(blue - d, 0.0f), alpha);
    }

    public Color lighter() {
        return new Color(Math.min(red + 0.01f, 1.0f), Math.min(green + 0.01f, 1.0f),
                Math.min(blue + 0.01f, 1.0f), alpha);
    }

    public Color lighter(float d) {
        return new Color(Math.min(red + d, 1.0f), Math.min(green + d, 1.0f), Math.min(blue + d, 1.0f), alpha);
    }

    public Color fadeToColor(Color target) {
        float dRed      = (target.getRed() - red) / 100;
        float dGreen    = (target.getGreen() - green) / 100;
        float dBlue     = (target.getBlue() - blue) / 100;

        return new Color(red + dRed, green + dGreen, blue + dBlue, alpha);
    }

    public Color fadeToColor(Color target, float d) {
        float dRed      = (target.getRed() - red) / (100 * d);
        float dGreen    = (target.getGreen() - green) / (100 * d);
        float dBlue     = (target.getBlue() - blue) / (100 * d);

        return new Color(red + dRed, green + dGreen, blue + dBlue, alpha);
    }

    public float[] getComponents() {
        return new float[]{red, green, blue, alpha};
    }

    public Vector4f toVector() {
        return new Vector4f(red, green, blue, alpha);
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }
}
