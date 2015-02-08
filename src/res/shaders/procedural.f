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
uniform float noiseGrainSizeX;
uniform float noiseGrainSizeY;

const float scanlineAlpha = 0.25;
const vec3 greenLight = vec3(0.35, 0.9, 0.35);
const vec3 greenMedium = vec3(0.1, 0.6, 0.1);
const vec3 greenDark = vec3(0, 0.5, 0);
const int pixelStepSize = 4;
const float linesFactor = 0.2;
const float noiseFactor = 0.3;

out vec4 out_Color;

// http://byteblacksmith.com/improvements-to-the-canonical-one-liner-glsl-rand-for-opengl-es-2-0/
highp float rand(vec2 co)
{
    highp float a = 12.9898;
    highp float b = 78.233;
    highp float c = 43758.5453;
    highp float dt= dot(co.xy ,vec2(a,b));
    highp float sn= mod(dt,3.14);
    return fract(sin(sn) * c);
}

vec3 scanlines() {
	int time = int(scanlineTimer * 12) % 6;
	int lineNumber = int(pass_TextureCoord.t * viewportHeight) % 6;

	vec3 pixelColor = greenLight;
	if (lineNumber == ((0 + time) % 6) || lineNumber == ((3 + time) % 6)) {
		pixelColor = greenMedium;
	}
	if (lineNumber == ((4 + time) % 6) || lineNumber == ((5 + time) % 6)) {
		pixelColor = greenDark;
	}

	return pixelColor;
}

vec2 quantitise(vec2 st) {
    return vec2(int((st.s / noiseGrainSizeX) + 0.5), int((st.t / noiseGrainSizeY) + 0.5));
}

void main(void) {
	vec4 texColor = texture(tex, pass_TextureCoord);
    vec4 scanlineColor = vec4(scanlines(), scanlineAlpha);
    vec2 quantTexCoords = quantitise(pass_TextureCoord);
    float noise = ((rand(vec2(quantTexCoords.s * scanlineTimer, quantTexCoords.t)) - 0.5) * noiseFactor) + 1;
    vec4 modifiedTexColor = (texColor.r > 0.1 && texColor.g == 0 && scanlineTimer > 0.5) ? vec4(0, 0, 0, 1) : texColor;
    out_Color = (modifiedTexColor.a == 0) ? scanlineColor * noise : (1 - linesFactor) * modifiedTexColor + linesFactor * scanlineColor * noise;
}
