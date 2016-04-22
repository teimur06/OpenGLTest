precision mediump float;
varying vec3 vTextCoord;
uniform samplerCube uTexture;
void main() {
  gl_FragColor = textureCube(uTexture, vTextCoord);
}