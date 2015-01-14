varying vec4 color;
varying vec2 uv;

void main(void) {
	color = gl_Color;
	uv = gl_MultiTexCoord0.xy;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
