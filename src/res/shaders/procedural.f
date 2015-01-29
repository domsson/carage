#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;
uniform vec2 viewportResolution;

out vec4 out_Color;

vec3 blackOrWhite() {
	vec3 pixelColor = vec3(0, 0, 0);
/*	if (int(pass_TextureCoord.s * viewportResolution.x / 5) % 2 == 1) {
		pixelColor = vec3(1, 1, 1);
	}*/
	if (viewportResolution.x > 500 && viewportResolution.x < 1000)
		pixelColor = vec3(1, 0, 0);
	return pixelColor;
}

void main(void) {
	out_Color = vec4(blackOrWhite(), 0.5);
}
