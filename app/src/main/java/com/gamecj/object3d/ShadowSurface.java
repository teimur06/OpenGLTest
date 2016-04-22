package com.gamecj.object3d;

import com.gamecj.io.Texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class ShadowSurface extends Object3D {

	final float coord_x = 1.0f;
	final float coord_y = 0.0f;
	final float coord_z = 1.0f;
	private float vertices[] = {
			-coord_x, coord_y, coord_z,  //0    
			coord_x, coord_y, coord_z,  //1
			-coord_x, coord_y, -coord_z,  //2
			coord_x, coord_y, -coord_z,  //3
	};

	private short index[] = 
		{
			0,1,2,2,1,3, 
		};
	
	private float texCord[] = 
		{
			0,0,
			1,0,
			0,1,
			1,1,
		};
	
	public ShadowSurface(Context context) {
		super(context,"shadowsurface/vertexShader.shader","shadowsurface/fragmentShader.shader");
		vertexBuffer = floatArrayToFloadBuffer(vertices);
		indexBuffer = shortArrayToShortBuffer(index);
		textureCoordinatesBuffer = floatArrayToFloadBuffer(texCord);
		texture = new Texture(context);
		texture.createTexture2DFromAssets("texture/Brick.jpg", null);
	}

	
	public void Draw(float[] viewProjectionMatrix, int textureName) {
		GLES20.glUseProgram(shaderProgram);
		// set position
		int aPosition = GLES20.glGetAttribLocation(shaderProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// set textcoord
		int aTexcoord = GLES20.glGetAttribLocation(shaderProgram, "aTexcoord");
		GLES20.glEnableVertexAttribArray(aTexcoord);
		GLES20.glVertexAttribPointer(aTexcoord, 2, GLES20.GL_FLOAT, false, 0, textureCoordinatesBuffer);
		
		// set matrix Projection * View * Rotation * world
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, viewProjectionMatrix, 0, worldMatrix, 0);
		int mat4x4 = GLES20.glGetUniformLocation(shaderProgram, "matrix");
		GLES20.glUniformMatrix4fv(mat4x4, 1,false, worldViewProjectionMatrix,0);
		
		
		int uTemp = GLES20.glGetUniformLocation(shaderProgram, "uTemp");
		
		if (textureName != 0)
		{
			GLES20.glUniform1f(uTemp, 1.0f);
			
		    int uTexture = GLES20.glGetUniformLocation(shaderProgram, "uTexture");
		    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureName);
		    GLES20.glUniform1i(uTexture, 0);	
		    
		    int uTexture2 = GLES20.glGetUniformLocation(shaderProgram, "uTexture2");
		    GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,  texture.getName());
		    GLES20.glUniform1i(uTexture2, 1);	
		    
		   
		} else GLES20.glUniform1f(uTemp, 0.0f);
		
	    // Draw Cube
	    GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    
	    // Disable attribute
		GLES20.glDisableVertexAttribArray(aPosition);
		GLES20.glDisableVertexAttribArray(aTexcoord);
		

		
	}
}
