package com.gamecj.object3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Line3D extends Object3D {

	//private final String LOG = "Line3D";
	
	private float vertices[] = {
		   0.0f, 0.0f, 0.0f,
           15.0f, 0.0f, 0.0f,
           
           0.0f, 0.0f, 0.0f,
           0.0f, 15.0f, 0.0f,
           
           0.0f, 0.0f, 0.0f,
           0.0f, 0.0f, 15.0f,           
	};

	private short index[] = 
		{
			0,1,2,3,4,5
		};
	
	private float color[] = {
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
	};
	
	
	public Line3D(Context context) {
		super(context,"Line3D/vertexShader.shader","Line3D/fragmentShader.shader");
		vertexBuffer = floatArrayToFloadBuffer(vertices);
		indexBuffer = shortArrayToShortBuffer(index);
		colorBuffer = floatArrayToFloadBuffer(color);
	}
	
	@Override
	public void Draw(float[] ViewProjectionMatrix) {
		super.Draw(ViewProjectionMatrix);
		// set position
		GLES20.glUseProgram(shaderProgram);
		int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(positionAttrib);
		GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		
		int colorAttrib = GLES20.glGetAttribLocation(shaderProgram, "aColor");
		GLES20.glEnableVertexAttribArray(colorAttrib);
		GLES20.glVertexAttribPointer(colorAttrib, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

		// set matrix Projection * View  * world
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, ViewProjectionMatrix, 0, worldMatrix, 0);
		int mat4x4 = GLES20.glGetUniformLocation(shaderProgram, "matrix");
		GLES20.glUniformMatrix4fv(mat4x4, 1,false, worldViewProjectionMatrix,0);
		
	
		
		GLES20.glLineWidth(10);
	    // Draw Cube
	    GLES20.glDrawElements(GLES20.GL_LINES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    
	    // Disable attribute
		GLES20.glDisableVertexAttribArray(positionAttrib);	
		GLES20.glDisableVertexAttribArray(colorAttrib);
		
	}
	
}
