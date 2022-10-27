/******************************************
 *Project-------Marching-Squares
 *File----------Main.java
 *Author--------Justin Kachele
 *Date----------10/26/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.simulation;

import com.jkachele.simulation.display.Engine;
import com.jkachele.simulation.util.Color;

public class Main {
    public static void main(String[] args) {
        final int DEFAULT_WIDTH = 1920;
        final int DEFAULT_HEIGHT = (DEFAULT_WIDTH / 16) * 9;    // 16 x 9 aspect ratio
        final String DEFAULT_TITLE = "Java Game";
        final Color backgroundColor = Color.BLACK;

        Engine engine = new Engine(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE, backgroundColor);
        engine.start();
    }
}
