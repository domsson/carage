#version 130

in vec3 pass_Position;
in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 pass_Light;

uniform sampler2D tex;

out vec4 out_Color;

float diffuseComponent(vec3 light, vec3 normal) {
    return max(dot(light, normal), 0f);
}

float specularComponent(vec3 reflection, vec3 camera, float hardness) {
    return pow(max(dot(reflection, camera), 0f), hardness);
}

vec3 lightDirectionFromVertex(vec3 lightSource, vec3 position) {
    return normalize(lightSource - position);
}

float intensity(vec3 light, vec3 normal) {
	float brightness = 1f; // TODO: get this from java (Light/Lamp object property)
    float intensity = 0f;
	float ambient = 0f; // TODO: what's a good value? get this from java?
	float diffuse = 0f;
	float specular = 0f;
    float hardness = 2f; // TODO get this from the object/material (via java) somehow
    
    vec3 reflection = normalize(-reflect(light, normal));
    vec3 camera = normalize(-pass_Position);
	
	diffuse = brightness * diffuseComponent(light, normal);
	specular = brightness * specularComponent(reflection, camera, hardness);
    
    intensity = ambient + diffuse + specular;
	return clamp(intensity, 0f, 1f);
}

// https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php
void main(void) {
    
	vec3 lightSource = vec3(2f, 3f, 1f); // ugly hack because i forgot how to pass in a variable from java...
    vec3 light = lightDirectionFromVertex(lightSource, pass_Position);
    out_Color = texture(tex, pass_TextureCoord) * intensity(light, pass_Normal);
}

