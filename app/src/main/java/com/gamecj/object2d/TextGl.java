package com.gamecj.object2d;

import com.gamecj.io.Texture;
import com.gamecj.object3d.Object3D;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;


public class TextGl extends Object3D {
    
	  private float RI_TEXT_UV_BOX_WIDTH = 83.0f/512.0f; // Ширина
	  private float RI_TEXT_UV_BOX_HEIGTH = 0.036376953125f;  // Высота
	  private float RI_TEXT_WIDTH = 0.6f;
	  private float RI_TEXT_HEIGTH = 1f;
	  private float[] vecs;
	  private float[] uvs;
	  private short[] indices;
	  private float[] colors;
	  private int index_vecs;
	  private int index_indices;
	  private int index_uvs;
	  private int index_colors;
	  private float uniformscale;
	  private String text;
	  float color[];
	  
	  public TextGl(Context context)
	  {
		super(context,"text/vertexShader.shader","text/fragmentShader.shader");
	    color = new float[4];
	    color[0]=color[1]=color[2]=color[3]=1.0f;
	    uniformscale=1;
	    
	  } 
	     
	  
	  @Override
	  public void loadTexture() {
		super.loadTexture();
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 2;
		texture = new Texture(context);
		texture.createTexture2DFromAssets("texture/font.png", options);
	}
	  
	  public void setTexture(Texture t) {texture = t;}
	  
	  public void setText(String text)
	  {
		  this.text = text;
		  updateText();
	  }

	  public float getWidth() { return ((float) text.length()) * RI_TEXT_WIDTH * uniformscale;}
	  
	  public void setColorText(float r, float g, float b, float a)
	  {
		  color[0]=r;
		  color[1]=g;
		  color[2]=b;
		  color[3]=a;
		  
	  }

	  
	  
	  public void AddCharRenderInformation(float[] vec, float[] cs, float[] uv, short[] indi)
	  {
	    // We need a base value because the object has indices related to 
	    // that object and not to this collection so basicly we need to 
	    // translate the indices to align with the vertexlocation in ou
	    // vecs array of vectors.
	    short base = (short) (index_vecs / 3);
	           
	    // We should add the vec, translating the indices to our saved vector
	    for(int i=0;i<vec.length;i++)
	    {
	      vecs[index_vecs] = vec[i];
	      index_vecs++;
	    }
	           
	    // We should add the colors.
	    for(int i=0;i<cs.length;i++)
	    {
	      colors[index_colors] = cs[i];
	      index_colors++;
	    }
	           
	    // We should add the uvs
	    for(int i=0;i<uv.length;i++)
	    {
	      uvs[index_uvs] = uv[i];
	      index_uvs++;
	    }
	   
	    // We handle the indices
	    for(int j=0;j<indi.length;j++)
	    {
	      indices[index_indices] = (short) (base + indi[j]);
	      index_indices++;
	    }
	  }
	  
	  public void updateText()
	  {
	    // Reset the indices.
	    index_vecs = 0;
	    index_indices = 0;
	    index_uvs = 0;
	    index_colors = 0;
	       
	    // Get the total amount of characters
	    int charcount = 0;

	        if(text!=null)
	        {
	          charcount += text.length(); 
	        }

	           
	    vecs = new float[charcount * 12];
	    colors = new float[charcount * 16];
	    uvs = new float[charcount * 8];
	    indices = new short[charcount * 6];
	    
	    convertTextToTriangleInfo();
	   
	  }
	       

