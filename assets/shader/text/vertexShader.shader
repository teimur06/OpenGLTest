uniform mat4 matrix;
uniform vec4 uColor;

attribute vec4 vPosition;
attribute vec2 a_texCoord;

varying vec4 vColor;
varying vec2 v_texCoord;

void main() {
   gl_Position =  matrix * vPosition;
   v_texCoord = a_texCoord;
   vColor = uColor;
}



