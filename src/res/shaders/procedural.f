#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

// uniform float lightIntensity;
uniform float materialAmbientReflectivity;
uniform float materialDiffuseReflectivity;
uniform float materialSpecularReflectivity;
uniform int   materialSpecularHardness;

uniform sampler2D tex;

out vec4 out_Color;

void main(void) {
	out_Color = texture(tex, pass_TextureCoord);
}
