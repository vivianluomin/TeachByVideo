package com.example.asus1.teacherbyvideo.views;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

public class VideoRecordView extends SurfaceView  {

    private Context mContext;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private MediaRecorder mRecorder;
    private File mViedoFile;


    public VideoRecordView(Context context) {
        this(context,null);
    }

    public VideoRecordView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        mHolder = getHolder();

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mHolder.setFixedSize(profile.videoFrameWidth,profile.videoFrameHeight);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setKeepScreenOn(true);

    }


    private void setRecorder(){
        try {

            mViedoFile = new File(
                    Environment.getExternalStorageDirectory()
                    ,System.currentTimeMillis()+".mp4");
            mCamera.startPreview();
            mCamera.unlock();
            mRecorder = new MediaRecorder();
            mRecorder.reset();
            mRecorder.setCamera(mCamera);
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
            mRecorder.setVideoFrameRate(20);
            mRecorder.setOutputFile(mViedoFile.getAbsolutePath());
            mRecorder.setPreviewDisplay(mHolder.getSurface());
            mRecorder.setOrientationHint(90);

            mRecorder.prepare();
            mRecorder.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
