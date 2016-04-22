uniform mat4 matrix;
attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexcoord;
varying vec3 vPosition;
varying vec2 vTexcoord;
varying vec3 v_normal;
void main() {
	vPosition = aPosition;
	vTexcoord = aTexcoord;
	v_normal = aNormal;
	gl_Position = matrix * vec4(aPosition,1.0);
}