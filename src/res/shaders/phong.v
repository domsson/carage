#version 130

in vec4 in_Position;
in vec3 in_Color;
in vec2 in_TextureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec4 pass_Color;
out vec2 pass_TextureCoord;


void main(void) {

	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
	
	pass_Color = vec4(in_Color, 1.0);
	pass_TextureCoord = in_TextureCoord;
}
