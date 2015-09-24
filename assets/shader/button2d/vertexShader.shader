uniform mat4 uMatrix;
attribute vec3 vPosition;
attribute vec2 a_texcoord;
varying vec2 v_texcoord;

void main() {
	v_texcoord = a_texcoord;
	gl_Position = uMatrix * vec4(vPosition,1.0);
	
}