package com.example.mediaservice;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.os.IBinder;
import android.widget.Button;


public class MainActivity extends Activity
	implements 
		Button.OnClickListener,
		ServiceConnection
	{
	private boolean mIsBound = false;
	private MusicService mServ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent music = new Intent(this, MusicService.class);
		startService(music);
		
		doBindService();
		
		findViewById(R.id.btn_play).setOnClickListener(this);
		findViewById(R.id.btn_pause).setOnClickListener(this);
		findViewById(R.id.btn_stop).setOnClickListener(this);
	}
	
	public void onServiceConnected(ComponentName name, IBinder binder)
	{
		mServ = ((MusicService.ServiceBinder) binder).getService();
	}
	
	public void onServiceDisconnected(ComponentName name)
	{
		mServ = null;
	}
	
	public void doBindService()
	{
 		Intent intent = new Intent(this, MusicService.class);
		bindService(intent, this, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}
	
	public void doUnbindService()
	{
		if(mIsBound)
		{
			unbindService(this);
      		mIsBound = false;
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		doUnbindService();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.btn_play:
				
				mServ.start();
				break;
				
			case R.id.btn_pause:
				mServ.pause();
				break;
				
			case R.id.btn_stop:
				mServ.stop();
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
