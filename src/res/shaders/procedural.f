#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform sampler2D tex;
uniform int viewportWidth;
uniform int viewportHeight;
uniform float scanlineTimer;

const vec3 greenLight = vec3(0.27, 0.8, 0.27);
const vec3 greenIntermediate = vec3(0.1, 0.7, 0.1);
const vec3 greenDark = vec3(0, 0.68, 0);

out vec4 out_Color;

vec3 scanlines() {
	int time = int(scanlineTimer) % 6;
	vec3 pixelColor = greenLight;
	if (int(pass_TextureCoord.t * viewportHeight) % 6 == ((0 + time) % 6) || int(pass_TextureCoord.t * viewportHeight) % 6 == ((3 + time) % 6)) {
		pixelColor = greenIntermediate;
	}
	if (int(pass_TextureCoord.t * viewportHeight) % 6 == ((4 + time) % 6) || int(pass_TextureCoord.t * viewportHeight) % 6 == ((5 + time) % 6)) {
		pixelColor = greenDark;
	}
	return pixelColor;
}

void main(void) {
	out_Color = vec4(scanlines(), 0.5);
}
