precision mediump float;

uniform sampler2D u_texture;
uniform vec3 uColor;
varying vec2 v_texcoord;

void main() {
    vec4 textureColor = texture2D(u_texture,v_texcoord);
    gl_FragColor =  vec4(uColor,textureColor.a);
}