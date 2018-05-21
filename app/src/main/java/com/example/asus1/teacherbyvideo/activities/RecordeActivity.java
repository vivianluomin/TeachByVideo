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
import com.example.asus1.teacherbyvideo.views.VideoRecordView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordeActivity extends  BaseActivity implements View.OnClickListener , SurfaceHolder.Callback{

    private ImageView mPlayImage;
    private VideoRecordView mPlayView;
    private MediaRecorder mRecorder;
    private File mViedoFile;
    private String[] permissions;
    private List<String> mPer;

    private ImageView mPreview;
    private ImageView mBack;
    private ImageView mChangeCamera;

    private Camera mCamera;

    private boolean isRecording = false;

    private int cameraId;

    private boolean isFrist = true;
    private String mFilePath = null;

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
        mPlayView = (VideoRecordView) findViewById(R.id.surfaceview);
        mPlayView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPlayView.getHolder().addCallback(this);
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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setPermission();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
           // mCamera.unlock();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    private int findFrontFacingCamera() {

        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private void setRecorder(){
        try {

            if(mCamera == null){
                mCamera = Camera.open(findFrontFacingCamera());
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(mPlayView.getHolder());
                mCamera.startPreview();
                mCamera.unlock();

            }
            if(mRecorder== null){
                mRecorder = new MediaRecorder();
                mRecorder.setCamera(mCamera);
            }



            mViedoFile = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis()+".mp4");
            mFilePath = mViedoFile.getPath();
            Log.d(TAG, "setRecorder: "+mFilePath);
            mRecorder.reset();
            mRecorder.setOrientationHint(90);

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

            mRecorder.setOutputFile(mViedoFile.getAbsolutePath());
            mRecorder.setPreviewDisplay(mPlayView.getHolder().getSurface());

            mRecorder.prepare();

            mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    mRecorder.stop();
                    mRecorder.release();
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                    mRecorder = null;
                    Log.e(TAG, "onError: "+extra);
                }
            });

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivbtn_play:
                if(isRecording){
                    mRecorder.pause();
//            mRecorder.release();
//            mRecorder = null;
                    mPlayImage.setImageResource(R.mipmap.ic_pause);
                    isRecording = false;
                    Log.d(TAG, "onClick: stop");
                }else {
                    if(isFrist){
                        mRecorder.start();
                        isFrist = false;
                    }else {
//                mCamera.stopPreview();
//                mCamera.release();
//                mCamera = null;
                        //setRecorder();
                        mRecorder.resume();
                    }

                    mPlayImage.setImageResource(R.mipmap.ic_recode);
                    isRecording = true;
                    Log.d(TAG, "onClick: start");
                }
                break;

            case R.id.iv_camera:
                break;
            case R.id.iv_preview:
                mRecorder.stop();
                mCamera.stopPreview();
                Intent intent = new Intent(RecordeActivity.this,PlayVideoActivity.class);
                intent.putExtra("video",mFilePath);
                startActivity(intent);

                break;
        }

    }

    @Override
    protected void onResume() {
        if(mCamera!=null){
            // mCamera.unlock();
            mCamera.startPreview();
            mCamera.unlock();
            // mCamera.release();
            //mCamera = null;
        }
        if(mRecorder !=null){
            mRecorder.resume();
            // mRecorder.release();
            // mRecorder = null;
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mCamera!=null){
            // mCamera.unlock();
            mCamera.stopPreview();
            mCamera.unlock();
           // mCamera.release();
            //mCamera = null;
        }
        if(mRecorder !=null){
            mRecorder.pause();
           // mRecorder.release();
           // mRecorder = null;
        }
        super.onPause();
    }



    @Override
    protected void onDestroy() {
        if(mCamera!=null){
            // mCamera.unlock();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if(mRecorder !=null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }

        super.onDestroy();
    }
}
