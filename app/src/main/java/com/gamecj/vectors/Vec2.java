package com.gamecj.vectors;

public class Vec2 {
	 public float x;
	 public float y;
	 
	 public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	 }
	 
	 public Vec2(Vec2 v) {
		 this.x = v.x;
		 this.y = v.y;
	 }
	 
	 public Vec2() {
		 this.x = 0;
		 this.y = 0;
	 }
	 
	 public void setVec2(Vec2 v) {
		 this.x = v.x;
		 this.y = v.y;
	 } 
	 
	 public void setVec2(float x, float y) {
			this.x = x;
			this.y = y;
	 } 

	 public static Vec2 vec2SummVec2(Vec2 v1, Vec2 v2)
	 {
	 	return new Vec2(v2.x+v1.x, v2.y+v1.y);
	 }

   public static Vec2 vec2MinusVec2(Vec2 v1, Vec2 v2)
   {
   	return new Vec2(v2.x - v1.x, v2.y - v1.y);
   }
   
   public static Vec2 vec2MulScalar(Vec2 v, float f)
   {
   	return new Vec2(v.x *f, v.y *f);
   }

	public static float lengthVec2(Vec2 v)
	{
		return (float) Math.sqrt(  v.x*v.x + v.y*v.y );
	}
	
	public static Vec2 Vec3MulVec2(Vec2 v1, Vec2 v2)
	{
		Vec2 res = new Vec2();
		res.x = v1.x * v2.x;
		res.y = v1.y * v2.y;
		return res;
	}
	
	public static Vec2 vec2Normal(Vec2 v)
	{
		float lengthVec3 = lengthVec2(v);
		Vec2 ret = new Vec2(v);
		
		ret.x /= lengthVec3;
		ret.y /= lengthVec3;
		return ret;
	}
}
