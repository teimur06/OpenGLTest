	precision mediump float;
	varying vec3 vPosition;
	uniform sampler2D uTexture;
	uniform sampler2D uTexture2;
	uniform vec3 u_camera;
	varying vec2 vTexcoord;
	varying vec3 v_normal;
	
	float getLight(vec3 lightPos,vec3 camPos,vec3 vertexPos, vec3 normal,
				   float ambient, float k_diffuse, float k_specular)
	{
		vec3 n_normal=normalize(normal);
	    vec3 lightvector = normalize(lightPos - vertexPos);
	    vec3 lookvector = normalize(camPos - vertexPos);
	    float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);
	    vec3 reflectvector = reflect(-lightvector, n_normal);
	    float specular = k_specular * pow( max(dot(lookvector,reflectvector),0.0), 60.0 );		
		return ambient+diffuse+specular;
	}
	
	
	void main() {
		vec3 lightPos = vec3(60.0,100.0,-415.0);
		float light = getLight(lightPos,u_camera,vPosition,v_normal,
							   0.6,  0.8,   0.4);

	    vec4 pixsel;
	    vec3 pos = normalize(vPosition);
	    if (pos.y > 0.01) 
	    	pixsel = texture2D(uTexture2,vTexcoord); 
	    else 
	    	pixsel = texture2D(uTexture,vTexcoord);
	
	    gl_FragColor = light * pixsel;
	}