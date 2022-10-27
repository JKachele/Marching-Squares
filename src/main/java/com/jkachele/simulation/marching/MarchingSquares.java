/******************************************
 *Project-------Marching-Squares
 *File----------MarchingSquares.java
 *Author--------Justin Kachele
 *Date----------10/27/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation.marching;

import com.jkachele.simulation.display.Scene;
import com.jkachele.simulation.render.Renderer;
import com.jkachele.simulation.util.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class MarchingSquares {
    private static Vector2i gridResolution;
    private static ImplicitFunction2D function;

    private static Vector2f[][] positions;
    private static float[][] values;

    private static boolean interpolate = false;

    public static void init(Vector2i gridResolution, ImplicitFunction2D function) {
        MarchingSquares.gridResolution = gridResolution;
        MarchingSquares.function = function;
        start();
    }

    public static void init(Vector2i gridResolution) {
        MarchingSquares.gridResolution = gridResolution;
        MarchingSquares.function = (x, y) -> (x * x) + (y * y);     // Default function is a unit circle
        start();
    }

    private static void start() {
        positions = new Vector2f[gridResolution.x][gridResolution.y];
        values = new float[gridResolution.x][gridResolution.y];
        Vector2f cameraSize = Scene.getCamera().getProjectionSize();
        for (int i = 0; i < gridResolution.x; i++) {
            for (int j = 0; j < gridResolution.y; j++) {
                float x = (cameraSize.x / gridResolution.x) * i;
                float y = (cameraSize.y / gridResolution.y) * j;
                positions[i][j] = new Vector2f(x, y);
                float value = function.call(x, y);
                values[i][j] = value;
//                if (value < 0) {
//                    Renderer.addPoint2D(new Vector2f(x, y), Color.WHITE.toVector(), 1);
//                } else {
//                    Renderer.addPoint2D(new Vector2f(x, y), Color.BLACK.toVector(), 1);
//                }
            }
        }
        march();
    }

    private static void march() {
        // Loop through each square
        for (int i = 1; i < gridResolution.x; i++) {
            for (int j = 1; j < gridResolution.y; j++) {
                int caseNum = getCaseNum(i, j);
                int[] lines = LookupTable.TABLE[caseNum];

                Vector2i a = new Vector2i(i-1, j-1);
                Vector2i b = new Vector2i(i, j-1);
                Vector2i c = new Vector2i(i, j);
                Vector2i d = new Vector2i(i-1, j);

                for (int k = 0; k < (lines.length / 2); k++) {
                    Vector2f start = new Vector2f();
                    Vector2f end = new Vector2f();

                    /* d--3--c
                       |     |
                       4     2
                       |     |
                       a--1--b */

                    switch (lines[k]) {
                        case 1 -> start = getPoint(a, b);
                        case 2 -> start = getPoint(b, c);
                        case 3 -> start = getPoint(c, d);
                        case 4 -> start = getPoint(d, a);
                    }

                    switch (lines[k + 1]) {
                        case 1 -> end = getPoint(a, b);
                        case 2 -> end = getPoint(b, c);
                        case 3 -> end = getPoint(c, d);
                        case 4 -> end = getPoint(d, a);
                    }

                    Renderer.addLine2D(start, end, Color.WHITE.toVector(), 1);
                }
            }
        }
    }

    private static int getCaseNum(int x, int y) {
        /* d--3--c
           |     |
           4     2
           |     |
           a--1--b */
        float a = values[x-1][y-1];
        float b = values[x][y-1];
        float c = values[x][y];
        float d = values[x-1][y];

        // convert to binary representation of the state of the grid
        int caseNum = 0;
        if (a > 0) caseNum += 1;
        if (b > 0) caseNum += 2;
        if (c > 0) caseNum += 4;
        if (d > 0) caseNum += 8;

        return caseNum;
    }

    private static Vector2f getPoint(Vector2i startIndex, Vector2i endIndex) {
        Vector2f startPos = positions[startIndex.x][startIndex.y];
        Vector2f endPos = positions[endIndex.x][endIndex.y];

        float x = startPos.x;
        float y = startPos.y;

        if (interpolate) {
            float startVal = values[startIndex.x][startIndex.y];
            float endVal = values[endIndex.x][endIndex.y];

            if (startIndex.x == endIndex.x) {
                y = interpolate(startPos.y, endPos.y, startVal, endVal);
            } else {
                x = interpolate(startPos.x, endPos.x, startVal, endVal);
            }
        } else {
            if (startIndex.x == endIndex.x) {
                y = (startPos.y + endPos.y) / 2;
            } else {
                x = (startPos.x + endPos.x) / 2;
            }
        }

        return new Vector2f(x, y);
    }

    private static float interpolate(float startPos, float endPos, float startVal, float endVal) {


        return 0;
    }

    public static float[][] getValues() {
        return values;
    }
}
