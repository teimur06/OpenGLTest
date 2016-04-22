package com.gamecj.object3d;

import com.gamecj.io.Texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;



public class Cube extends Object3D {
//	private final String LOG = "Triangle";

	final float coord_x = 1.0f;
	final float coord_y = 1.0f;
	final float coord_z = 1.0f;
	private float vertices[] = {
			-coord_x, coord_y, -coord_z,
			coord_x, coord_y, -coord_z,
			-coord_x, -coord_y, -coord_z,
			coord_x, -coord_y, -coord_z,
			
			coord_x, coord_y, -coord_z,
			coord_x, coord_y, coord_z,
			coord_x, -coord_y, -coord_z,
			coord_x, -coord_y, coord_z,		
			
			coord_x, coord_y, coord_z,
			-coord_x, coord_y, coord_z,
			coord_x, -coord_y, coord_z,
			-coord_x, -coord_y, coord_z,	
			
			-coord_x, coord_y, coord_z,
			-coord_x, coord_y, -coord_z,
			-coord_x, -coord_y, coord_z,
			-coord_x, -coord_y, -coord_z,				
	};

	float lightPosition[];
	float camera[];
	
	
	private float normal[] = {
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,			
	};	
	
	
	private short index[] = 
		{
			0,1,2,2,1,3,
			4,6,5,5,6,7,
			8,10,9,9,10,11,
			12,14,13,13,14,15
		};
	
	private float TextureCoordinates[] = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,		
			
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,	
	};	

	private float angleY,angleX;
	private float normalMatrix[];

	
	public Cube(Context context) {
		super(context,"cube/vertexShader.shader","cube/fragmentShader.shader");
		vertexBuffer = floatArrayToFloadBuffer(vertices);
		indexBuffer = shortArrayToShortBuffer(index);
		normalBuffer = floatArrayToFloadBuffer(normal);
		textureCoordinatesBuffer = floatArrayToFloadBuffer(TextureCoordinates);
		texture = new Texture(context);
		texture.createTexture2DFromAssets("texture/butterfly.png", null);

		angleY = angleX = 0;
		
		lightPosition = new float[3];
		lightPosition[0] = 0;
		lightPosition[1] = 0.5f;
		lightPosition[2] = -3;
				 
		camera = new float[3];
		camera[0] = -5;
		camera[1] = 8;
		camera[2] = -8;
		
		normalMatrix = new float[16];
		Matrix.setIdentityM(normalMatrix, 0);
		
		loadTexture() ;
	}
	
	public void setLightPosition(float x,float y, float z)
	{
		lightPosition[0] = x;
		lightPosition[1] = y;
		lightPosition[2] = z;
	}
	
	public float getLightX(){return lightPosition[0];}
	public float getLightY(){return lightPosition[1];}
	public float getLightZ(){return lightPosition[2];}
	
	@Override
		public void loadTexture() {
			super.loadTexture();
			texture = new Texture(context);
			texture.createTexture2DFromAssets("texture/butterfly.png", null);
		}

	
	public void setAngleY(float angle)
	{
		if (angle == 0 ) this.angleY = 0;
		this.angleY += angle;
		if (this.angleY >180)this.angleY = -180;
		if (this.angleY <-180) this.angleY = 180;
	}

	public void setAngleX(float angle)
	{
		if (angle == 0 ) this.angleX = 0;
		this.angleX += angle;
		if (this.angleX >180)this.angleX = -180;
		if (this.angleX <-180) this.angleX = 180;
	}

	
	public void Draw(float[] ProjectionMatrix,float[] ViewMatrix) {
		
		
		GLES20.glUseProgram(shaderProgram);
		// set position
		int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(positionAttrib);
		GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// set textcoord
		int a_texcoord = GLES20.glGetAttribLocation(shaderProgram, "a_texcoord");
		GLES20.glEnableVertexAttribArray(a_texcoord);
		GLES20.glVertexAttribPointer(a_texcoord, 2, GLES20.GL_FLOAT, false, 0, textureCoordinatesBuffer);
		
		//set normals
		int a_normal = GLES20.glGetAttribLocation(shaderProgram, "a_normal");
		GLES20.glEnableVertexAttribArray(a_normal);
		GLES20.glVertexAttribPointer(a_normal, 4, GLES20.GL_FLOAT, false, 0, normalBuffer);		
		
	
		// set matrix Projection * View * Rotation * world
		
		//Matrix.multiplyMM(worldViewProjectionMatrix, 0, ProjectionMatrix, 0, worldMatrix, 0);
		int u_ProjectionMatrix = GLES20.glGetUniformLocation(shaderProgram, "u_ProjectionMatrix");
		GLES20.glUniformMatrix4fv(u_ProjectionMatrix, 1,false, ProjectionMatrix,0);
		
		
		
		int u_viewMatrix = GLES20.glGetUniformLocation(shaderProgram, "u_viewMatrix");
		GLES20.glUniformMatrix4fv(u_viewMatrix, 1,false, ViewMatrix,0);		
		
		
		rotateXY(worldMatrix,angleX,angleY);
		int u_WorldMatrix = GLES20.glGetUniformLocation(shaderProgram, "u_WorldMatrix");
		GLES20.glUniformMatrix4fv(u_WorldMatrix, 1,false, worldMatrix,0);			
		
		
		rotateXY(normalMatrix,angleX,angleY);
		int u_normalMatrix = GLES20.glGetUniformLocation(shaderProgram, "u_normalMatrix");
		GLES20.glUniformMatrix4fv(u_normalMatrix, 1,false, normalMatrix,0);
		
		// set texture
	    int u_texture0_Handle = GLES20.glGetUniformLocation(shaderProgram, "u_texture");
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getName());
	    GLES20.glUniform1i(u_texture0_Handle, 0);		
		
	    
	    int u_lightPosition = GLES20.glGetUniformLocation(shaderProgram, "u_lightPosition");
	    GLES20.glUniform3fv(u_lightPosition, 1, lightPosition, 0);
	    
	    camera[0]= ViewMatrix[12]; camera[1]= ViewMatrix[13]; camera[2]= ViewMatrix[14];
	    int u_camera = GLES20.glGetUniformLocation(shaderProgram, "u_camera");
	    GLES20.glUniform3fv(u_camera, 1, camera, 0);
	    
	    // Draw Cube
	    GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    
	    // Disable attributes
		GLES20.glDisableVertexAttribArray(positionAttrib);
		GLES20.glDisableVertexAttribArray(a_texcoord);		
		GLES20.glDisableVertexAttribArray(a_normal);
		
	};
	

	
}
