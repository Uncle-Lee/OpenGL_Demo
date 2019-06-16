package com.will.airhockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AirHockeyActivity extends AppCompatActivity {
	
	private GLSurfaceView mGLSurfaceView;
	private boolean rendererSet = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new GLSurfaceView(this);
		
		final ActivityManager activityManager =
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		if (supportsEs2) {
			mGLSurfaceView.setEGLContextClientVersion(2);
			mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
			rendererSet = true;
		} else {
			Toast.makeText(this, "Not support OpenGL2.0", Toast.LENGTH_SHORT).show();
			return;
		}
		
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
