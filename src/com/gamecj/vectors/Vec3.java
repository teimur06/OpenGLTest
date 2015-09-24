package com.gamecj.vectors;

import android.util.Log;

public class Vec3 {
	 public float x;
	 public float y;
	 public float z;
	 
	 public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	 }
	 
	 public Vec3(Vec3 v) {
		 this.x = v.x;
		 this.y = v.y;
		 this.z = v.z;
	 }
	 
	 public Vec3() {
		 this.x = 0;
		 this.y = 0;
		 this.z = 0;
	 }
	 
	 public void setVec3(Vec3 v) {
		 this.x = v.x;
		 this.y = v.y;
		 this.z = v.z;
	 } 
	 
	 public void setVec3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
	 } 
 
	 public static Vec3 vec3SummVec3(Vec3 v1, Vec3 v2)
	 {
	 	return new Vec3(v2.x+v1.x, v2.y+v1.y, v2.z+v1.z);
	 }
 
    public static Vec3 vec3MinusVec3(Vec3 v1, Vec3 v2)
    {
    	return new Vec3(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }
    
    public static Vec3 vec3MulScalar(Vec3 v, float f)
    {
    	return new Vec3(v.x *f, v.y *f, v.z *f);
    }
 
	public static float lengthVec3(Vec3 v)
	{
		float t = v.x*v.x + v.y*v.y + v.z*v.z;
		if (t==0.0f) return 0;

		return (float) Math.sqrt(  t );
	}
	
	public static Vec3 Vec3MulVec3(Vec3 v1, Vec3 v2)
	{
		Vec3 res = new Vec3();
		res.x = v1.x * v2.x;
		res.y = v1.y * v2.y;
		res.z = v1.z * v2.z;
		return res;
	}
	
	public static Vec3 Vec3vectorProduct(Vec3 v1, Vec3 v2)
	{
		Vec3 res = new Vec3();
		res.x = v1.y * v2.z - v1.z * v2.y;
		res.y = v1.z * v2.x - v1.x * v2.z;
		res.z = v1.x * v2.y - v1.y * v2.x;
		return res;
	}
	
	public static Vec3 vec3Normal(Vec3 v)
	{
		float lengthVec3 = lengthVec3(v);
		if (lengthVec3 == 0.0f) return new Vec3();
		Vec3 ret = new Vec3(v);
		ret.x /= lengthVec3;
		ret.y /= lengthVec3;
		ret.z /= lengthVec3;
		return ret;
	}


}
