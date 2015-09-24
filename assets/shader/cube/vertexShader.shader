uniform mat4 u_ProjectionMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_WorldMatrix;
uniform mat4 u_normalMatrix;
attribute vec3 vPosition;
attribute vec2 a_texcoord;
attribute vec3 a_normal;
varying vec2 v_texcoord;
varying vec3 v_normal;
varying vec3 v_vertex;

void main() {
    v_texcoord = a_texcoord;
    v_normal = vec3(u_normalMatrix * vec4(a_normal,1.0));
	v_vertex = vec3(u_normalMatrix * vec4(vPosition,1.0));
	gl_Position = u_ProjectionMatrix * u_viewMatrix * u_WorldMatrix * vec4(vPosition,1.0);
	
}