package com.gamecj.main;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.gamecj.camera.CameraMove;
import com.gamecj.io.Texture;
import com.gamecj.object2d.Arrow;
import com.gamecj.object2d.Button2D;
import com.gamecj.object2d.TextGl;
import com.gamecj.object3d.Fon;
import com.gamecj.object3d.Line3D;
import com.gamecj.object3d.Object3D;
import com.gamecj.object3d.Obj3d;
import com.gamecj.object3d.Terrain;
import com.gamecj.vectors.Vec3;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

public class MainRender implements Renderer {

	private Line3D line3D;
	private Obj3d obj3d;
	private Fon fon;
	private Arrow arrows[];
	private TextGl textloading;
	private Button2D button2d;
	private CameraMove cameraMove;
	private Terrain terrain;
	
	
	private MotionEvent event;
	private long oldTimeFPS;
	private long oldTimeCameraMove;
	private long oldTimeOnclick;
	private Context context;
	private float projectionMatrix[];
	private float projectionMatrix2D[];
	private float viewMatrix[];
	private float viewProjectionMatrix[];
	private int fps, colLoad, m_shadowMap, m_fbo;
	private int lengthArrowType;
	

	private TextGl textArray[];
	
	private float k2D;
	private int width, height;
	private boolean bDown, bTimeOnclick;

	private enum DrawMode{
		DRAWMODE_NULL,
		DRAWMODE_LOAD,
		DRAWMODE_GAME
	}
	
	
	private GLSurfaceViewGame gLSurfaceViewGame;
	private DrawMode drawmode;
	private float ratio;
	
	
	public MainRender(Context context,GLSurfaceViewGame gLSurfaceViewGame) {
		this.context = context;
		this.gLSurfaceViewGame = gLSurfaceViewGame;
		bDown = false;
	}
	
	public void init()
	{
		colLoad = 0;
		lengthArrowType = Arrow.typeCameraMove.values().length;
		arrows = new Arrow[ lengthArrowType ];

		terrain = null;
		obj3d = null;
		event = null;
		line3D = null;
		fon = null;
		textArray = null;
		textloading = null;	

		
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		init();
		projectionMatrix = new float[16];
		projectionMatrix2D = new float[16];
		viewProjectionMatrix =  new float[16];
		viewMatrix =  new float[16];
		fps = 60;
		
		textloading = new TextGl(context);
		textloading.loadTexture();
		textloading.setUniformscale(2f);
		textloading.setColorText(0.2f, 0.2f, 0.6f, 1);	
		textloading.setText("Загрузка");
		textloading.setPos(0.85f, 1.0f, 0);
		bTimeOnclick = true;
		drawmode = DrawMode.DRAWMODE_LOAD;
		Log.d("MainRender","onSurfaceCreated");
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		   GLES20.glViewport(0, 0, width, height);
	       ratio = (float) width / height;
	       float k=0.055f;
	       float left = k*ratio;
	       float right = -k*ratio;
	       float bottom = -k;
	       float top = k;
	       float near = 0.1f;
	       float far = 10000.0f;
	       // получаем матрицу проекции
	      Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
	      k2D=20;
	      Matrix.orthoM(projectionMatrix2D, 0, -k2D*ratio, k2D*ratio, -k2D,k2D, -1, 1); 
	      Log.d("MainRender","onSurfaceChanged");
	}
	
	
	private void createObjects()
	{
		oldTimeFPS = oldTimeCameraMove = oldTimeOnclick = System.currentTimeMillis();

		line3D = new Line3D(context);
		fon = new Fon(context);
		fon.setScaleXYZ(10);
	
		//obj3d = new Obj3d(context);
		terrain = new Terrain(context);
		button2d = new Button2D(context);
		button2d.setScaleXY(1);
		button2d.createButton(1, 1, "texture/camera.png");
		
		cameraMove = new CameraMove(CameraMove.MoveType.MANUALCONTROL);
		cameraMove.setRepeat(true);
		cameraMove.setblimitYdown(true);
		
		Texture texture = new Texture(context);
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 2;
	    texture.createTexture2DFromAssets("texture/font.png", options);

		textArray = new TextGl[4];
		float f = 0;
		for (int i=0;i<textArray.length; i++)
		{
			textArray[i] = new TextGl(context);
			textArray[i].setTexture(texture);
			textArray[i].setPos(0, f, 0);
			f+=0.05f;
		}
		
		
		texture = new Texture(context);
		texture.createTexture2DFromAssets("texture/arrow.png",null);
		Arrow.typeCameraMove tc[] = Arrow.typeCameraMove.values();
		for (int i=0;  i < lengthArrowType; i++) 
		{
			arrows[i] = new Arrow(context, tc[i] , cameraMove);
			arrows[i].setScaleXY(3.0f); 
			arrows[i].setTexture(texture);
		}
	
	}
	
