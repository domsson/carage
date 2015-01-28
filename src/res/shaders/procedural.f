#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_LightPosition;

uniform float lightIntensity;
uniform float materialAmbientReflectivity;
uniform float materialDiffuseReflectivity;
uniform float materialSpecularReflectivity;
uniform int   materialSpecularHardness;

uniform sampler2D tex;

out vec4 out_Color;

float diffuseComponent(vec3 light, vec3 normal) {
	return max(dot(light, normal), 0f);
}

float specularComponent(vec3 reflection, vec3 camera, float hardness) {
	return pow(max(dot(reflection, camera), 0f), hardness);
}

vec3 lightVector(vec3 lightSource, vec3 position) {
	return normalize(lightSource - position);
}

float intensity(vec3 light, vec3 normal, float lightIntensity) {
    vec3 reflection = normalize(-reflect(light, normal));
	vec3 camera     = normalize(-pass_Position);
    
    float ambient  = materialAmbientReflectivity;
	float diffuse  = lightIntensity * materialDiffuseReflectivity  * diffuseComponent(light, normal);
	float specular = lightIntensity * materialSpecularReflectivity * specularComponent(reflection, camera, materialSpecularHardness);
	
	float intensity = ambient + diffuse + specular;
	return clamp(intensity, 0f, 1f);
}

// https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php
void main(void) {
    vec3 L = lightVector(pass_LightPosition, pass_Position);
	out_Color = texture(tex, pass_TextureCoord) * intensity(L, pass_Normal, lightIntensity);
}
