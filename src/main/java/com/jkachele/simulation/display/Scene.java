/******************************************
 *Project-------Marching-Squares
 *File----------Scene.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.display;

import com.jkachele.simulation.render.Renderer;
import com.jkachele.simulation.util.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Scene {

    private static int textureID;

    private static Camera camera;

    private static final Vector2f cameraSize = new Vector2f(1920, 1080);

    public static void init() {
        camera = new Camera(cameraSize);

        Renderer.addLine2D(new Vector2f(0, 0), new Vector2f(1920, 1080), Color.WHITE.toVector(), -1);
        Renderer.addLine2D(new Vector2f(0, 1080), new Vector2f(1920, 0), Color.WHITE.toVector(), -1);


        Vector2i gridSize = new Vector2i(50, 50);
        for (int i = 0; i < cameraSize.x; i += cameraSize.x / gridSize.x) {
            for (int j = 0; j < cameraSize.y; j += cameraSize.y / gridSize.y) {
                Renderer.addPoint2D(new Vector2f(i, j), Color.MAGENTA.toVector(), -1);
            }
        }
    }

    public static void update(float dt) {
    }

    public static Camera getCamera() {
        return camera;
    }
}