	  private int convertCharToIndex(int c_val)
	    {
	        int indx = -1;
	 
	        if(c_val>64&&c_val<91) // A-Z
	            indx = c_val - 65;
	        
	        else if(c_val>96&&c_val<123) // a-z
	            indx = c_val - 97 + 26;
	        
	        else if(c_val>1039&&c_val<1071) // А-Я
	            indx = c_val - 1040 +52 ;

	        else if(c_val>1071&&c_val<1104) // а-я
	            indx = c_val - 1072 +84 ;

	        else if(c_val>47&&c_val<58) // 0-9
	            indx = c_val -48 + 116;
	        
	      

	        else 
	        	switch(c_val)
	        	{
	        	case 46: indx = 126; break;
	        	case 44: indx = 127; break;
	        	case 59: indx = 128; break;
	        	case 58: indx = 129; break;
	        	case 63: indx = 130; break;
	        	case 33: indx = 131; break;
	        	case 45: indx = 132; break;
	        	case 95: indx = 133; break;
	        	case 126: indx = 134; break;
	        	case 35: indx = 135; break;
	        	case 34: indx = 136; break;
	        	case 39: indx = 137; break;
	        	case 38: indx = 138; break;
	        	case 40: indx = 139; break;
	        	case 41: indx = 140; break;
	        	case 91: indx = 141; break;
	        	case 93: indx = 142; break;
	        	case 124: indx = 143; break;
	        	case 96: indx = 144; break;
	        	case 92: indx = 145; break;
	        	case 47: indx = 146; break;
	        	case 64: indx = 147; break;
	        	case 43: indx = 148; break;
	        	case 61: indx = 149; break;
	        	case 42: indx = 150; break;
	        	case 36: indx = 151; break;
	        	case 60: indx = 152; break;
	        	case 62: indx = 153; break;
	        	case 37: indx = 154; break;
	        	case 1025: indx = 155; break;
	        	case 1105: indx = 156; break;
	        	case 21: indx = 157; break;
	        	default: indx = 158; break;
	        	}

	        return indx;
	    }
	  
	  
	  private void convertTextToTriangleInfo()
	  {
	    // Get attributes from text object
	    float x = 0;
	    float y = 0;
	    
	    // Create 
	    for(int j=0; j<text.length(); j++)
	    {
	      // get ascii value
	      char c = text.charAt(j);
	      int indx = convertCharToIndex((int)c);
	             
	      // Creating the triangle information
	      float[] vec = new float[12];
	      float[] uv = new float[8];
	      float[] colors = new float[16];
	
	      //   X                                           Y                                               Z
	      
	      vec[0] = x;                                   vec[1] = y;                                       vec[2] = 0;
	      vec[3] = x + (RI_TEXT_WIDTH * uniformscale);  vec[4] = y ;                                      vec[5] = 0;
	      vec[6] = x;                                   vec[7] = -(y+ (RI_TEXT_HEIGTH * uniformscale));    vec[8] = 0;
	      vec[9] = x + (RI_TEXT_WIDTH * uniformscale);  vec[10] = -(y + (RI_TEXT_HEIGTH * uniformscale));  vec[11] = 0;
	         
 	      // Calculate the uv parts
	      int row =  indx / 6;
	      int col =  indx % 6;
       

	      float v = row *  RI_TEXT_UV_BOX_HEIGTH;
	      float v2 = v +  RI_TEXT_UV_BOX_HEIGTH;
	      float u = col * RI_TEXT_UV_BOX_WIDTH;
	      float u2 = u +  RI_TEXT_UV_BOX_WIDTH;
	      
	      // 0.001f = texture bleeding hack/fix
	      uv[0] = u;    uv[1] = v;
	      uv[2] = u2;   uv[3] = v;
	      uv[4] = u;    uv[5] = v2;
	      uv[6] = u2;   uv[7] = v2;
	      

	      short[] inds = {0,1,2,2,1,3};
	             
	      // Add our triangle information to our collection for 1 render call.
	      AddCharRenderInformation(vec, colors, uv, inds);
	             
	      // Calculate the new position
	      x += (RI_TEXT_WIDTH * uniformscale);
	    }
	    
	    

		colorBuffer = floatArrayToFloadBuffer(colors);
		vertexBuffer = floatArrayToFloadBuffer(vecs);
		textureCoordinatesBuffer = floatArrayToFloadBuffer(uvs);
		indexBuffer = shortArrayToShortBuffer(indices)   ; 
	  }
	 
	  public float getUniformscale() {
	    return uniformscale;
	  }
	 
	  public void setUniformscale(float uniformscale) {
	    this.uniformscale = uniformscale;
	  }	  
	  
	  
	  @Override
	  public void Draw(float viewProjectionMatrix[])
	  {
		  if (vertexBuffer == null) return;
	    // Set the correct shader for our grid object.
		GLES20.glUseProgram(shaderProgram);
     
	    // get handle to vertex shader's vPosition member
	    int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram,  "vPosition");
	           
	    // Enable a handle to the triangle vertices
	    GLES20.glEnableVertexAttribArray(mPositionHandle);
	        
	    // Prepare the background coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, 3,
	                                 GLES20.GL_FLOAT, false,
	                                 0, vertexBuffer);
	           
	    int mTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram,   "a_texCoord" );
	           
	    // Prepare the texturecoordinates
	    GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
	                  false, 
	                  0, textureCoordinatesBuffer);
	   
	    GLES20.glEnableVertexAttribArray ( mPositionHandle );
	    GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
	   
	   
	    // get handle to shape's transformation matrix
	    int mtrxhandle = GLES20.glGetUniformLocation(shaderProgram, "matrix");
	    float mw[] = new float[16];
	    Matrix.multiplyMM(mw, 0,viewProjectionMatrix , 0, worldMatrix, 0);
	    mw[12] = ( worldMatrix[12] - 1.0f) ;
	    mw[13] = (1.0f -  worldMatrix[13]);
	    //mw[12] = 0;
	//  logMatrix("TextGl", mw);
	    
	    GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mw, 0);
	    int uColor = GLES20.glGetUniformLocation(shaderProgram, "uColor");
	    GLES20.glUniform4fv(uColor, 1, color, 0);
	    
	    
	    
	    int mSamplerLoc = GLES20.glGetUniformLocation (shaderProgram, 
	              "s_texture" );
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getName());	           
	    // Set the sampler texture unit to our selected id
	    GLES20.glUniform1i ( mSamplerLoc, 0);

	    
	    GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

	    // Draw the triangle
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, 
	                GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	   
	    GLES20.glDisable(GLES20.GL_BLEND);
	    
	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	    GLES20.glDisableVertexAttribArray(mTexCoordLoc);
	       
	  }


	  

}

