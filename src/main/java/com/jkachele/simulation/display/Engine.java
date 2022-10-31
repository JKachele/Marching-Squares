/******************************************
 *Project-------Marching-Squares
 *File----------Engine.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.display;

import com.jkachele.simulation.render.Renderer;
import com.jkachele.simulation.util.Color;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine implements Runnable{
    private Color backgroundColor;

    private final Thread GAME_LOOP_THREAD;

    public Engine(int width, int height, String title, Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        GAME_LOOP_THREAD = new Thread(this, "GAME_LOOP_THREAD");
        Window.init(width, height, title, backgroundColor);
    }

    public void start() {
        GAME_LOOP_THREAD.start();
    }

    @Override
    public void run() {
        try {
            Window.start();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void gameLoop() {
        Scene.init();

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        // Set the clear color
        glClearColor(backgroundColor.getRed(), backgroundColor.getGreen(),
                backgroundColor.getBlue(), backgroundColor.getAlpha());

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(Window.glfwWindow) ) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            Renderer.beginFrame();

            if (dt >= 0) {
                Renderer.draw();
                Scene.update(dt);
            }

            glfwSwapBuffers(Window.glfwWindow); // swap the color buffers

//            System.out.print("\r" + fps(dt));

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static String fps(float dt) {
        return String.format("FPS: %.2f", 1.0f / dt);
    }
}
