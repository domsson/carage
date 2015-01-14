#version 130

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

uniform sampler2D diffuse;

void main(void) {
    
    //out_Color = pass_Color;
    out_Color = texture2D(diffuse, pass_TextureCoord);
}
