varying vec4 color;
varying vec2 uv;
uniform sampler2D s;

void main(void) {
	gl_FragColor = texture2D(s, uv);
}