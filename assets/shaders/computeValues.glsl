#version 460 core

layout(local_size_x = 32, local_size_y = 32, local_size_z = 1) in;
layout(r32f, binding = 0) uniform image2D out_tex1;

uniform ivec2 screenSize = ivec2(16, 9);

void main() {
    // get position to read/write data from
    ivec2 pos = ivec2(gl_GlobalInvocationID.xy);

    vec2 screenPos = pos * screenSize;

    vec4 pixel = vec4(screenPos.x, 0, 0, 0);

    // store new value in image
    imageStore( out_tex1, pos, pixel );
}