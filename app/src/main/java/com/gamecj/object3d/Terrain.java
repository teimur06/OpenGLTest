package com.gamecj.object3d;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.gamecj.io.Texture;
import com.gamecj.vectors.Vec3;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class Terrain extends Object3D {

	private int numVertsPerRow;     // Количество вершин в строке
	private int numVertsPerCol;     // Количество вершин в столбце
	private int cellSpacing;        // Растояние между вершинами (Ширина и глубина клетки)
	private int numCellsPerRow;     // Количество клеток в строке
	private int numCellsPerCol;     // Количество клеток в столбце
	private int width;              // Ширина по X
	private int depth;              // Глубина по Z
	private int numVertices;        // Количество вершин
	private int numTriangles;       // Количество треугольников
	private float heightScale;      // Коэффицент масштабирования высоты

    
	private float vertexTerrain[];
	private float textureCoordinateTerrain[];
	private float normalTerrain[];
	private short indicesTerrain[];
	private ByteBuffer byteBuffer;
	private byte heigthMap[];
	private boolean draw;
	
	private int shaderProgrammNormal;
	private int numLinesNormal;
	private float vertexBufferNormalDraw[];
	private FloatBuffer vertexBufferNormalDrawFloatBuffer;

	Texture texture2;

	public void createShaderProgrammNormalLine()
	{
		vertexShaderCode = getShaderCodeAssets(context.getAssets(), "terrain/NormalVertexShader.shader");
		fragmentShaderCode = getShaderCodeAssets(context.getAssets(),"terrain/NormalFragmentShader.shader");
		
		int vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int frugmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		shaderProgrammNormal = GLES20.glCreateProgram();


		GLES20.glAttachShader(shaderProgrammNormal, vertexShader);
		GLES20.glAttachShader(shaderProgrammNormal, frugmentShader);
		GLES20.glLinkProgram(shaderProgrammNormal);
	}
	
	public Terrain(Context context) {
		super(context,"terrain/vertexShader.shader","terrain/fragmentShader.shader");
		createShaderProgrammNormalLine();
		
		InputStream ims = null;
		draw = true;
		try {
	    	// получаем входной поток
	    	ims = context.getAssets().open("heightMap/map.raw");
	    	
    		numVertsPerRow = 16;
    		numVertsPerCol = 16;
    		
    		cellSpacing = 50;
    		heightScale = 0.5f;
    		
    		numLinesNormal= numVertsPerRow*numVertsPerRow *2;
    		numCellsPerRow = numVertsPerRow - 1;
    		numCellsPerCol = numVertsPerCol - 1;
    		width = numVertsPerRow * cellSpacing;
    		depth = numVertsPerCol * cellSpacing;

    		int VR = numVertsPerRow %2;
    		if (VR == 0) numVertices = (numVertsPerRow+1) * (numVertsPerCol+1);
    	    else numVertices = numVertsPerRow * numVertsPerCol;

    		

    		numTriangles = numCellsPerRow * numCellsPerCol * 2;
    		
    		heigthMap = new byte[numVertices];
	    	ims.read(heigthMap);
	    	
	    	vertexBufferNormalDraw = new float[numVertices*3*2];
	    	
    		vertexTerrain = new float[numVertices*3];
    		normalTerrain = new float[numVertices*3];
    		textureCoordinateTerrain = new float[numVertices*2];
    		indicesTerrain = new short[numCellsPerRow*numCellsPerCol*6];
    		computeVertices();
    		computeIndices();
    		computeNormals();
    		texture = new Texture(context);
    		texture.createTexture2DFromAssets("texture/grass_texture238.jpg", null);
    		
    		texture2 = new Texture(context);
    		texture2.createTexture2DFromAssets("texture/sand.jpg", null);
		}
		catch(IOException ex) {
			draw = false;
		} 
		finally{
			if (ims!= null)
			{
				try {
					ims.close();
				} catch (IOException e) {}
			}
		}
	}
	
	private float getHeightmap(int row, int col)
	{
		int index = row * numVertsPerRow + col;
		if (index >= numVertices) return 0;
		if (index <0) return 0;
	     return (heigthMap[row * numVertsPerRow + col] & 255) * heightScale;
	}
	
	
	// Возвращает точную высоту карты 
	public float getHeight(float x, float z)
	{
		
		x = (width/2 + x) / cellSpacing;
		z = (depth/2 - z) / cellSpacing;
		
		int col = (int) Math.floor(x);
		int row = (int) Math.floor(z);
		
		float A = getHeightmap(row, col);
		float B = getHeightmap(row, col+1);
		float C = getHeightmap(row+1, col);
		float D = getHeightmap(row+1, col+1);
		
		float dx = x - col;
		float dz = z - row;
		
		if(dz < 1.0f - dx) // верхний треугольник ABC
		{
		     float uy = B - A; // A->B
		     float vy = C - A; // A->C
		     return A + uy*dx+ vy*dz; // A + Упращенная формула линейной интерполяции Y по оси X + Упращенная формула линейной интерполяции Y по оси Z
		}
		else // нижний треугольник DCB
		{
		     float uy = C - D; // D->C
		     float vy = B - D; // D->B
		     return D + uy*(1- dx)+  vy*(1- dz);
		}

	}


	
	private void computeVertices()
	{
		
	     // координаты, с которых начинается генерация вершин
	     int startX = -width / 2;
	     int startZ =  depth / 2;

	     // координаты, на которых завершается генерация вершин
	     int endX =  width / 2;
	     int endZ = -depth / 2;

	     // вычисляем приращение координат текстуры
	     // при переходе от одной вершины к другой.
	     float uCoordIncrementSize = 1.0f / (float)numCellsPerRow;
	     float vCoordIncrementSize = 1.0f / (float)numCellsPerCol;

    
	     int i = 0;
	     for(int z = startZ; z >= endZ; z -= cellSpacing)
	     {

	          int j = 0;
	          for(int x = startX; x <= endX; x += cellSpacing)
	          {
	               // вычисляем правильный индекс в буфере вершин
	               // и карте высот на основании счетчиков вложенных циклов
	               int index = i * numVertsPerRow + j;
	               vertexTerrain[index*3] = (float)x;//new Vec3((float)x, 0, (float)z);
	               vertexTerrain[index*3+1] = (float)(heigthMap[index] & 255)*heightScale;
	               vertexTerrain[index*3+2]  = (float)z;
	               
	               int jC = j%2;
	               int iC = i%2;
	               
	               textureCoordinateTerrain[index*2] = (float)(jC==0?0:1);      
	               textureCoordinateTerrain[index*2+1] = (float)(iC==0?0:1);
	               
	             //  textureCoordinateTerrain[index*2] =  j * uCoordIncrementSize;
		         //  textureCoordinateTerrain[index*2+1] = i * vCoordIncrementSize;
	               
	               j++;
	          }
	          i++; // следующая строка
	     }
	     
	     vertexBuffer = floatArrayToFloadBuffer(vertexTerrain);
	     textureCoordinatesBuffer = floatArrayToFloadBuffer(textureCoordinateTerrain);
	     
	}

	
	private void computeIndices()
	{

	     // Индекс, с которого начинается группа из 6 индексов,
	     // описывающая два треугольника, образующих квадрат
	     int baseIndex = 0;

	     // В цикле вычисляем треугольники для каждого квадрата
	     for(int i = 0; i < numCellsPerCol; i++)
	     {
	          for(int j = 0; j < numCellsPerRow; j++)
	          {
	        	  indicesTerrain[baseIndex]     = (short) (i * numVertsPerRow + j);
	        	  indicesTerrain[baseIndex + 1] = (short) (i * numVertsPerRow + j + 1);
	        	  indicesTerrain[baseIndex + 2] = (short) ((i+1) * numVertsPerRow + j);
	        	  indicesTerrain[baseIndex + 3] = (short) ((i+1) * numVertsPerRow + j);
	        	  indicesTerrain[baseIndex + 4] = (short) (i * numVertsPerRow + j + 1);
	        	  indicesTerrain[baseIndex + 5] = (short) ((i+1) * numVertsPerRow + j + 1);

	               // следующий квадрат
	               baseIndex += 6;
	          }
	     }
	     indexBuffer = shortArrayToShortBuffer(indicesTerrain);
	}


	
	private void computeNormals()
	{
	     // В цикле вычисляем треугольники для каждого квадрата
	     for(int i = 0; i <= numCellsPerCol; i+=2)
	     {
	          for(int j = 0; j <= numCellsPerRow; j+=2)
	          {
	        	  if (i==numCellsPerCol) i--;
	        	  if (j==numCellsPerRow) j--;
	        	  
	        	  int index = i * numVertsPerRow + j;
	        	  int x = index*3; int y = index*3+1; int z = index*3+2;
	        	  Vec3 A  = new Vec3( vertexTerrain[ x ], vertexTerrain[ y ], vertexTerrain[ z ] );
	        	  
	        	  index = i * numVertsPerRow + j+1;
	        	  x = index*3; y = index*3+1; z = index*3+2;
	        	  Vec3 B  = new Vec3( vertexTerrain[ x ], vertexTerrain[ y ], vertexTerrain[ z ] );
	        	  
	        	  index = (i+1) * numVertsPerRow + j;
	        	  x = index*3; y = index*3+1; z = index*3+2;
	        	  Vec3 C  = new Vec3( vertexTerrain[ x ], vertexTerrain[ y ], vertexTerrain[ z ] );

	        	  index = (i+1) * numVertsPerRow + j + 1;
	        	  x = index*3; y = index*3+1; z = index*3+2;
	        	  Vec3 D  = new Vec3( vertexTerrain[ x ], vertexTerrain[ y ], vertexTerrain[ z ] );

	        	  Vec3 normalA = Vec3.vec3Normal( Vec3.Vec3vectorProduct ( Vec3.vec3MinusVec3(A, B), Vec3.vec3MinusVec3(A, C) ) );
	        	  Vec3 normalB = Vec3.vec3Normal( Vec3.Vec3vectorProduct ( Vec3.vec3MinusVec3(B, D), Vec3.vec3MinusVec3(B, C) ) );
	        	  Vec3 normalC = Vec3.vec3Normal( Vec3.Vec3vectorProduct ( Vec3.vec3MinusVec3(C, B), Vec3.vec3MinusVec3(C, D) ) );
	        	  Vec3 normalD = Vec3.vec3Normal( Vec3.Vec3vectorProduct ( Vec3.vec3MinusVec3(D, C), Vec3.vec3MinusVec3(D, B) ) );

	        	  
	        	  index = (i * numVertsPerRow + j)*3;

	        	  normalTerrain[index] = normalA.x;
	        	  normalTerrain[index+1] = normalA.y;
	        	  normalTerrain[index+2] = normalA.z;
	        	  
	        	  index = (i * numVertsPerRow + j+1)*3;
	        	  normalTerrain[index] = normalB.x;
	        	  normalTerrain[index+1] = normalB.y;
	        	  normalTerrain[index+2] = normalB.z;
	        	  
	        	  index = ((i+1) * numVertsPerRow + j)*3;
	        	  normalTerrain[index] = normalC.x;
	        	  normalTerrain[index+1] = normalC.y;
	        	  normalTerrain[index+2] = normalC.z;
	        	  
	        	  index = ((i+1) * numVertsPerRow + j + 1)*3;
	        	  normalTerrain[index] = normalD.x;
	        	  normalTerrain[index+1] = normalD.y;
	        	  normalTerrain[index+2] = normalD.z;	
	          }
	     }
	     
	     int j = 0; 
	     for (int i = 0; i < numVertices*3; i+=3)
	     {
	    	 float k=40;
	    	 vertexBufferNormalDraw[j] =  vertexTerrain[i]; vertexBufferNormalDraw[j+1] = vertexTerrain[i+1]; vertexBufferNormalDraw[j+2] = vertexTerrain[i+2];
	    	 vertexBufferNormalDraw[j+3] = vertexTerrain[i]+normalTerrain[i]*k; vertexBufferNormalDraw[j+4] = vertexTerrain[i+1]+normalTerrain[i+1]*k; vertexBufferNormalDraw[j+5] = vertexTerrain[i+2]+normalTerrain[i+2]*k;
	    	 j+=6;
	     }
	     
	     vertexBufferNormalDrawFloatBuffer = floatArrayToFloadBuffer(vertexBufferNormalDraw);
	     normalBuffer = floatArrayToFloadBuffer(normalTerrain);
	}


	public void DrawNormal(float[] viewProjectionMatrix) {
		if (!draw) return;
		
		GLES20.glUseProgram(shaderProgrammNormal);
		// set position
		int aPosition = GLES20.glGetAttribLocation(shaderProgrammNormal, "aPosition");
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBufferNormalDrawFloatBuffer);

		// set matrix Projection * View * Rotation * world
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, viewProjectionMatrix, 0, worldMatrix, 0);
		int mat4x4 = GLES20.glGetUniformLocation(shaderProgrammNormal, "matrix");
		GLES20.glUniformMatrix4fv(mat4x4, 1,false, worldViewProjectionMatrix,0);
		
		GLES20.glLineWidth(3);
	
	//	Log.d("Terrain","normalTerrain.length = "+normalTerrain.length);
		
	    // Draw Cube
	    GLES20.glDrawArrays(GLES20.GL_LINES, 0, numLinesNormal);
	    
	    // Disable attribute
		GLES20.glDisableVertexAttribArray(aPosition);
	}
	


	public void Draw(float[] viewProjectionMatrix, Vec3 cameraPos) {

		if (!draw) return;
		
		GLES20.glUseProgram(shaderProgram);
		// set position
		int aPosition = GLES20.glGetAttribLocation(shaderProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(aPosition);
		GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// set textcoord
		int aTexcoord = GLES20.glGetAttribLocation(shaderProgram, "aTexcoord");
		GLES20.glEnableVertexAttribArray(aTexcoord);
		GLES20.glVertexAttribPointer(aTexcoord, 2, GLES20.GL_FLOAT, false, 0, textureCoordinatesBuffer);
		
		int aNormal = GLES20.glGetAttribLocation(shaderProgram, "aNormal");
		GLES20.glEnableVertexAttribArray(aNormal);
		GLES20.glVertexAttribPointer(aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
		
		// set texture
	    int uTexture = GLES20.glGetUniformLocation(shaderProgram, "uTexture");
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getName());
	    GLES20.glUniform1i(uTexture, 0);	
		
		// set texture 2
	    int uTexture2 = GLES20.glGetUniformLocation(shaderProgram, "uTexture2");
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture2.getName());
	    GLES20.glUniform1i(uTexture2, 1);		    
	    
		// set matrix Projection * View * Rotation * world
		Matrix.multiplyMM(worldViewProjectionMatrix, 0, viewProjectionMatrix, 0, worldMatrix, 0);
		int mat4x4 = GLES20.glGetUniformLocation(shaderProgram, "matrix");
		GLES20.glUniformMatrix4fv(mat4x4, 1,false, worldViewProjectionMatrix,0);
		
		float camPos[] = {cameraPos.x,cameraPos.y,cameraPos.z};
		
		GLES20.glUniform3fv(GLES20.glGetUniformLocation(shaderProgram, "u_camera")  , 1, camPos, 0);
		
		GLES20.glLineWidth(3);
	    // Draw Cube
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, indicesTerrain.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    
	    // Disable attribute
		GLES20.glDisableVertexAttribArray(aPosition);
		GLES20.glDisableVertexAttribArray(aTexcoord);
		GLES20.glDisableVertexAttribArray(aNormal);
		

		
		//DrawNormal(viewProjectionMatrix);
	}
	
	@Override
	public void free() {
		// TODO Auto-generated method stub
		super.free();
		texture2.deleteTexture();
	}
	
}
