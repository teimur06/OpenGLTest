package com.gamecj.object3d;

import java.io.IOException;

import com.gamecj.io.Obj3DParcerException;
import com.gamecj.io.Obj3DParser;
import com.gamecj.io.Texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class Obj3d extends Object3D {

	int vertexLength ;
	private boolean draw;
	public Obj3d(Context context) {
		super(context,"obj3d/vertexShader.shader","obj3d/fragmentShader.shader");
		vertexLength = 0;
		draw = true;
		Obj3DParser obj3dParcer = null;
		try {
			obj3dParcer = new Obj3DParser(context,"Grid.obj","models");
			vertexLength = obj3dParcer.getVertex().length;
			vertexBuffer =floatArrayToFloadBuffer(obj3dParcer.getVertex());
			textureCoordinatesBuffer = floatArrayToFloadBuffer(obj3dParcer.getTexureCoordinates());
			normalBuffer = floatArrayToFloadBuffer(obj3dParcer.getNormal());
			indexBuffer = shortArrayToShortBuffer(obj3dParcer.getIndex());
			texture = new Texture(context);
			if (!texture.createTexture2DFromAssets("texture/"+obj3dParcer.getTextureName(), null)) draw = false;
			
		} catch (Obj3DParcerException e) {
			Log.e("Obj3DParser","file: '"+e.getFileName()+"' error line "+ Integer.toString(e.getLineNumber()) +": "+e.getMessage());
			draw = false;
		} catch (IOException e) {
			Log.e("Obj3DParser","Error open file :"+e.getMessage());
			draw = false;
		}
	}
	
	
	@Override
	public void Draw(float[] viewProjectionMatrix) {
		super.Draw(viewProjectionMatrix);
		if (!draw) return;
		GLES20.glUseProgram(shaderProgram);

		
		
		int aPosition = GLES20.glGetAttribLocation(shaderProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		int aTextCoord = GLES20.glGetAttribLocation(shaderProgram, "aTextCoord");
		GLES20.glEnableVertexAttribArray(aTextCoord);
		GLES20.glVertexAttribPointer(aTextCoord, 2, GLES20.GL_FLOAT, false, 0, textureCoordinatesBuffer);		
		
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, viewProjectionMatrix, 0, worldMatrix, 0);
		int matrix = GLES20.glGetUniformLocation(shaderProgram, "matrix");
		GLES20.glUniformMatrix4fv(matrix, 1,false, worldViewProjectionMatrix,0);			
	    
		
		// set texture
	    int uTexture = GLES20.glGetUniformLocation(shaderProgram, "uTexture");
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getName());
	    GLES20.glUniform1i(uTexture, 0);		
	    
		
	    // Draw Cubed
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexLength/3);
	   // GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indexLength, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

	 //   Log.d("Obj3DParcer",worldMatrix)
	    
	    // Disable attributes
		GLES20.glDisableVertexAttribArray(aPosition);
		GLES20.glDisableVertexAttribArray(aTextCoord);
		
	};
}
