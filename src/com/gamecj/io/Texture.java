package com.gamecj.io;


import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture {

    private int name = 0;
    private Context context;

    public Texture(Context context) {
    		this.context = context;
    }
    
    // ����� ������������ ��������� ��������
     //��� ����� ����� �����, ������� ���������� ��� ��������
    public int getName() {
            return name;
    }
    
  
    
    public boolean createTextureCube(int idpicture, 
    		                      int idpicture2, 
    		                      int idpicture3,
    		                      int idpicture4,
    		                      int idpicture5,
    		                      int idpicture6,
    		                      BitmapFactory.Options option )
    {
    	int []names = new int[1];
    	GLES20.glGenTextures(1, names, 0);
    	name = names[0];
    	if (name == 0) {Log.e("Texture", "createTextureCube glGenTextures return 0");return false;}
    	GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, name);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_MIN_FILTER, 
                GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
       //������������� ����� ������� ����������� 
        //���� ���������� �������� ����� �� ������� �� 0 �� 1
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);  
        
        Bitmap bitmap = loadBitmap(idpicture, option);
        if (bitmap == null){Log.e("Texture", "createTextureCube bitmap not loading!"); deleteTexture(); return false;}
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,0, bitmap, 0);
        bitmap.recycle();
        
        bitmap = loadBitmap(idpicture2, option);
        if (bitmap == null){Log.e("Texture", "createTextureCube bitmap 2 not loading!"); deleteTexture();  return false;}
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,0, bitmap, 0);
        bitmap.recycle();
        
        bitmap = loadBitmap(idpicture3, option);
        if (bitmap == null){Log.e("Texture", "createTextureCube bitmap 3 not loading!"); deleteTexture();  return false;}
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,0, bitmap, 0);
        bitmap.recycle();
        
        bitmap = loadBitmap(idpicture4, option);
        if (bitmap == null){Log.e("Texture", "createTextureCube bitmap 4 not loading!"); deleteTexture();  return false;}
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,0, bitmap, 0);
        bitmap.recycle();
        
        bitmap = loadBitmap(idpicture5, option);
        if (bitmap == null){Log.e("Texture", "createTextureCube bitmap 5 not loading!"); deleteTexture();  return false;}
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,0, bitmap, 0);
        bitmap.recycle();
        
        bitmap = loadBitmap(idpicture6, option);
        if (bitmap == null){Log.e("Texture", "createTextureCube bitmap 6 not loading!"); deleteTexture();  return false;}
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,0, bitmap, 0);
        bitmap.recycle();
        
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        return true;
    }
 
    
    
    public boolean createTextureCubeFromAssets(String fileName, 
    		String fileName2, 
    		String fileName3,
    		String fileName4,
    		String fileName5,
    		String fileName6,
            BitmapFactory.Options option )
    {
		int []names = new int[1];
		GLES20.glGenTextures(1, names, 0);
		name = names[0];
		if (name == 0) {Log.e("Texture", "createTextureCubeFromAssets glGenTextures return 0");return false;}
		GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, name);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
		GLES20.GL_TEXTURE_MIN_FILTER, 
		GLES20.GL_LINEAR_MIPMAP_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
		GLES20.GL_TEXTURE_MAG_FILTER,
		GLES20.GL_LINEAR);
		//������������� ����� ������� ����������� 
		//���� ���������� �������� ����� �� ������� �� 0 �� 1
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
		GLES20.GL_TEXTURE_WRAP_S,
		GLES20.GL_REPEAT);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
		GLES20.GL_TEXTURE_WRAP_T,
		GLES20.GL_REPEAT);  
		
		Bitmap bitmap = loadBitmapFromAssets(fileName, option);
		if (bitmap == null){Log.e("Texture", "createTextureCubeFromAssets bitmap not loading!"); deleteTexture();  return false;}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,0, bitmap, 0);
		bitmap.recycle();
		
		bitmap = loadBitmapFromAssets(fileName2, option);
		if (bitmap == null){Log.e("Texture", "createTextureCubeFromAssets bitmap 2 not loading!"); deleteTexture();  return false;}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,0, bitmap, 0);
		bitmap.recycle();
		
		bitmap = loadBitmapFromAssets(fileName3, option);
		if (bitmap == null){Log.e("Texture", "createTextureCubeFromAssets bitmap 3 not loading!"); deleteTexture();  return false;}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,0, bitmap, 0);
		bitmap.recycle();
		
		bitmap = loadBitmapFromAssets(fileName4, option);
		if (bitmap == null){Log.e("Texture", "createTextureCubeFromAssets bitmap 4 not loading!"); deleteTexture();  return false;}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,0, bitmap, 0);
		bitmap.recycle();
		
		bitmap = loadBitmapFromAssets(fileName5, option);
		if (bitmap == null){Log.e("Texture", "createTextureCubeFromAssets bitmap 5 not loading!"); deleteTexture();  return false;}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,0, bitmap, 0);
		bitmap.recycle();
		
		bitmap = loadBitmapFromAssets(fileName6, option);
		if (bitmap == null){Log.e("Texture", "createTextureCubeFromAssets bitmap 6 not loading!"); deleteTexture();  return false;}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,0, bitmap, 0);
		bitmap.recycle();
		
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
		return true;
    }    
    
    
    
    public boolean createTexture2D(int idpicture, BitmapFactory.Options option)
    {
        //������� ������ ������ �� ������ ��������
        //� ���� ������ OpenGL ES ������� ��������� ����� ��������, 
        // ������� �������� ������ ��������
        int []names = new int[1];
        // �������� ��������� ��� ��������, ������� ����� �������� � names[0]
        GLES20.glGenTextures(1, names, 0);
        //�������� ��� �������� � ��������� ���� ������
        name = names[0];
        if (name == 0) {Log.e("Texture", "createTexture2D glGenTextures return 0");return false;}
        
        //������ �� ����� ���������� � �������� �� �� ����� name
        //������������� ����� ������������ �� �����
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
        //������ �������� � ������ name �������
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, name);
        //������������� ������� ��������
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, 
                GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
       //������������� ����� ������� ����������� 
        //���� ���������� �������� ����� �� ������� �� 0 �� 1
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);
        // ��������� �������� � Bitmap �� �������
        Bitmap bitmap = loadBitmap(idpicture, option);
        if (bitmap == null){Log.e("Texture", "createTexture2D bitmap not loading!"); deleteTexture();  return false;}
        //������������ Bitmap � ������ ����������
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        // ������� Bitmap �� ������, �.�. �������� ��� ���������� � �����������
        bitmap.recycle();
        bitmap = null;
        // ������ ������ ! 
        // ��������� ������� ����� ������
        // ����� �������� �������� � �����������
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        return true;
    }
    
    
    public boolean createTexture2DFromAssets(String fileName, BitmapFactory.Options option)
    {
        //������� ������ ������ �� ������ ��������
        //� ���� ������ OpenGL ES ������� ��������� ����� ��������, 
        // ������� �������� ������ ��������
        int []names = new int[1];
        // �������� ��������� ��� ��������, ������� ����� �������� � names[0]
        GLES20.glGenTextures(1, names, 0);
        //�������� ��� �������� � ��������� ���� ������
        name = names[0];
        if (name == 0) {Log.e("Texture", "createTexture2DFromAssets glGenTextures return 0");return false;}
        
        //������ �� ����� ���������� � �������� �� �� ����� name
        //������������� ����� ������������ �� �����
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
        //������ �������� � ������ name �������
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, name);
        //������������� ������� ��������
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, 
                GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
       //������������� ����� ������� ����������� 
        //���� ���������� �������� ����� �� ������� �� 0 �� 1
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);
        // ��������� �������� � Bitmap �� �������
        Bitmap bitmap = loadBitmapFromAssets(fileName,null);
        if (bitmap == null){Log.e("Texture", "createTexture2DFromAssets bitmap not loading!"); deleteTexture();  return false;}
        //������������ Bitmap � ������ ����������
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        // ������� Bitmap �� ������, �.�. �������� ��� ���������� � �����������
        bitmap.recycle();
        bitmap = null;
        // ������ ������ ! 
        // ��������� ������� ����� ������
        // ����� �������� �������� � �����������
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        return true;
    }
    
    private Bitmap loadBitmapFromAssets(String fileName, BitmapFactory.Options option)
    {
    	 Bitmap bitmap = null;
    		try {
    	    	// �������� ������� �����
    	    	InputStream ims = context.getAssets().open(fileName);
    	    	if (option != null)
    	        	bitmap = BitmapFactory.decodeStream(ims);
    	        else
    	        	bitmap = BitmapFactory.decodeStream(ims,null,option);
    		}
    		catch(IOException ex) {
    			return null;
    		}
        return bitmap;
    }
    
    private Bitmap loadBitmap(int idpicture, BitmapFactory.Options option)
    {
    	 Bitmap bitmap;
        if (option != null)
        	bitmap = BitmapFactory.decodeResource(context.getResources(), idpicture, option);
        else
        	bitmap = BitmapFactory.decodeResource(context.getResources(), idpicture);
        return bitmap;
    }
    
    public void deleteTexture()
    {
    	int i[] = new int[1];
    	i[0] = name;
    	GLES20.glDeleteTextures(1, i, 0);
    }
}
