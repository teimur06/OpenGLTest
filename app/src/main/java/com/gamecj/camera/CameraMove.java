package com.gamecj.camera;

import java.util.ArrayList;
import com.gamecj.vectors.Vec3;



public class CameraMove {

	private ArrayList<Vec3> vectorPos;
	private ArrayList <Vec3> vectorLook;
	private int indexPos, indexLook;
	private Vec3 pos1, pos2, vecNormalPos, look1,look2, vecNormalLook;
	private boolean ifnext;
	private Vec3 speedPos;
	private Vec3 speedLook;
	private float lengthVec3Look,lengthVec3Pos;
	private boolean start;
	private boolean repeat;
	private float radius; // Радиус
	private double angleStart, angleInc; // Угол для начала, угол шага
	private Vec3 point; // Точка в которую смотрит камера во время движения по кругу
	private MoveType moveType;
	private float speedLookmanual;
	private boolean bFlagAddFirstElement;
	private boolean blimitYup, blimitYdown, blimitXright, blimitXleft, blimitZright, blimitZleft;
	private float limitYup, limitYdown, limitXright, limitXleft, limitZright, limitZleft;
	
	public void setblimitYup(boolean b) {this.blimitYup = b;}
	public void setblimitYdown(boolean b) {this.blimitYdown = b;}
	public void setblimitXright(boolean b) {this.blimitXright = b;}
	public void setblimitXleft(boolean b) {this.blimitXleft = b;}
	public void setblimitZright(boolean b) {this.blimitZright = b;}
	
	public void setLimitYup(float limit) {this.limitYup = limit;}
	public void setLimitYdown(float limit) {this.limitYdown = limit;}
	public void setLimitXright(float limit) {this.limitXright = limit;}
	public void setLimitXleft(float limit) {this.limitXleft = limit;}
	public void setLimitZright(float limit) {this.limitZright = limit;}
	public void setLimitZleft(float limit) {this.limitZleft = limit;}
	
	public float getLimitYup(){ return limitYup; }
	public float getLimitYdown(){ return limitYdown; }
	public float getLimitXright(){ return limitXright; }
	public float getLimitXleft(){ return limitXleft; }
	public float getLimitZright(){ return limitZright; }
	public float getLimitZleft(){ return limitZleft; }
	
	
	public static enum MoveType{
		TRAJECTORY,
		CIRCLE_XZ,
		CIRCLE_YZ,
		CIRCLE_XY,
		MANUALCONTROL,
	}

	public CameraMove(MoveType moveType)
	{
		bFlagAddFirstElement = false;
		vectorPos = new ArrayList<Vec3>();
		vectorLook = new ArrayList<Vec3>();
		vecNormalPos = new Vec3();
		vecNormalLook = new Vec3();
		look1 = null;
		look2 = new Vec3(0,0,0);;
		pos1 = null;
		pos2 = new Vec3(0,0,0);		
		ifnext = true;
		start = true;
		repeat = false;
		speedPos = new Vec3(0.03f, 0.03f, 0.03f) ;
		speedLook = new Vec3(0.03f, 0.3f, 0.03f) ;
		indexLook = indexPos = 0;
		point = new Vec3(0f, 0f, 0f) ;
		radius = 0;
		speedLookmanual = 0.001f;
		angleInc = 0;
		angleStart = 0;
		this.moveType = moveType;
		blimitYup = blimitYdown = blimitXright = blimitXleft = blimitZright = blimitZleft = false;
		limitYup = limitYdown = limitXright = limitXleft = limitZright = limitZleft = 0;
	}
	
