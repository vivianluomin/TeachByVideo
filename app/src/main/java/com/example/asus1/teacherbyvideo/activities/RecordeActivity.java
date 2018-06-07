package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.Encoder.MediaAudioEncoder;
import com.example.asus1.teacherbyvideo.Encoder.MediaEncoder;
import com.example.asus1.teacherbyvideo.Encoder.MediaMuxerWrapper;
import com.example.asus1.teacherbyvideo.Encoder.MediaVideoEncoder;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.views.CameraGLViews.CameraGLView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordeActivity extends  BaseActivity implements View.OnClickListener{

    private ImageView mPlayImage;
    private CameraGLView mPlayView;
    private String[] permissions;
    private List<String> mPer;

    private ImageView mPreview;
    private ImageView mBack;
    private ImageView mChangeCamera;

    private int mWidth;
    private int mHeight;


    private MediaMuxerWrapper mMuxer;
    private MediaVideoEncoder mVedioEncoder;
    private MediaAudioEncoder mAudiaEncoder;

    private boolean mRecord = false;


    private static final String TAG = "PlayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorde);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mWidth = getWindow().getDecorView().getWidth();
        mHeight = getWindow().getDecorView().getHeight();
        Log.d(TAG, "mWidth: "+mWidth+ "  mHeight: " +mHeight);
        init();


    }

    

    private void init(){
        mPlayImage = (ImageView)findViewById(R.id.ivbtn_play);
        //mPlayImage.setVideoSize(1280, 720);
        mPlayView = (CameraGLView) findViewById(R.id.surfaceview);
        mPlayImage.setOnClickListener(this);
        mPreview = (ImageView)findViewById(R.id.iv_preview);
        mPreview.setOnClickListener(this);
        mBack = (ImageView)findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChangeCamera = (ImageView)findViewById(R.id.iv_camera);
        mChangeCamera.setOnClickListener(this);

        //setPermission();


    }




    private void setPermission(){
        permissions = new String[3];
        mPer = new ArrayList<>();
        permissions[0] = Manifest.permission.RECORD_AUDIO;
        permissions[1] = Manifest.permission.CAMERA;
        permissions[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;


        for(int i = 0;i<permissions.length;i++){
            if(ContextCompat.checkSelfPermission
                    (this,permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                mPer.add(permissions[i]);
            }
        }
        if(mPer.size()>0){
            ActivityCompat.requestPermissions(this,
                    (String[]) mPer.toArray(new String[mPer.size()]),100);
        }else {
            setRecorder();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults.length>0){
                for(int i = 0;i<grantResults.length;i++){
                    Log.d(TAG, "onRequestPermissionsResult: ");
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"你拒绝了该请求",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                setRecorder();

            }
        }


    }



    private void setRecorder(){
      // mPlayView.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivbtn_play:
                if(mRecord){
                    stopRecord();
                    mRecord = false;
                }else {
                    startRecording();
                    mRecord = true;
                }
                break;

            case R.id.iv_camera:
                break;
            case R.id.iv_preview:

//                Intent intent = new Intent(RecordeActivity.this,PlayVideoActivity.class);
//                intent.putExtra("video",mFilePath);
//                startActivity(intent);

                break;
        }

    }

    private void startRecording(){

        try {
            mPlayImage.setImageResource(R.mipmap.ic_recode);
            mMuxer = new MediaMuxerWrapper(".mp4");
            mVedioEncoder = new MediaVideoEncoder(mMuxer,mEncoderListener,
                    mPlayView.getVideoWidth(),mPlayView.getVideoHeight());
            mAudiaEncoder = new MediaAudioEncoder(mMuxer,mEncoderListener);

            mMuxer.prepare();
            mMuxer.startRecoding();
        }catch (IOException e){

        }


    }

    private void stopRecord(){

            mPlayImage.setImageResource(R.mipmap.ic_pause);
            mMuxer.stopRecording();

    }

    private MediaEncoder.MediaEncoderListener mEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepares(MediaEncoder encoder) {
            if(encoder instanceof MediaVideoEncoder){
                mPlayView.setVedioEncoder((MediaVideoEncoder)encoder);
            }
        }

        @Override
        public void onStop(MediaEncoder encoder) {
            if(encoder instanceof MediaVideoEncoder){
                mPlayView.setVedioEncoder(null);
            }
        }
    };

    @Override
    protected void onResume() {

        super.onResume();
        mPlayView.onResume();
    }

    @Override
    protected void onPause() {
       // stopRecording();
        mPlayView.onPause();
        super.onPause();
    }




    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
