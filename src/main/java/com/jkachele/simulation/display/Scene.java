/******************************************
 *Project-------Marching-Squares
 *File----------Scene.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.display;

import com.jkachele.simulation.render.ShaderParser;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.stb.STBImage.*;

public class Scene {

    private static int vaoID;
    private static int vboID;
    private static int eboID;
    private static int textureID;

    private static ShaderParser defaultShader;
    private static Camera camera;

    private static final Vector2f cameraSize = new Vector2f(1920, 1080);

    private static final float[] vertexArray = {
            500f,   500f,  0.0f,           0.0f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     // 0: Bottom Left
            500f,   1000f, 0.0f,           0.0f, 1.0f,     0.0f, 1.0f, 0.0f, 1.0f,     // 1: Top Left
            1000f,  1000f, 0.0f,           1.0f, 1.0f,     0.0f, 0.0f, 1.0f, 1.0f,     // 2: Top Right
            1000f,  500f,  0.0f,           1.0f, 0.0f,     1.0f, 1.0f, 1.0f, 1.0f      // 3: Bottom Right
    };

    private static final int[] elementArray = {
            0, 2, 1,
            0, 3, 2
    };

    public static void init() {
        defaultShader = new ShaderParser("assets/shaders/default.glsl");
        defaultShader.compile();

        camera = new Camera(cameraSize);

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int uvSize = 2;
        int colorSize = 4;
        int vertexSizeBytes = (positionsSize + uvSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, uvSize, GL_FLOAT, false, vertexSizeBytes,
                (positionsSize) * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, colorSize, GL_FLOAT, false, vertexSizeBytes,
                (positionsSize + uvSize) * Float.BYTES);
        glEnableVertexAttribArray(2);

        glLineWidth(2.0f);
    }

    public static void update(float dt) {
        defaultShader.use();

        defaultShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());
        defaultShader.uploadMat4f("uProjectionMatrix", camera.getProjectionMatrix());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
//        glDrawArrays(GL_LINE, 0, 4 * 9);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);

        defaultShader.detach();
    }

    private static void texture(String filePath) {
        // Generate the texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Set the texture parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);   // Wrap in x direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);   // Wrap in y direction

        // When stretching and shrinking the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);  // Stretching
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);  // Shrinking

        // Load the image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        float[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        // Upload the image to the GPU
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, 10, 1, 0, GL_RED, GL_FLOAT, data);
        if (image != null) {

            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (Texture) Unknown number of color channels: " + channels.get(0);
            }
        } else {
            assert false : "Error: (Texture) Could not load image from file: " + filePath;
        }

        // Clean up to prevent memory leaks
        stbi_image_free(image);
    }
}
