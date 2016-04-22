package com.gamecj.object3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Surface3D extends Object3D {

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
	
	public Surface3D(Context context) {
		super(context,"Surface3D/vertexShader.shader","Surface3D/fragmentShader.shader");
		vertexBuffer = floatArrayToFloadBuffer(vertices);
		indexBuffer = shortArrayToShortBuffer(index);
		
	}

	@Override
	public void Draw(float[] ViewProjectionMatrix) {
		super.Draw(ViewProjectionMatrix);
		GLES20.glUseProgram(shaderProgram);
		// set position
		int aPosition = GLES20.glGetAttribLocation(shaderProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// set matrix Projection * View * Rotation * world
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, ViewProjectionMatrix, 0, worldMatrix, 0);
		int mat4x4 = GLES20.glGetUniformLocation(shaderProgram, "matrix");
		GLES20.glUniformMatrix4fv(mat4x4, 1,false, worldViewProjectionMatrix,0);
		

	    // Draw Cube
	    GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    
	    // Disable attribute
		GLES20.glDisableVertexAttribArray(aPosition);

		
	}
	
}
