precision mediump float;
varying vec4 vColor;
varying vec2 v_texCoord;
uniform sampler2D s_texture;
void main() {
  	vec4 a = texture2D(s_texture, v_texCoord);
  	a.xyz = vColor.xyz;
    gl_FragColor = a;
}