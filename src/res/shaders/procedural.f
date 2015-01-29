#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;
uniform int viewportWidth;
uniform int viewportHeight;
uniform int shaderTimer;

out vec4 out_Color;

vec3 blackOrWhite() {
	vec3 pixelColor = vec3(0.27, 0.8, 0.27);
	if (int(pass_TextureCoord.t * viewportHeight / 6) % 2 == 1) {
		pixelColor = vec3(0, 0.68, 0);
	}
	if (int(pass_TextureCoord.t * viewportHeight) % 6 == 0 || int(pass_TextureCoord.t * viewportHeight) % 6 == 0) {
		pixelColor = vec3(0.1, 0.7, 0.1);
	}
	return pixelColor;
}

void main(void) {
	out_Color = vec4(blackOrWhite(), 0.5);
}