	// Глобальный метод для следующего шага движения
	public void next()
	{
		switch (moveType){
			case TRAJECTORY:
				if (isEndVector()) return;
				if (start)
				{
					nextVec3Look();
					nextVec3Pos();
					start = false;
				}
				testDistancePos1Pos2();
				testDistanceLock1Look2();
				break;
			
			case CIRCLE_XZ:
				initAngle();
				pos1.x = point.x + radius *(float) Math.cos(angleStart);	
				pos1.y = point.y;
				pos1.z = point.z + radius *(float) Math.sin(angleStart);	;
				angleStart += angleInc;
				break;

				
			case CIRCLE_XY:
				initAngle();
				pos1.x = point.x+ radius *(float) Math.cos(angleStart);	
				pos1.y = point.y + radius *(float) Math.sin(angleStart);
				pos1.z = point.z;	
				angleStart += angleInc;				
				break;
				
			case CIRCLE_YZ:
				initAngle();
				pos1.x = point.x ;	
				pos1.y = point.y + radius *(float) Math.sin(angleStart);
				pos1.z = point.z+ radius *(float) Math.cos(angleStart);	
				angleStart += angleInc;					
				break;				
			
			case MANUALCONTROL:
				
				break;
				
			default:
				break;
		}
	}		

	
	public void addPos(Vec3 v)
	{
		vectorPos.add(v);
		if (pos1 == null) pos1= new Vec3(v);
	}
	
	public void addLook(Vec3 v)
	{
		vectorLook.add(v);
		if (look1 == null) look1= new Vec3(v);
	}
	
	// Установка данных для вращения по кругу: начальный угол, угол для шага, радиус, точка вокруг которой будет происходить вращение
	public void setDataCircle(double angleStart, double angleInc, float radius, Vec3 point)
	{
		this.angleInc = angleInc;
		this.angleStart = angleStart;
		this.radius = radius;
		this.point.setVec3(point);
	}		
	

	
	public void setMoveType(MoveType moveType){ 
		if (this.moveType == moveType) return;
		
		indexPos = indexLook =  0;
	/*	if (moveType == MoveType.TRAJECTORY)
		{
			if (vectorPos.get(0) != pos1)
			{
				vectorPos.add(0, pos1);
			//	vectorLook.add(0, look1);
				vectorPos.add( pos1);
				//vectorLook.add(look1);
			}
		} else if(this.moveType == MoveType.TRAJECTORY)
		{
			vectorPos.remove(vectorPos.size()-1);
			//vectorLook.remove(vectorLook.size()-1);
			vectorPos.remove(0);
			//vectorLook.remove(0);
			
		}*/
		start = true;	
		this.moveType = moveType;
	}
	public MoveType getMoveType() {return moveType;}
	public void setSpeedPos(Vec3 speedPos){ this.speedPos.setVec3(speedPos); }
	public void setSpeedLook(Vec3 speedLook){ this.speedLook.setVec3(speedLook); }
	public void setSpeedLookManual(float speedLookmanual) {this.speedLookmanual = speedLookmanual;}
	public float getX() {return pos1.x;}
	public float getY() {return pos1.y;}
	public float getZ() {return pos1.z;}
	public void setX(float x) {pos1.x = x;}
	public void setY(float y) {pos1.y = y;}
	public void setZ(float z) {pos1.z = z;}
	
	public float getYpos2() {return pos2.y;}
	public void setYpos2(float y) {pos2.y = y;}
	
	public float getLookX() {return look1.x;}
	public float getLookY() {return look1.y;}
	public float getLookZ() {return look1.z;}
	public void setRepeat(boolean repeat){this.repeat = repeat;}
	public boolean isNext(){return ifnext;}	
	
	private void nextVec3Pos()
	{
		if (indexPos+1 >= vectorPos.size()) return;
		pos1 = vectorPos.get(indexPos);
		pos2 = vectorPos.get(indexPos+1);
		
		Vec3 normalPos2 = Vec3.vec3MinusVec3(pos1, pos2);
		normalPos2 = Vec3.vec3Normal(normalPos2);
		normalPos2 = Vec3.Vec3MulVec3(normalPos2,speedPos);
		vecNormalPos = new Vec3(normalPos2);
		lengthVec3Pos = Vec3.lengthVec3(vecNormalPos);
	}
	
