#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;
uniform int viewportResolution[2];
uniform int viewportWidth;
uniform int viewportHeight;

out vec4 out_Color;

vec3 blackOrWhite() {
	vec3 pixelColor = vec3(1, 0, 0);
/*	if (int(pass_TextureCoord.s * viewportResolution[0] / 5) % 2 == 1) {
		pixelColor = vec3(1, 1, 1);
	}*/
	if (viewportWidth == 800)
		pixelColor = vec3(0, 1, 0);
	return pixelColor;
}

void main(void) {
	out_Color = vec4(blackOrWhite(), 0.5);
}
