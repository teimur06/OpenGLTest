package com.gamecj.object3d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.gamecj.io.Texture;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class Object3D {
	protected Context context;
	private final String LOG = "Object3D";
	protected FloatBuffer vertexBuffer, textureCoordinatesBuffer, colorBuffer, normalBuffer ;
	protected ShortBuffer indexBuffer;
	protected String vertexShaderCode;
	protected String fragmentShaderCode;
	protected int shaderProgram;
	protected float worldMatrix[];
	protected float worldViewProjectionMatrix[];
	protected Texture texture;
	protected float scale;
	
	public Object3D(Context context,String vertexShaderPath, String fragmentShaderPath) {
		this.context = context;
		createShaderProgramm(vertexShaderPath, fragmentShaderPath);
		init();
	}
	
	public Object3D(Context context)
	{
		this.context = context;
		init();
	
	}
	
	private void init()
	{
		worldMatrix = new float[16];
		worldViewProjectionMatrix = new float[16];
		Matrix.setIdentityM(worldMatrix, 0);
		Matrix.setIdentityM(worldViewProjectionMatrix, 0);	
		texture = null;
		scale = 1.0f;
	}
	
	public void createShaderProgramm(String vertexShaderPath, String fragmentShaderPath)
	{
		vertexShaderCode = getShaderCodeAssets(context.getAssets(), vertexShaderPath);
		fragmentShaderCode = getShaderCodeAssets(context.getAssets(),fragmentShaderPath);
		Log.d(LOG, "Загрузка кода вершинного шейдера: "+Boolean.toString(!vertexShaderCode.isEmpty()));
		Log.d(LOG, "Загрузка кода фрагментного шейдера: "+Boolean.toString(!fragmentShaderCode.isEmpty()));
		
		int vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int frugmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		Log.d(LOG, "Создание вершинного шейдера: "+Boolean.toString(vertexShader != 0));
		Log.d(LOG, "Создание фрагментного шейдера: "+Boolean.toString(frugmentShader != 0));

		shaderProgram = GLES20.glCreateProgram();
		Log.d(LOG, "Создание шейдерной программы: "+Boolean.toString(shaderProgram != 0));		

		GLES20.glAttachShader(shaderProgram, vertexShader);
		GLES20.glAttachShader(shaderProgram, frugmentShader);
		GLES20.glLinkProgram(shaderProgram);
	}
	
	public void loadTexture()
	{
		
	}
	
	public void Draw(float viewProjectionMatrix[])
	{
		
	}
	
	public void getWorldMatrix(float outMatrix[])
	{
		for (int i=0; i<16;i++)
			outMatrix[i] = worldMatrix[i];
	}
	
	public void setWorldMatrix(float inMatrix[])
	{
		for (int i=0; i<16;i++)
			worldMatrix[i] = inMatrix[i];
	}
	
	public void setPos(float x, float y, float z)
	{
		worldMatrix[12] = x;
		worldMatrix[13] = y;
		worldMatrix[14] = z;
	}
	
	
	public void setScaleXYZ(float scale)
	{
		Matrix.scaleM(worldMatrix, 0, scale, scale, scale);
		this.scale = scale;
	}
	
	public void setScaleXY(float scale)
	{
		Matrix.scaleM(worldMatrix, 0, scale, scale, worldMatrix[10]);
		this.scale = scale;
	}
	
	public void setTexture(Texture texture){this.texture = texture;}
	public float getScale(){ return scale;}
	public float getX(){ return worldMatrix[12];}
	public float getY(){ return worldMatrix[13];}
	public float getZ(){ return worldMatrix[14];}
	
	public static void logMatrix(String LOG, float mat[])
	{
		Log.d(LOG,"================");
		Log.d(LOG,Float.toString(mat[0])+" "+Float.toString(mat[1])+" "+Float.toString(mat[2])+" "+Float.toString(mat[3]));
		Log.d(LOG,Float.toString(mat[4])+" "+Float.toString(mat[5])+" "+Float.toString(mat[6])+" "+Float.toString(mat[7]));
		Log.d(LOG,Float.toString(mat[8])+" "+Float.toString(mat[9])+" "+Float.toString(mat[10])+" "+Float.toString(mat[11]));
		Log.d(LOG,Float.toString(mat[12])+" "+Float.toString(mat[13])+" "+Float.toString(mat[14])+" "+Float.toString(mat[15]));
		Log.d(LOG,"================");
	}
	
	
	public static String getShaderCodeAssets(AssetManager am, String fileName)
	{	
		String sFileName = "shader/"+fileName;
		String result = "";
		try {
			StringBuilder sb = new StringBuilder();
			InputStream is = am.open(sFileName);
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String Line;
			while((Line = bf.readLine()) != null)
			{
				sb.append(Line);
			}
			result = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
		return result;
	}
	
	
	public static int LoadShader(int type, String shaderCode)
	{
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		final int[] compileStatus = new int[1];
	    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
    
	    
	    if (compileStatus[0] == 0) 
	    {
	    	
	    	String errorShader = GLES20.glGetShaderInfoLog(shader);
	    	Log.e("Object3D","error = " +errorShader);
	    	shader = 0;
	    }
		return shader;
	}	
	
	public static  FloatBuffer floatArrayToFloadBuffer  ( float array[])
	{
		FloatBuffer floatBuffer;
		ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
		bb.order(ByteOrder.nativeOrder());
		
		floatBuffer = bb.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);		
		
		return floatBuffer;
	}
	
	public static  ShortBuffer shortArrayToShortBuffer  ( short array[])
	{
		ShortBuffer shortBuffer;
		ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 2);
		bb.order(ByteOrder.nativeOrder());
		
		shortBuffer = bb.asShortBuffer();
		shortBuffer.put(array);
		shortBuffer.position(0);		
		
		return shortBuffer;
	}	
	
	public static void rotateXY(float matrix[], float angleX, float angleY)
	{
		float cloneInMatrix[] = matrix.clone();
		float matrixRot[] = new float[16];
		float matrixResult[] = new float[16];
        Matrix.setRotateM(matrixRot, 0, angleY, 0, 1, 0);
		Matrix.multiplyMM(matrixResult, 0, matrixRot, 0, cloneInMatrix, 0);
		Matrix.setRotateM(matrixRot, 0, angleX, 1, 0, 0);
		Matrix.multiplyMM(matrix, 0, matrixRot , 0,matrixResult, 0);		
	};
	

	public void free()
	{
		if (texture != null) 
		{
			texture.deleteTexture();
			texture = null;
		}
	}
	
}

