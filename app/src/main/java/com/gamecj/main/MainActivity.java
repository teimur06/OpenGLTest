package com.gamecj.main;

import java.util.Timer;

import com.gamecj.opengltest.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

	private GLSurfaceViewGame mGLSurfaceView;
	private final String LOG = "MainActivity";
	Timer oneTimer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGLSurfaceView = new GLSurfaceViewGame(this);
		loadGame();
		Log.i(LOG,"onCreate");
	}

	
	private void loadGame()
	{

		// Проверяем поддереживается ли OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		if (supportsEs2)
	    {
			
			setContentView(mGLSurfaceView);
			MainRender mainRender = new MainRender(this,mGLSurfaceView);
	        mGLSurfaceView.setEGLContextClientVersion(2);
	        mGLSurfaceView.setEGLConfigChooser(8,8,8,8,16,8);
	        mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888); 
	        mGLSurfaceView.setRenderer(mainRender);
	    }
	    else
	    {
	    	Log.e(LOG, "Устройство не поддерживает OpenGL ES 2");
	        return;
	    }
	}
	
	
	
	@Override
	protected void onResume()
	{
	    // Вызывается GL surface view's onResume() когда активити переходит onResume().
	    super.onResume();
	    mGLSurfaceView.onResume();

	   Log.i(LOG,"onResume");
	}
	 
	@Override
	protected void onPause()
	{
	    // Вызывается GL surface view's onPause() когда наше активити onPause().
	    super.onPause();
	    mGLSurfaceView.onPause();
	    Log.i(LOG,"onPause");
	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(LOG,"onDestroy");
	}
}
