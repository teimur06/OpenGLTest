package com.gamecj.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

public class Obj3DParser {
	private String fileObj3dName;
	private String patchLib;
	private enum typeObject{
		VERTEX,
		NORMAL,
		TEXTCOORD,
		TEXTURENAME,
		INDEX,
		MTLLIB,
		UNKNOWN,
	}
	
	private enum typeF{
		VERTEX,
		VERTEX_TEXTCOORD,
		VERTEX_TEXTCOORD_NORMAL,
		VERTEX_NORMAL,
		UNKNOWN,
	}
	
	private class Vec3
	{
		public float x,y,z;
	}
	
	private class Vec2
	{
		public float u,v;
	}
	
	private ArrayList < Vec3 >  vertex;
	private ArrayList < Vec3 >  normal;
	private ArrayList < Vec2 >  texcoord;
	
	private ArrayList <Float> OutVertex;
	private ArrayList <Float> OutNormal;
	private ArrayList <Float> OutTexcoord;
	private ArrayList < Short >  OutIndex;
		
	private String textureName;
	private float floatVertex[];
	private float floatNormal[];
	private float floatTexcoord[];
	private short floatIndex[];
	
	private Context context;

	private typeObject getTypeObject(String s)
	{
		if (s.contentEquals("v")) return typeObject.VERTEX;
		if (s.contentEquals("vt")) return typeObject.TEXTCOORD;
		if (s.contentEquals("vn")) return typeObject.NORMAL;
		//if (s.contentEquals("usemtl")) return typeObject.TEXTURENAME;
		if (s.contentEquals("map_Kd")) return typeObject.TEXTURENAME;
		if (s.contentEquals("f")) return typeObject.INDEX;
		if (s.contentEquals("mtllib")) return typeObject.MTLLIB;
		return typeObject.UNKNOWN;
	}
	
	private typeF getTypeF(String s)
	{
		Pattern p = Pattern.compile("^[0-9]+/[0-9]+/[0-9]+$");
		if ( p.matcher(s).matches()) return typeF.VERTEX_TEXTCOORD_NORMAL;
		p = Pattern.compile("^[0-9]+//[0-9]+$");
		if ( p.matcher(s).matches()) return typeF.VERTEX_NORMAL;
		p = Pattern.compile("^[0-9]+[0-9]+$");
		if ( p.matcher(s).matches()) return typeF.VERTEX;
		p = Pattern.compile("^[0-9]+/[0-9]+$");
		if ( p.matcher(s).matches()) return typeF.VERTEX_TEXTCOORD;		
		
		return typeF.UNKNOWN;
	}
	
	
	private int setVertex(String s)
	{
		Pattern pVertex = Pattern.compile("^[0-9]+");
		Matcher mVertex = pVertex.matcher(s);
		int indexVertex = 0;

		if (mVertex.find()) 
		{
			indexVertex = Integer.valueOf(mVertex.group());
			OutVertex.add(  vertex.get(indexVertex-1).x  );
			OutVertex.add(  vertex.get(indexVertex-1).y  );
			OutVertex.add(  vertex.get(indexVertex-1).z  );
		}
		
		return indexVertex;
	}
	
	private void setTextureCoord(String s)
	{
		float u,v;
		Pattern pTextCoord = Pattern.compile("/[0-9]+/?");
		Matcher mTextCoord = pTextCoord.matcher(s);
		if (mTextCoord.find())
		{
			String original = mTextCoord.group();
			String group = mTextCoord.group().replaceAll("/", "");
		
			int indexTextCoord = Integer.valueOf(group);
			u = texcoord.get(indexTextCoord-1).u;
			OutTexcoord.add( Float.valueOf(u) );
			v = texcoord.get(indexTextCoord-1).v;
			OutTexcoord.add( Float.valueOf(v) );
		}		
	}
	

	
	private void setNormal(String s)
	{
		float nx,ny,nz;

		Pattern pNormal = Pattern.compile("/[0-9]+$");
		Matcher mNormal = pNormal.matcher(s);
		if (mNormal.find())
		{
			String group = mNormal.group().substring(1, mNormal.group().length()) ;
			int indexNormal = Integer.valueOf(group);
			nx = normal.get(indexNormal-1).x;
			OutNormal.add(  Float.valueOf(nx) );
			ny = normal.get(indexNormal-1).y;
			OutNormal.add(  Float.valueOf(ny) );		
			nz = normal.get(indexNormal-1).z;
			OutNormal.add(  Float.valueOf(nz) );
		}
	}		
		
	private void parseOBJ(String line,int lineNumber) throws Obj3DParcerException, IOException
	{
		Scanner sc = new Scanner(line);
		sc.useLocale(Locale.ROOT);
		while (sc.hasNext())
		{
			Obj3DParser.Vec3 vec3 = new Obj3DParser.Vec3();
			Obj3DParser.Vec2 vec2 = new Obj3DParser.Vec2();
			String s = sc.next();
			switch (getTypeObject(s))
			{
			case VERTEX:
				if (sc.hasNextFloat()) vec3.x = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_VERTEX1,lineNumber,fileObj3dName);
				if (sc.hasNextFloat()) vec3.y = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_VERTEX2,lineNumber,fileObj3dName);
				if (sc.hasNextFloat()) vec3.z = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_VERTEX3,lineNumber,fileObj3dName);
				vertex.add(vec3);
				break;
			
