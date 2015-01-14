varying vec4 color;
varying vec2 uv;
uniform sampler2D s;

void main(void) {
	gl_FragColor = texture2D(s, uv);
    
    // out_Color = pass_Color;
    // out_Color = texture2D(s, pass_TextureCoord);
}
