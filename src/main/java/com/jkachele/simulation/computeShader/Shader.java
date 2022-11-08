/******************************************
 *Project-------Marching-Squares
 *File----------Shader.java
 *Author--------Justin Kachele
 *Date----------10/29/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.simulation.computeShader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public class Shader {
    private int shaderProgramID;
    private String filePath;
    private String shaderSource;

    public Shader(String filePath) {
        this.filePath = filePath;
        init();
    }

    public void init() {
        try {
            shaderSource = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader:" + filePath;
        }
    }

    public void compile() {
        // =========================================================
        // Compile and link shaders
        // =========================================================
        int shaderID;

        // First load and compile the vertex shader
        shaderID = glCreateShader(GL_COMPUTE_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        // Check for errors in compilation process
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filePath + "'\n\tCompute shader compilation failed.");
            System.err.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }

        // Link the shader program and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, shaderID);
        glLinkProgram(shaderProgramID);

        // Check for errors in linking process
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filePath + "'\n\tProgram linking failed.");
            System.err.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}