	private void setObjectsPos()
	{
		arrows[0].setPos(28,-3);
		arrows[1].setPos(28,-14);

		arrows[2].setPos(20, -9);
		arrows[3].setPos(8, -9);
		arrows[4].setPos(14,-3);
		arrows[5].setPos(14,-14);
		
		arrows[6].setPos(-26,-9);
		arrows[7].setPos(-14,-9);
		arrows[8].setPos(-20,-3);
		arrows[9].setPos(-20,-14);
	
		button2d.setPos(0, -14);

	//	obj3d.setPos(0, 0, 0);
		
		cameraMove.setDataCircle(0,0.01,10,new Vec3(-6,4,0));
		cameraMove.setSpeedPos(new Vec3(1f,1f,1f));
		cameraMove.setSpeedLook(new Vec3(1f,1f,1f));
		cameraMove.setSpeedLookManual(0.04f);
		
		cameraMove.addPos(new Vec3(255,100,-255)); 
		cameraMove.addPos(new Vec3(-255,100,-255)); cameraMove.addLook(new Vec3(-255,80,-255));
		cameraMove.addPos(new Vec3(-255,100,255));  cameraMove.addLook(new Vec3(-255,80,255));
		cameraMove.addPos(new Vec3(255,100,255));   cameraMove.addLook(new Vec3(255,80,255));
		cameraMove.addPos(new Vec3(255,100,-255));  cameraMove.addLook(new Vec3(255,80,-255));
													cameraMove.addLook(new Vec3(-255,80,-255));

		cameraMove.next();
	}
		
	private void drawLoading()
	{
	//	drawmode = DRAWMODE_NULL;
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_STENCIL_TEST);
		GLES20.glClearColor(0,0,0, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);	
		textloading.Draw(projectionMatrix2D);
		Log.d("MainRender","drawLoading");
		
