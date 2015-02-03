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

	pass_TextureCoord = in_TextureCoord;
	gl_Position = modelMatrix * in_Position;
}

