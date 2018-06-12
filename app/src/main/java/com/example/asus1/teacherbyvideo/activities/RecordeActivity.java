package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.Encoder.MediaAudioEncoder;
import com.example.asus1.teacherbyvideo.Encoder.MediaEncoder;
import com.example.asus1.teacherbyvideo.Encoder.MediaMuxerWrapper;
import com.example.asus1.teacherbyvideo.Encoder.MediaVideoEncoder;
import com.example.asus1.teacherbyvideo.Encoder.VideoComposer;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.views.CameraGLViews.CameraGLView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class RecordeActivity extends  BaseActivity implements View.OnClickListener{

    private ImageView mPlayImage;
    private CameraGLView mPlayView;

    private ImageView mPreview;
    private ImageView mBack;
    private ImageView mChangeCamera;

    private int mWidth;
    private int mHeight;


    private MediaMuxerWrapper mMuxer;
    private MediaVideoEncoder mVedioEncoder;
    private MediaAudioEncoder mAudiaEncoder;

    private ImageView mDelete;
    private ImageView mMask;
    private TextView mTime;
    private static final String DIR_NAME = "AVRecReal";
    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

    private String mOutputPath;
    private boolean mRecord = false;

    private static final String TAG = "PlayActivity";

    private ArrayList<String> mVideos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorde);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        init();


    }

    

    private void init(){
        mPlayImage = (ImageView)findViewById(R.id.ivbtn_play);
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

        mDelete = (ImageView)findViewById(R.id.iv_delete);
        mDelete.setOnClickListener(this);

        mMask = (ImageView)findViewById(R.id.iv_mask);
        mMask.setOnClickListener(this);

        mTime = (TextView)findViewById(R.id.tv_time) ;


        mChangeCamera = (ImageView)findViewById(R.id.iv_camera);
        mChangeCamera.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivbtn_play:
                if(mMuxer != null){
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

                VideoComposer composer = new VideoComposer
                        (mVideos, mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES
                                , ".mp4").toString());

                if(composer.joinVideo() && mOutputPath!=null){
                    Intent intent = new Intent(RecordeActivity.this,PlayVideoActivity.class);
                    intent.putExtra("video",mOutputPath);
                    startActivity(intent);
                }
                break;
        }

    }

    private void startRecording(){

        try {
            mPlayImage.setImageResource(R.mipmap.ic_pause);
            mMuxer = new MediaMuxerWrapper(".mp4");
            mVedioEncoder = new MediaVideoEncoder(mMuxer,mEncoderListener,
                    mPlayView.getVideoWidth(),mPlayView.getVideoHeight());
            mAudiaEncoder = new MediaAudioEncoder(mMuxer,mEncoderListener);
            mMuxer.prepare();
            mMuxer.startRecording();
        }catch (IOException e){

        }


    }

    private void stopRecord(){

        mPlayImage.setImageResource(R.mipmap.ic_recode);
        if (mMuxer != null) {
            mMuxer.stopRecording();
            mVideos.add(mMuxer.getOutputPath());
            mMuxer = null;
            // you should not wait here
        }

    }

    private MediaEncoder.MediaEncoderListener mEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(MediaEncoder encoder) {
            if(encoder instanceof MediaVideoEncoder){
                mPlayView.setVedioEncoder((MediaVideoEncoder)encoder);
            }
        }

        @Override
        public void onStopped(MediaEncoder encoder) {
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
        stopRecord();
        mPlayView.onPause();
        super.onPause();
    }




    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public static final File getCaptureFile(final String type, final String ext) {
        final File dir = new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);
        Log.d(TAG, "path=" + dir.toString());
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, getDateTimeString() + ext);
        }
        return null;
    }

    /**
     * get current date and time as String
     * @return
     */
    private static final String getDateTimeString() {
        final GregorianCalendar now = new GregorianCalendar();
        return mDateTimeFormat.format(now.getTime());
    }
}
