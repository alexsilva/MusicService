package com.example.mediaservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.widget.MediaController;



public class MusicService extends Service 
	implements MediaPlayer.OnErrorListener, 
			   MediaController.MediaPlayerControl,
			   MediaPlayer.OnBufferingUpdateListener
	
	{
	
    private final IBinder mBinder = new ServiceBinder();
    
    MediaPlayer mPlayer;
    
    private int mBuffer = 0;
    
    public MusicService() { }

    public class ServiceBinder extends Binder {
     	 MusicService getService()
    	 {
    		return MusicService.this;
    	 }
    }

    @Override
    public IBinder onBind(Intent context)
    {
    	return mBinder;
    }

    @Override
    public void onCreate () {
    	super.onCreate();
    	
    	create();
	}

    @Override
	public int onStartCommand (Intent intent, int flags, int startId) {
         return START_STICKY;
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
		if(mPlayer != null)
		{
			try{
				 mPlayer.stop();
				 mPlayer.release();
			}finally {
				mPlayer = null;
			}
		}
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
		
		if(mPlayer != null)
		{
			try{
				mPlayer.stop();
				mPlayer.release();
			}finally {
				mPlayer = null;
			}
		}
		return false;
	}
	
	public void create()
	{
    	mPlayer = MediaPlayer.create(this, R.raw.tired);
    	mPlayer.setOnErrorListener(this);

    	mPlayer.setOnErrorListener(new OnErrorListener() {
    		public boolean onError(MediaPlayer mp, int what, int extra) {
    			onError(mPlayer, what, extra);
    			return true;
    		}
    	});
	}
	
	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getBufferPercentage() {
		return mBuffer;
	}

	@Override
	public int getCurrentPosition() {
		return mPlayer.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		return mPlayer.getDuration();
	}

	@Override
	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}

	@Override
	public void pause() {
		if(mPlayer != null && isPlaying())
		{
			mPlayer.pause();
		}
	}

	@Override
	public void seekTo(int pos) {
		mPlayer.seekTo(pos);
	}
	
	public void resume()
	{
		if(isPlaying() == false)
		{
			seekTo(getCurrentPosition());
			start();
		}
	}
	
	@Override
	public void start() {
		if (mPlayer == null) create();
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run()
			{
				mPlayer.start();
			}
		});
		th.start();
	}
	
	public void stop() {
		mPlayer.stop();
		mPlayer.release();
		mPlayer = null;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mBuffer = percent;
		
	}
}