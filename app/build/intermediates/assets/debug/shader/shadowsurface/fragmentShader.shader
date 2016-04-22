	precision mediump float;
	uniform sampler2D uTexture;
	uniform sampler2D uTexture2;
	uniform float uTemp;
	varying vec2 vTexcoord;
	void main() {
	  if (uTemp == 1.0)
	  {
		    float Depth = texture2D(uTexture, vTexcoord).z;
		    if (Depth>0.0) Depth = 0.3; else Depth = 1.0;
	    	gl_FragColor = texture2D(uTexture2, vTexcoord)  *Depth;
	  } else
	  {
	 
	  }
	}