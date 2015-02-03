#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;

out vec4 out_Color;


// https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php
void main(void) {
	out_Color = texture(tex, pass_TextureCoord) * pass_Color;
}