			case NORMAL:
				if (sc.hasNextFloat()) vec3.x = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_NORMAL1,lineNumber,fileObj3dName);
				if (sc.hasNextFloat()) vec3.y = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_NORMAL2,lineNumber,fileObj3dName);
				if (sc.hasNextFloat()) vec3.z = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_NORMAL3,lineNumber,fileObj3dName);
				normal.add(vec3);
				break;
	
			case TEXTCOORD:
				if (sc.hasNextFloat()) vec2.u = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_TEXTCOORD1,lineNumber,fileObj3dName);
				if (sc.hasNextFloat()) vec2.v = sc.nextFloat(); else throw new Obj3DParcerException(Obj3DParcerException.ERROR_TEXTCOORD2,lineNumber,fileObj3dName);
				texcoord.add(vec2);
				break;
				
			case TEXTURENAME:
				if (sc.hasNext()) textureName = sc.next();
				break;	
				
			case INDEX:
				while (sc.hasNext())
				{
					String face = sc.next();
					
					switch (getTypeF( face )) {
					case VERTEX_TEXTCOORD_NORMAL:
						if (setVertex(face)!= 0)
						{
							setTextureCoord(face);
							setNormal(s);
						}
						break;
	
					case VERTEX_NORMAL:
						if (setVertex(face)!= 0)
							setNormal(s);
						break;
						
					case VERTEX_TEXTCOORD:
						if (setVertex(face)!= 0)
							setTextureCoord(face);
						break;
					case VERTEX:
						setVertex(face);
						break;						
					
					case UNKNOWN:
					//	throw new Obj3DParcerException(Obj3DParcerException.ERROR_INDEX,lineNumber,fileObj3dName);
						break;
						
					}
				}
				break;		
			
			case MTLLIB:
				if (sc.hasNext())
				{
					String sFileName = sc.next();
					readFileOBJ(patchLib+"/"+sFileName);
				}
				break;
				
			case UNKNOWN:
				break;
				
			}
		}		
	
		sc.close();
	}
	
	
	private void readFileOBJ(String sFileName) throws Obj3DParcerException, IOException
	{
		InputStream is = null;
		try {
			is = context.getAssets().open(sFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is) );
			String line = "";
			int lineNumber = 0;
			while ((line = br.readLine()) != null)
			{
				lineNumber++;
				parseOBJ(line, lineNumber);
			}
		} catch (IOException e) {
			setNullVectors();
			throw e;
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e){}
			}
		}
	}
	
	private void setNullVectors()
	{
		vertex = null;
		normal = null;
		texcoord = null;
		OutVertex = null;
		OutNormal = null;
		OutTexcoord = null;
		OutIndex = null;
	}

	private void convertVectorShortTo_shortArray(short out[], ArrayList <Short> in)
	{
		int i = 0;
		for (short sh: in) out[i++] = sh;
	}
	
	private void convertVectorFloatTo_floatArray(float out[], ArrayList <Float> in)
	{
		int i = 0;
		for (float f: in) out[i++] = f;
	}
	
	
	// public function
	public Obj3DParser(Context context, String fileName, String patchLib) throws Obj3DParcerException, IOException {
		this.context = context;
		this.patchLib = patchLib;
		fileObj3dName = fileName;
		floatVertex = null;
		floatNormal = null;
		floatTexcoord = null;
		floatIndex = null;
		setNullVectors();
		loadModelFromAssets(fileName);
		 
	}
	
	
	private void loadModelFromAssets(String fileName) throws Obj3DParcerException, IOException {	
		//String sFileName = fileName;
		vertex =  new ArrayList<Obj3DParser.Vec3>();
		normal = new ArrayList<Obj3DParser.Vec3>();
		texcoord = new ArrayList<Obj3DParser.Vec2>();
		OutVertex = new ArrayList<Float>();
		OutNormal = new ArrayList<Float>();
		OutTexcoord = new ArrayList<Float>();
		OutIndex = new ArrayList<Short>();
		
		readFileOBJ(patchLib+"/"+fileName);
		
		floatVertex = new float[OutVertex.size()];
		floatNormal  = new float[OutNormal.size()];
		floatTexcoord  = new float[OutTexcoord.size()];
		floatIndex  = new short[OutIndex.size()];
		convertVectorFloatTo_floatArray(floatVertex, OutVertex);
		convertVectorFloatTo_floatArray(floatNormal, OutNormal);
		convertVectorFloatTo_floatArray(floatTexcoord, OutTexcoord);
		convertVectorShortTo_shortArray(floatIndex, OutIndex);
		setNullVectors();
	}
	
	public float[] getVertex() { return floatVertex;}
	public float[] getNormal() { return floatNormal;}
	public float[] getTexureCoordinates() { return floatTexcoord;}
	public short[] getIndex() { return floatIndex;}
	public String  getTextureName() { return textureName;}

}
