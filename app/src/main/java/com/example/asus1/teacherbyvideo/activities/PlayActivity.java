package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends  BaseActivity implements View.OnClickListener{

    private ImageView mPlayImage;
    private TextureView mPlayView;
    private MediaRecorder mRecorder;
    private File mViedoFile;
    private String[] permissions;
    private List<String> mPer;
    private Camera mCamers;
    private boolean isRecording = false;

    private static final String TAG = "PlayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        init();
        setPermission();

    }

    private void init(){
        mPlayImage = (ImageView)findViewById(R.id.ivbtn_play);
        mPlayView = (TextureView) findViewById(R.id.surfaceview);
        mPlayImage.setOnClickListener(this);
        
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//        mPlayView.getHolder().setFixedSize(profile.videoFrameWidth,profile.videoFrameHeight);
//        mPlayView.getHolder().setKeepScreenOn(true);
//        mPlayView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


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
        try {

            mViedoFile = new File(
                    Environment.getExternalStorageDirectory()
                            .getCanonicalFile()+"/testvideo.3gp");
            mRecorder = new MediaRecorder();
            mRecorder.reset();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
            mRecorder.setVideoFrameRate(20);
            mRecorder.setOutputFile(mViedoFile.getAbsolutePath());
            mRecorder.setPreviewDisplay(mPlayView.);
            mRecorder.prepare();
           // mRecorder.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(isRecording){
            mRecorder.pause();
            mPlayImage.setImageResource(R.mipmap.ic_recode);
            isRecording = false;
            Log.d(TAG, "onClick: pause");
        }else {
           mRecorder.start();
           mPlayImage.setImageResource(R.mipmap.ic_pause);
           isRecording = true;
            Log.d(TAG, "onClick: start");
        }
    }
}
