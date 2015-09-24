precision mediump float;

uniform sampler2D u_texture;
uniform vec3 u_lightPosition;
uniform vec3 u_camera;
varying vec2 v_texcoord;
varying vec3 v_normal;
varying vec3 v_vertex;

void main() {

    vec3 n_normal=normalize(v_normal);
    vec3 lightvector = normalize(u_lightPosition - v_vertex);
    vec3 lookvector = normalize(u_camera - v_vertex);
    float ambient=0.2;
    float k_diffuse=0.9;
    float k_specular=0.4;
    float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);
    vec3 reflectvector = reflect(-lightvector, n_normal);
    float specular = k_specular * pow( max(dot(lookvector,reflectvector),0.0), 40.0 );
    

    vec4 textureColor = texture2D(u_texture,v_texcoord);
    gl_FragColor =   (ambient+diffuse+specular)*textureColor;
}