/******************************************
 *Project-------Marching-Squares
 *File----------Scene.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.display;

import com.jkachele.simulation.marching.ImplicitFunction2D;
import com.jkachele.simulation.marching.MarchingSquares;
import com.jkachele.simulation.render.Renderer;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Scene {
    private static Camera camera;
    private static final Vector2f cameraSize = new Vector2f(16 , 9);
    private static ImplicitFunction2D circle;
    private static ImplicitFunction2D heart;
    private static ImplicitFunction2D cool;

    public static void init() {
        camera = new Camera(cameraSize);

        circle = (x, y) -> square(x - (cameraSize.x / 2)) + square(y - (cameraSize.y / 2)) -
                square((cameraSize.y / 4));
        heart = (x, y) -> cube(square((x - 8)/3) + square((y - 4.5f)/3) - 1) -
                (square((x - 8)/3) * cube((y - 4.5f)/3));
        cool = (x, y) -> (float)(Math.sin(square(x - 8) + square(y - 4.5f)) - Math.cos((x - 8) * (y - 4.5f)));
    }

    private static float square(float x) {
        return x * x;
    }

    private static float cube(float x) {
        return x * x * x;
    }

    public static void update(float dt) {
        MarchingSquares.init(new Vector2i(10000, 10000), heart);

//        System.out.print("\rFPS: " + Engine.fps(dt) + "\tPoints: " + Renderer.numPoints());
    }

    public static Camera getCamera() {
        return camera;
    }
}
