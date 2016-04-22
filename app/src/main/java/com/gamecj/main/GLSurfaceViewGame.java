package com.gamecj.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSurfaceViewGame extends GLSurfaceView {
	MainRender renderer;
	public GLSurfaceViewGame(Context context) {
		super(context);
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
		return renderer.onTouchEvent(event);
    }
	@Override
	public void setRenderer(Renderer renderer)
	{
		super.setRenderer(renderer);
		this.renderer = (MainRender) renderer;
	}

	@Override
	public void onPause() {
		super.onPause();
		//renderer.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	//	renderer.onResume();
	}
	
}
