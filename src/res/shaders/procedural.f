#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;

out vec4 out_Color;

vec3 blackOrWhite {
	vec3 pixelColor = (0, 0, 0);
	if (pass_TextureCoord.u % 2 == 1)
		pixelColor = (1, 1, 1);
	return pixelColor;
}

void main(void) {
	out_Color = vec4(blackOrWhite, 0.5);
}
