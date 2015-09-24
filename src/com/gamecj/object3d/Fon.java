package com.gamecj.object3d;

import com.gamecj.io.Texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;



public class Fon extends Object3D{
	final float coord_x = 50.0f;
	final float coord_y = 50.0f;
	final float coord_z = 50.0f;
	private float vertices[] = {
			coord_x, -coord_y, -coord_z,  //0    
			-coord_x, -coord_y, -coord_z,  //1
			coord_x, -coord_y, coord_z,  //2
			-coord_x, -coord_y, coord_z,  //3
			
			-coord_x, -coord_y, coord_z,  //4    
			-coord_x, coord_y, coord_z,  //5
			coord_x, -coord_y, coord_z,  //6
			coord_x, coord_y, coord_z,  //7	
			
			coord_x, coord_y, coord_z,  //8
			coord_x, -coord_y, coord_z,  //9
			coord_x, coord_y, -coord_z,  //10
			coord_x, -coord_y, -coord_z,  //11		
			
			coord_x, -coord_y, -coord_z,  //12
			coord_x, coord_y, -coord_z,  //13
			-coord_x, -coord_y,  -coord_z,  //14
			-coord_x, coord_y, -coord_z,  //15				
			
			-coord_x, coord_y, -coord_z,  //16
			-coord_x, coord_y, coord_z,  //17
			-coord_x, -coord_y,-coord_z,  //18
			-coord_x, -coord_y, coord_z,  //19	
			
			-coord_x, coord_y, -coord_z,  //20
			coord_x, coord_y, -coord_z,  //21
			-coord_x, coord_y,coord_z,  //22
			coord_x, coord_y, coord_z,  //23					
			
	};

	private short index[] = 
		{
			0,1,2,2,1,3, 
			4,5,6,6,5,7,
			8,9,10,10,9,11,
			12,13,14,14,13,15,
			16,17,18,18,17,19,
			20,21,22,22,21,23

		};
	
	
	public Fon(Context context) {
		super(context,"fon/vertexShader.shader","fon/fragmentShader.shader");
		vertexBuffer = floatArrayToFloadBuffer(vertices);
		indexBuffer = shortArrayToShortBuffer(index);
		loadTexture() ;		
	}
	
	@Override
	public void loadTexture() {
		super.loadTexture();
		texture = new Texture(context);	
		texture.createTextureCubeFromAssets("texture/t1.jpg",
											"texture/t3.png",
											"texture/t3.png",
											"texture/t3.png",
											"texture/t5.jpg",
											"texture/t6.jpg",
											null);
	}
	
	@Override
	public void Draw(float[] viewProjectionMatrix) {
		super.Draw(viewProjectionMatrix);
		GLES20.glUseProgram(shaderProgram);
		// set position
		int aPosition = GLES20.glGetAttribLocation(shaderProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		
		int uTexture = GLES20.glGetUniformLocation(shaderProgram, "uTexture");
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texture.getName());
		GLES20.glUniform1i(uTexture, 0);
		
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, viewProjectionMatrix, 0, worldMatrix, 0);
		int mat4x4 = GLES20.glGetUniformLocation(shaderProgram, "matrix");
		GLES20.glUniformMatrix4fv(mat4x4, 1,false, worldViewProjectionMatrix,0);
		
		//GLES20.glEnable(GLES20.GL_BLEND);
							//Новый цвет,             цвет который уже в буффере кадра
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,             GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
	    // Draw Cube
	    GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    
	    GLES20.glDisable(GLES20.GL_BLEND);
	    // Disable attribute
		GLES20.glDisableVertexAttribArray(aPosition);
	
	}
}
