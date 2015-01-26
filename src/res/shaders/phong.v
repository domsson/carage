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

out vec3 pass_Position;
out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;
out vec3 pass_LightPosition;

void main(void) {

	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	mat4 modelViewProjectionMatrix = projectionMatrix * modelViewMatrix;
	
	pass_Position = (modelViewMatrix * in_Position).xyz;
	pass_LightPosition = (modelViewMatrix * vec4(lightPosition.x, lightPosition.y, lightPosition.z, 1.0)).xyz;
	pass_Color = vec4(in_Color, 1.0);
	pass_TextureCoord = in_TextureCoord;	
	pass_Normal = normalize((normalMatrix * vec4(in_Normal.x, in_Normal.y, in_Normal.z, 1.0)).xyz); // necessary in case of non-uniformly scaled objects (normals will change, too!)
	
	gl_Position = projectionMatrix * modelViewMatrix * in_Position;
}