		if (colLoad == 15)
			gLSurfaceViewGame.queueEvent(new Runnable() {
				@Override
				public void run() {
					textloading.free();
					textloading = null;	
					createObjects();
					setObjectsPos();
					drawmode = DrawMode.DRAWMODE_GAME;
				}
			});
	}

	
	
	private void genTexture()
	{
		// Создаем новую текстуру
		int i[] = new int[1];
		GLES20.glGenTextures(1, i,0);
		m_shadowMap = i[0];
		if (m_shadowMap == 0) Log.e("genTexture","m_shadowMap = "+m_shadowMap);
		// Связываю текстуру  с 2D текстурой
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_shadowMap);
		// заполняю текстуру в формате буфера глубены
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, width, height, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_INT, null);
		// Фильтры
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		// Cоздаем буффер кадра
		GLES20.glGenFramebuffers(1, i, 0);
		m_fbo = i[0];
		if (m_fbo == 0) Log.e("genTexture","m_fbo = "+m_fbo);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, m_fbo);
		
		//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		// Указываю буфферу кадра что бы рисовал в текстуру только буффер глубены
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, m_shadowMap, 0);
		
		
		int Status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
		 
		if (Status != GLES20.GL_FRAMEBUFFER_COMPLETE ) {
			Log.e("genTexture","GL_FRAMEBUFFER error: "+Status);
			
		}
		
		
		
	}
	
	
	private void testTouch(){
		// Обработка нажатия пальца на экран
		if (bDown)
		{
			int  pointerCount = event.getPointerCount();
			for (int i=0; i < pointerCount; i++)
			{
				try
				{
					float x = convertWindowXtoOpenglX(event.getX(i));
					float y = convertWindowYtoOpenglY(event.getY(i));
					if (button2d.OnDown(x, y) && bTimeOnclick)
					{
						if (cameraMove.getMoveType() == CameraMove.MoveType.MANUALCONTROL)
							cameraMove.setMoveType(CameraMove.MoveType.TRAJECTORY);
						else cameraMove.setMoveType(CameraMove.MoveType.MANUALCONTROL);
						bTimeOnclick = false;
					}
					if (cameraMove.getMoveType() == CameraMove.MoveType.MANUALCONTROL) for (Arrow arrow: arrows) arrow.OnDown(x, y);
				} catch (IllegalArgumentException e){}
			}
		}		
	}
	
	private void setCamera(){
		// Передвижения камеры
		if 	(cameraMove.isNext() )
		{
			float yTerrain = terrain.getHeight(cameraMove.getX(), cameraMove.getZ());
			
			textArray[1].setText("Высота ландшафта: "+ yTerrain);
			cameraMove.setLimitYdown(yTerrain+1);
			
			textArray[2].setText("Позиция камеры: x:"+ cameraMove.getX()+"  y:"+cameraMove.getY()+"  z:"+cameraMove.getZ());
			textArray[3].setText("Смотрит: x:"+ cameraMove.getLookX()+"  y:"+cameraMove.getLookY()+"  z:"+cameraMove.getLookZ());
			
			Matrix.setLookAtM(viewMatrix,0, cameraMove.getX() ,cameraMove.getY(),cameraMove.getZ(),    cameraMove.getLookX() ,cameraMove.getLookY(),cameraMove.getLookZ() ,   0, 1, 0);
			fon.setPos(cameraMove.getX() ,cameraMove.getY(),cameraMove.getZ());
		}
		Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix , 0,viewMatrix, 0);
	}
	
	private void testTimers(){
		long currenttime = System.currentTimeMillis();
		if ((currenttime - oldTimeCameraMove) >= 10)
		{
			oldTimeCameraMove = System.currentTimeMillis();
			cameraMove.next();
		}
		
		currenttime = System.currentTimeMillis();
		if ((currenttime - oldTimeOnclick) >= 1000)
		{
			oldTimeOnclick = System.currentTimeMillis();
			bTimeOnclick = true;
		}
		
		// Установим FPS
		currenttime = System.currentTimeMillis();
		if ((currenttime - oldTimeFPS) >= 1000)
		{
			oldTimeFPS = System.currentTimeMillis();
			textArray[0].setText("FPS:"+ Integer.toString(fps));
			fps = 0;			
		}
	}
	
	private void drawGame()
	{
		testTimers();
		testTouch();
		setCamera();

		
		// Включаю z буффер
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LESS);
		GLES20.glDepthMask(true);
		GLES20.glClearColor(0.6f, 0.6f, 0.6f, 1.0f);
		GLES20.glClearDepthf(1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);	
		
		// Рисуем 3D объекты
		line3D.Draw(viewProjectionMatrix);
		GLES20.glDepthMask(false);
		fon.Draw(viewProjectionMatrix);
		GLES20.glDepthMask(true);
		Vec3 camPos = new Vec3(viewMatrix[12],viewMatrix[13],viewMatrix[14]);
		terrain.Draw(viewProjectionMatrix,camPos);
		
		// Рисуем 2D объекты
		GLES20.glDepthMask(false);
		for (TextGl temp: textArray) temp.Draw(projectionMatrix2D);
		if (cameraMove.getMoveType() == CameraMove.MoveType.MANUALCONTROL) for (Arrow arrow: arrows) arrow.Draw(projectionMatrix2D);
		button2d.Draw(projectionMatrix2D);
		GLES20.glDepthMask(true);
		
		fps++;
		
		// Обработка отпускания пальца от экрана
		if (cameraMove.getMoveType() == CameraMove.MoveType.MANUALCONTROL) for (Arrow arrow: arrows) arrow.onUp();
		button2d.onUp();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		switch (drawmode){
		case DRAWMODE_NULL:
			break;
			
		case DRAWMODE_LOAD:
			if (colLoad <= 15)drawLoading();
			colLoad++;
			break;

		case DRAWMODE_GAME:
			drawGame();
			break;			
		}
	}

	
	private float convertWindowXtoOpenglX(float x)
	{
		return x*k2D*ratio*2/width -k2D*ratio;
	}
	
	private float convertWindowYtoOpenglY(float y)
	{
		return -(y*k2D*2/height -k2D);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	
    	this.event = event;
		switch (event.getActionMasked()) {
	    case MotionEvent.ACTION_DOWN: // нажатие
	    	bDown = true;
	      break;
	    case MotionEvent.ACTION_MOVE: // движение
	      break;
	    case MotionEvent.ACTION_UP: // отпускание
	    case MotionEvent.ACTION_CANCEL:  
	    	bDown = false;
	      break;
	    case MotionEvent.ACTION_POINTER_UP:
	    	break;
	    }
		return true;
	}
	
	protected void onPause() {
		drawmode = DrawMode.DRAWMODE_NULL;
		for (Arrow arrow: arrows) freeObjects( arrow );
		freeObjects(line3D);
		freeObjects(obj3d);
		freeObjects(terrain);
		freeObjects(fon);
		freeObjects(textloading);
		for (TextGl temp: textArray) freeObjects(temp);
		Log.i("MainRender", "onPause");
	}

	protected void freeObjects(Object3D o)
	{
		if (o != null)
		{
			o.free();
			o = null;
		}
	}
}
