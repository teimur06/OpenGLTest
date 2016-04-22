package com.gamecj.object2d;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.gamecj.camera.CameraMove;
import com.gamecj.io.Texture;
import com.gamecj.object3d.Object3D;

public class Arrow extends Button2D {

	public static enum typeCameraMove{
		MOVE_FORWARD,
		MOVE_BACK,
		MOVE_RIGHT,
		MOVE_LEFT,
		MOVE_UP,
		MOVE_DOWN,
		ROTATAION_LEFT,
		ROTATAION_RIGHT,
		ROTATAION_UP,
		ROTATAION_DOWN,
	}
	
	private typeCameraMove currcentTypeCameraMove;
	private CameraMove cameraMove;

	
	final float coord_x = 1.0f;
	final float coord_y = 1.0f;
	final float coord_z = 0.0f;
	private float vertices[] = {
			-coord_x, coord_y, coord_z,  //0    
			coord_x, coord_y, coord_z,  //1
			-coord_x, -coord_y, coord_z,  //2
			coord_x, -coord_y, coord_z,  //3
	};

	private short index[] = {0,1,2,2,1,3, };
	private float texCordUP[] = {0,0,1,0,0,1,1,1,};	
	private float texCordDOWN[] = {	1,1,0,1,1,0,0,0,};	
	private float texCordLEFT[] = {1,0,1,1,0,0,0,1,};	
	private float texCordRIGHT[] = {0,1,0,0,1,1,1,0,};		

	
	public Arrow(Context context,typeCameraMove currcentTypeCameraMove, CameraMove cameraMove) {
		super(context,"arrow/vertexShader.shader","arrow/fragmentShader.shader");
		initArrow(currcentTypeCameraMove,cameraMove);
	}
	
	public Arrow(Context context) {
		super(context,"arrow/vertexShader.shader","arrow/fragmentShader.shader");
	}


	
	public void initArrow(typeCameraMove currcentTypeCameraMove, CameraMove cameraMove)
	{
		this.currcentTypeCameraMove = currcentTypeCameraMove;
		this.cameraMove = cameraMove;
	//	texture = new Texture(context);
	//	texture.createTexture2DFromAssets("texture/arrow.png",null);
		vertexBuffer =floatArrayToFloadBuffer(vertices);
		indexBuffer = shortArrayToShortBuffer(index);
    	fColor = fColorRed;
		
	  switch (currcentTypeCameraMove) {
		case MOVE_FORWARD:
		case ROTATAION_UP:
		case MOVE_UP:
			textureCoordinatesBuffer = floatArrayToFloadBuffer(texCordUP);
			break;
			
		case MOVE_BACK:
		case ROTATAION_DOWN:
		case MOVE_DOWN:
			textureCoordinatesBuffer = floatArrayToFloadBuffer(texCordDOWN);
			break;
			
		case ROTATAION_LEFT:
		case MOVE_LEFT:	
			textureCoordinatesBuffer = floatArrayToFloadBuffer(texCordLEFT);
			break;
			
		case ROTATAION_RIGHT:
		case MOVE_RIGHT:
			textureCoordinatesBuffer = floatArrayToFloadBuffer(texCordRIGHT);
			break;					

		default:
			break;
		}		
	}
		  
	
	
	  @Override
	  public boolean OnDown(float x, float y)
	  {
		  boolean res = super.OnDown(x, y);
		  if (res)
		  {
			  	  
				  switch (currcentTypeCameraMove) {
				case MOVE_FORWARD:
					cameraMove.moveForward();
					break;
					
				case MOVE_BACK:
					cameraMove.moveBack();
					break;

				case MOVE_LEFT:
					cameraMove.moveLeft();
					break;
					
				case MOVE_UP:
					cameraMove.moveUp();
					break;

				case MOVE_DOWN:
					cameraMove.moveDown();
					break;					
					
				case MOVE_RIGHT:
					cameraMove.moveRigth();
					break;					
					
				case ROTATAION_UP:
					cameraMove.rotationUp();
					break;
					
				case ROTATAION_DOWN:
					cameraMove.rotationDown();
					break;
					
				case ROTATAION_LEFT:
					cameraMove.rotationLeft();
					break;
					
				case ROTATAION_RIGHT:
					cameraMove.rotationRight();
					break;					

				default:
					break;
				}
		  } 
		  return res;
	  }
	  

}
