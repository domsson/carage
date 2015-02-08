#version 130

in vec4 in_Position;
in vec3 in_Color;
in vec2 in_TextureCoord;
in vec3 in_Normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 normalMatrix;
uniform vec3 lightPosition;

uniform float lightIntensity;
uniform float materialAmbientReflectivity;
uniform float materialDiffuseReflectivity;
uniform float materialSpecularReflectivity;
uniform int   materialSpecularHardness;

out vec3 pass_Position;
out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;
out vec3 pass_LightPosition;

float diffuseComponent(vec3 light, vec3 normal) {
	return max(dot(light, normal), 0);
}

float specularComponent(vec3 reflection, vec3 camera, float hardness) {
	return pow(max(dot(reflection, camera), 0), hardness);
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
	return clamp(intensity, 0, 1);
}

void main(void) {

	mat4 modelViewMatrix = viewMatrix * modelMatrix; // TODO pre-calculate this in java
	mat4 modelViewProjectionMatrix = projectionMatrix * modelViewMatrix;
	
	pass_Position = (modelViewMatrix * in_Position).xyz;
	pass_LightPosition = (viewMatrix * vec4(lightPosition.x, lightPosition.y, lightPosition.z, 1.0)).xyz; // TODO pre-calculate this in java
	//pass_Color = vec4(in_Color, 1.0);
	pass_TextureCoord = in_TextureCoord;	
	pass_Normal = normalize((normalMatrix * vec4(in_Normal.x, in_Normal.y, in_Normal.z, 1.0)).xyz); // necessary in case of non-uniformly scaled objects (normals will change, too!)
	
	gl_Position = projectionMatrix * modelViewMatrix * in_Position;

	vec3 L = lightVector(lightPosition, gl_Position.xyz);
	pass_Color = vec4(1, 1, 1, 1.0) * intensity(L, pass_Normal, lightIntensity);
	
	

}

