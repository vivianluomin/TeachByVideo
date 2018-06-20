package com.example.asus1.teacherbyvideo.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Util.ActivityManager;

public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnCompletionListener,

        MediaPlayer.OnPreparedListener,SurfaceHolder.Callback{

    private MediaPlayer mPlayer;
    private SurfaceView mPlayView;
    private ImageView mBack;
    private TextView mSaveNet;

    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ActivityManager.getInstance().addActivity(this);
        mFilePath = getIntent().getStringExtra("video");
        init();
    }

    private void init(){
        mPlayView = (SurfaceView)findViewById(R.id.surfaceview);
        mBack = (ImageView)findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSaveNet = (TextView)findViewById(R.id.tv_save_net);
        setVideo();
    }

    private void setVideo(){
        Uri uri = Uri.parse(mFilePath);
        mPlayer = MediaPlayer.create(this,uri);
       mPlayer.setOnCompletionListener(this);
       mPlayView.getHolder().addCallback(this);
       mPlayView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
       // mRecorde.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer.setDisplay(holder);
//        if (mRecorde!=null){
//            mRecorde.stop();
//        }
//        mRecorde.prepareAsync();
        mPlayer.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
