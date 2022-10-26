#type vertex
#version 460 core
layout (location=0) in vec3 aPos;       // Position
layout (location=1) in vec2 aUVCoords;  // UV Coordinates
layout (location=2) in vec4 aColor;    // Normal

uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

out vec2 fUVCoords;
out vec4 fColor;

void main() {
    fUVCoords = aUVCoords;
    fColor = aColor;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 1.0);
//    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 460 core

in vec2 fUVCoords;
in vec4 fColor;

uniform sampler2D uTextures;

out vec4 color;

void main() {
//    color = texture(uTextures, fUVCoords);
    color = fColor;
}