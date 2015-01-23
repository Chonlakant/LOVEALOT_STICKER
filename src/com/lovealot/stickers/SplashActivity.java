package com.lovealot.stickers;

import java.io.File;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity  {

	private static final int WAIT_TIME = 3000;
	private Timer waitTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);	
		// Animation animation = AnimationUtils.loadAnimation(this,
		// R.anim.splash);
		// animation.setAnimationListener(this);
		// findViewById(R.id.imgSplash);
		// Intent intent = new Intent(SplashActivity.this, SelectPhoto.class);
		// SplashActivity.this.startActivity(intent);
		// SplashActivity.this.finish();
		File directory = new File(Environment.getExternalStorageDirectory() + "/LoveaLot");
		if(!directory.exists() && !directory.isDirectory()) 
	    {
	        // create empty directory
	        if (directory.mkdirs())
	        {
	            Log.i("CreateDir","App dir created");
	        }
	        else
	        {
	            Log.w("CreateDir","Unable to create app dir!");
	        }
	    }
	    else
	    {
	        Log.i("CreateDir","App dir already exists");
	    }
		
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, SelectPhoto.class));
                finish();
			}
		}, WAIT_TIME);
	}
}
