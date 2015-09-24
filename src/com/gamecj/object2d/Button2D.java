package com.gamecj.object2d;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.gamecj.io.Texture;
import com.gamecj.object3d.Object3D;

public class Button2D extends Object3D {

	protected float borderX,borderY;
	protected float vertexButton[];
	protected short indexButton[] = {0,1,2,2,1,3};
	protected float textureCordinatesButton[] = {0,0,1,0,0,1,1,1};	
	protected float fColor[];
	protected float fColorRed[] = {1,0,0};
	protected float fColorGreen[] = {0,1,0};
	
	public Button2D(Context context, String vertexShaderPath,
			String fragmentShaderPath) {
		super(context, vertexShaderPath, fragmentShaderPath);
		borderX = borderY = 1;
	}
	
	public Button2D(Context context) {
		super(context, "button2d/vertexShader.shader","button2d/fragmentShader.shader");
		borderX = borderY = 1;
		fColor = fColorRed;
	}
	
	public void setPos(float x, float y)
	{
		setPos(x, y, 0);
	}
	
	public void createButton(float borderX, float borderY, String textureName)
	{
		this.borderX = borderX;
		this.borderY = borderY;
		texture = new Texture(context);
		texture.createTexture2DFromAssets(textureName, null);
		vertexButton = new float[] {
				-borderX, borderY, 0,  //0    
				borderX, borderY, 0,  //1
				-borderX, -borderY, 0,  //2
				borderX, -borderY, 0,  //3				
		};
		vertexBuffer =floatArrayToFloadBuffer(vertexButton);
		indexBuffer = shortArrayToShortBuffer(indexButton);
		textureCoordinatesBuffer = floatArrayToFloadBuffer(textureCordinatesButton);
	}
	
	  public RectF getRectF()
	  {
		return new RectF(worldMatrix[12]-borderX * scale,worldMatrix[13]+borderY*scale,worldMatrix[12]+borderX*scale,worldMatrix[13]-borderY*scale);
	  }
	  
	  protected boolean isPointToRect(float x, float y)
	  {
		  RectF f = getRectF();
		  return  ((x > f.left) && (x < f.right) && (y>f.bottom) && (y<f.top));
	  }
	  
	  public boolean OnDown(float x, float y)
	  {
		  boolean res = isPointToRect(x,y);
		  if (res) fColor = fColorGreen;
		  return res;
	  }
	  
	  public void onUp()
	  {
		  fColor = fColorRed;
	  }
	  
	  @Override
	  public void Draw(float viewProjectionMatrix[])
	  {
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
	           
	    int mTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram,   "a_texcoord" );
	           
	    // Prepare the texturecoordinates
	    GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
	                  false, 
	                  0, textureCoordinatesBuffer);
	   
	    GLES20.glEnableVertexAttribArray ( mPositionHandle );
	    GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
	   
	   
	    // get handle to shape's transformation matrix
	    int mtrxhandle = GLES20.glGetUniformLocation(shaderProgram, "uMatrix");
	    float mw[] = new float[16];
	    Matrix.multiplyMM(mw, 0,viewProjectionMatrix , 0, worldMatrix, 0);
	    GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mw, 0);
	    

	    
	    int uColor = GLES20.glGetUniformLocation(shaderProgram, "uColor");
	    GLES20.glUniform3fv(uColor, 1,  fColor, 0);
	    
	    int u_texture = GLES20.glGetUniformLocation (shaderProgram,  "u_texture" );
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getName());	           
	    GLES20.glUniform1i ( u_texture, 0);

	    
	    GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

	    // Draw the triangle
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexButton.length, 
	                GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	   
	    GLES20.glDisable(GLES20.GL_BLEND);
	    
	 
	    
	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	    GLES20.glDisableVertexAttribArray(mTexCoordLoc);
	       
	  }
}
