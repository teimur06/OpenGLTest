uniform mat4 matrix;
attribute vec3 aPosition;
attribute vec2 aTexcoord;
varying vec2 vTexcoord;
void main() {
	vTexcoord = aTexcoord;
	gl_Position = matrix * vec4(aPosition,1.0);
}