	private void testDistancePos1Pos2()
	{
		Vec3 vecLength = Vec3.vec3MinusVec3(pos1, pos2);
		if (Vec3.lengthVec3(vecLength) < lengthVec3Pos)
		{
			indexPos++;
			if (!isEndVector())
				nextVec3Pos();
		}
		
		pos1 = Vec3.vec3SummVec3(vecNormalPos, pos1);
	}	
	
	
	private void nextVec3Look()
	{

		if (indexLook+1 >= vectorLook.size()) return;
		look1 = vectorLook.get(indexLook);
		look2  = vectorLook.get(indexLook+1);
		Vec3 normalLook2 = Vec3.vec3MinusVec3(look1, look2);
		normalLook2 = Vec3.vec3Normal(normalLook2);
		normalLook2 = Vec3.Vec3MulVec3(normalLook2,speedLook);
		vecNormalLook = new Vec3(normalLook2);	
		lengthVec3Look = Vec3.lengthVec3(vecNormalLook);
	}	
	
	private void testDistanceLock1Look2()
	{
		Vec3 vecLength = Vec3.vec3MinusVec3(look1, look2);
		if (Vec3.lengthVec3(vecLength) < lengthVec3Look)
		{
			indexLook++;
			if (!isEndVector())
				nextVec3Look();
		} 		
		look1 = Vec3.vec3SummVec3(vecNormalLook, look1);
	}
	

	private void initAngle()
	{
		if (start)
		{
			nextVec3Look();
			start = false;
		}
		testDistanceLock1Look2();
		
		if (angleStart >= 360) angleStart = 0;
		if (angleStart <= -360) angleStart = 0;
	}
	
	
	private boolean isEndVector()
	{
		if ((indexPos+1 >= vectorPos.size()) && (indexLook+1 >= vectorLook.size()))
		{
			if (!repeat)
			{
			  		ifnext = false ;
			  		return true;
			} 
		}

		if (repeat && (indexLook+1 >= vectorLook.size())) {indexLook = 0;}
		if (repeat && (indexPos+1 >= vectorPos.size()))  {indexPos = 0;}	
		
		return false;
	}
	
	
	public void moveForward()
	{
		if (moveType == MoveType.MANUALCONTROL)goForwardBack(true);
	}
	
	public void moveBack()
	{
		if (moveType == MoveType.MANUALCONTROL) goForwardBack(false);
	}
	
	public void moveRigth()
	{
		if (moveType == MoveType.MANUALCONTROL) setRightLeftPos(true);
	}
	
	public void moveLeft()
	{
		if (moveType == MoveType.MANUALCONTROL) setRightLeftPos(false);
	}
	
	public void moveUp()
	{
		if (moveType == MoveType.MANUALCONTROL) {
			
			pos1.y += speedPos.y;
			if ((pos1.y >= limitYup) && blimitYup) {pos1.y=limitYup; return; }
			look1.y += speedPos.y;
		} 
	}
	
	public void moveDown()
	{
		if (moveType == MoveType.MANUALCONTROL) {
			pos1.y -= speedPos.y;
			if ((pos1.y <= limitYdown) && blimitYdown) {pos1.y+=speedPos.y;	return;	}  
			look1.y -= speedPos.y;
		} 
	}	
	
	public void rotationUp()
	{
		if (moveType == MoveType.MANUALCONTROL) setAngleLookUp(speedLookmanual,false,true);
	}
	
	public void rotationDown()
	{
		if (moveType == MoveType.MANUALCONTROL) setAngleLookUp(-speedLookmanual,false,true);
	}
	
	public void rotationLeft()
	{
		if (moveType == MoveType.MANUALCONTROL) setAngleLookUp(-speedLookmanual,true,false);
	}
	
