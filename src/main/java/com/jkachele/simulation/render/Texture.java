/******************************************
 *Project-------Marching-Squares
 *File----------Texture.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.simulation.render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int textureID;
    private String filePath;

    public Texture(String filePath) {
        this.filePath = filePath;
        init();
    }

    private void init() {
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

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
