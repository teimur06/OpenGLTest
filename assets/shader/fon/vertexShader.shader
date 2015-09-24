uniform mat4 matrix;
attribute vec3 aPosition;
varying vec3 vTextCoord;
void main() {
	vTextCoord = aPosition;
	gl_Position = matrix* vec4(aPosition,1.0);
}