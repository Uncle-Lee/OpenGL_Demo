package com.will.airhockey;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AirHockeyActivity extends AppCompatActivity {
	
	private GLSurfaceView mGLSurfaceView;
	private boolean rendererSet = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new GLSurfaceView(this);
		mGLSurfaceView.setEGLContextClientVersion(2);
		mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
		rendererSet = true;
		setContentView(mGLSurfaceView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (rendererSet) {
			mGLSurfaceView.onResume();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (rendererSet) {
			mGLSurfaceView.onPause();
		}
	}
	
}
