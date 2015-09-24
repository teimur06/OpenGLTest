uniform mat4 matrix;
attribute vec3 aPosition;
attribute vec2 aTextCoord;
varying vec2 vTextCoord;

void main() {
vTextCoord = aTextCoord;
	gl_Position = matrix* vec4(aPosition,1.0);
}