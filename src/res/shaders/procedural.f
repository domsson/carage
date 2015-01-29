#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;
uniform int viewportWidth;
uniform int viewportHeight;

out vec4 out_Color;

vec3 blackOrWhite() {
	vec3 pixelColor = vec3(0, 0, 1);
	if (int(pass_TextureCoord.t * viewportHeight / 10) % 2 == 1) {
		pixelColor = vec3(0, 1, 0);
	}
	return pixelColor;
}

void main(void) {
	out_Color = vec4(blackOrWhite(), 0.5);
}
