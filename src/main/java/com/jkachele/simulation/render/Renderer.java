/******************************************
 *Project-------Marching-Squares
 *File----------Renderer.java
 *Author--------Justin Kachele
 *Date----------10/27/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.render;

import com.jkachele.simulation.display.Scene;
import com.jkachele.simulation.primitives.Line2D;
import com.jkachele.simulation.primitives.Point2D;
import com.jkachele.simulation.util.Color;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;

public class Renderer {
    private static int vaoID;
    private static int vboID;

    private static final int MAX_POINTS = 100000;

    // 7 floats per point (x, y, z, r, g, b, a)
    private static float[] vertices = new float[MAX_POINTS * 7];
    private static List<Line2D> lines = new ArrayList<>();
    private static List<Point2D> points = new ArrayList<>();
    private static ShaderParser shader;

    private static float lineWidth = 2.0f;
    private static float pointSize = 2.0f;

    private static boolean started = false;

    public static void init() {
        shader = new ShaderParser("assets/shaders/lineShader.glsl");
        shader.compile();

        // ============================================================
        // Generate VAO and VBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                (positionsSize) * Float.BYTES);
        glEnableVertexAttribArray(1);

        glEnable(GL_PROGRAM_POINT_SIZE);

        glLineWidth(lineWidth);
        glPointSize(pointSize);
    }

    public static void beginFrame() {
        if (!started) {
            init();
            started = true;
        }

        // Remove deadlines
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0 && !lines.get(i).isConstant()) {
                lines.remove(i);
                i--;
            }
        }
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).beginFrame() < 0 && !points.get(i).isConstant()) {
                points.remove(i);
                i--;
            }
        }
    }

    public static void draw() {
        if (lines.size() == 0 && points.size() == 0) {
            return;
        }

        int index = 0;
        if (lines.size() != 0) {
            for (Line2D line : lines) {
                for (int i = 0; i < 2; i++) {
                    Vector2f position = (i == 0 ? line.getStart() : line.getEnd());
                    Vector4f color = line.getColor();

                    // Load position into array
                    vertices[index] = position.x;
                    vertices[index + 1] = position.y;
                    vertices[index + 2] = -10.0f;

                    // Load color into array
                    vertices[index + 3] = color.x;
                    vertices[index + 4] = color.y;
                    vertices[index + 5] = color.z;
                    vertices[index + 6] = color.w;
                    index += 7;
                }
            }
        }
        if (points.size()!= 0) {
            for (Point2D point : points) {
                Vector2f position = point.getPosition();
                Vector4f color = point.getColor();

                // Load position into array
                vertices[index] = position.x;
                vertices[index + 1] = position.y;
                vertices[index + 2] = -10.0f;

                // Load color into array
                vertices[index + 3] = color.x;
                vertices[index + 4] = color.y;
                vertices[index + 5] = color.z;
                vertices[index + 6] = color.w;
                index += 7;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertices, 0,
                (lines.size() * 7 * 2) + (points.size() * 7)));

        // Use our shader
        shader.use();
        shader.uploadMat4f("uViewMatrix", Scene.getCamera().getViewMatrix());
        shader.uploadMat4f("uProjectionMatrix", Scene.getCamera().getProjectionMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 2);
        glDrawArrays(GL_POINTS, lines.size() * 2, points.size());

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.detach();
    }

    public static void setLineWidth(float lineWidth) {
        Renderer.lineWidth = lineWidth;
    }

    public static void setPointSize(float pointSize) {
        Renderer.pointSize = pointSize;
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector4f color, int lifetime) {
        if (lines.size() >= MAX_POINTS) {
            return;
        }
        Renderer.lines.add(new Line2D(start, end, color, lifetime));
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector4f color) {
        addLine2D(start, end, color, 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end) {
        addLine2D(start, end, Color.GREEN.toVector(), 1);
    }

    public static void addPoint2D(Vector2f position, Vector4f color, int lifetime) {
        if (points.size() >= MAX_POINTS) {
            return;
        }
        Renderer.points.add(new Point2D(position, color, lifetime));
    }

    public static void addPoint2D(Vector2f position, Vector4f color) {
        addPoint2D(position, color, 1);
    }

    public static void addPoint2D(Vector2f position) {
        addPoint2D(position, Color.GREEN.toVector(), 1);
    }
}
