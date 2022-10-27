#type vertex
#version 460 core
layout (location=0) in vec3 aPos;       // Position
layout (location=1) in vec4 aColor;    // Normal

uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 1.0);
}

    #type fragment
    #version 460 core

in vec4 fColor;

out vec4 color;

void main() {
    color = fColor;
}