	public void rotationRight()
	{
		if (moveType == MoveType.MANUALCONTROL) setAngleLookUp(speedLookmanual,true,false);
	}
	
	
	// Перемещение камеры вперед и назад
	private void goForwardBack(boolean forward)
	{
		Vec3 lengthPosLook = Vec3.vec3MinusVec3(pos1, look1);
		Vec3 normalSpeed = Vec3.vec3Normal(lengthPosLook);
		normalSpeed = Vec3.Vec3MulVec3(normalSpeed, speedPos);
		Vec3 vec = null;
		
		if (forward) vec =  Vec3.vec3SummVec3(pos1, normalSpeed);
		else vec = Vec3.vec3MinusVec3(normalSpeed,pos1);
		
		if ((vec.y >= limitYup) && blimitYup) normalSpeed.y=0;
		if ((vec.y <= limitYdown) && blimitYdown) {
			normalSpeed.y=0;
			float posY = limitYdown-pos1.y;
			pos1.y+=posY;
			look1.y +=posY;	
		}
		if (forward) {
			pos1 = Vec3.vec3SummVec3(pos1, normalSpeed);
			look1 = Vec3.vec3SummVec3(look1, normalSpeed);
		}
		else {	
			pos1 = Vec3.vec3MinusVec3(normalSpeed,pos1);
			look1 = Vec3.vec3MinusVec3(normalSpeed,look1);
		}
	}
	
	// Перемещение камеры в право и в лево
	private void setRightLeftPos(boolean right)
	{
		Vec3 lengthPosLook = Vec3.vec3MinusVec3(pos1, look1);
		Vec3 newLook = new Vec3(lengthPosLook);
		double angle ;
		if (right) angle = -Math.PI/2; else angle = Math.PI/2;
		float x = newLook.x* (float) Math.cos(angle) - newLook.z *(float) Math.sin(angle);
		newLook.y = 0;
		float z =  newLook.x * (float) Math.sin(angle) + newLook.z * (float) Math.cos(angle); 
		newLook.x = x; newLook.z = z;
		Vec3 normalSpeed = Vec3.vec3Normal(newLook);
		normalSpeed = Vec3.Vec3MulVec3(normalSpeed, speedPos);
		
		Vec3 vec = Vec3.vec3SummVec3(pos1, normalSpeed);
		if ((vec.y >= limitYup) && blimitYup) normalSpeed.y=0;
		if ((vec.y <= limitYdown) && blimitYdown) {
			normalSpeed.y=0;
			float posY = limitYdown-pos1.y;
			pos1.y+=posY;
			look1.y +=posY;	
		}
		
		pos1 = Vec3.vec3SummVec3(pos1, normalSpeed);
		look1 = Vec3.vec3SummVec3(look1, normalSpeed);

	}
	
	// Поворот камеры 
	private void setAngleLookUp(double angle, boolean right, boolean up)
	{
	    // Получаю вектор куда смотрит камера
	    Vec3 localLookCoord =  Vec3.vec3MinusVec3(pos1, look1);
	    
	     // Определители точки куда смотрит камера в сферической системе координат
	    float r, y, o , rXZ;
	    // Перевожу из декартовой системы координат в сферическую
	    r = (float)  Math.sqrt(localLookCoord.x*localLookCoord.x + localLookCoord.y*localLookCoord.y+ localLookCoord.z*localLookCoord.z  );
	    rXZ  = (float) Math.sqrt(localLookCoord.x*localLookCoord.x +localLookCoord.z*localLookCoord.z);
	    y =  (float)  Math.acos(localLookCoord.x / rXZ);
	    o =  (float)  Math.asin(localLookCoord.y / r);

	    
	    if( localLookCoord.z>0)  y = 2 *(float)  Math.PI - y;
	    
	    // Изменяем углы
	    if (up) 
	    {
	    	o += angle;
	    	if (o < 0.1-Math.PI/2 ) o = 0.1f-(float) Math.PI/2;
	    	if (o > Math.PI/2 -0.1) o = (float) Math.PI/2 -0.1f;
	    }
	    if (right) y += angle;


	    
	    // Перевожу из сферической системы координат в декартовою
	    localLookCoord.x = (float) (r* Math.cos(o) * Math.cos(y));
	    localLookCoord.y = (float) (r* Math.sin(o));
	    localLookCoord.z = (float) -(r* Math.cos(o) * Math.sin(y));
	    
	    
	    localLookCoord = Vec3.vec3Normal(localLookCoord);
	    
	    // Устанавливаю координаты направления взгляда камеры
	    look1= Vec3.vec3SummVec3(pos1, localLookCoord);		
	    
	    
	}
}
