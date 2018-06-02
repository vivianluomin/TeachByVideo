package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.views.CameraGLView;
import com.example.asus1.teacherbyvideo.views.VideoRecordView;

import java.io.File;
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


    private static final String TAG = "PlayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorde);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
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
