uniform mat4 matrix;
attribute vec3 vPosition;
attribute vec4 aColor;
varying vec4 vColor;
void main() {

	vColor = aColor;
	gl_Position = matrix *  vec4(vPosition,1.0);
}