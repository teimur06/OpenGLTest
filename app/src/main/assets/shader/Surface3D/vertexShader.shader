uniform mat4 matrix;
attribute vec3 aPosition;
void main() {
	gl_Position = matrix * vec4(aPosition,1.0);
}