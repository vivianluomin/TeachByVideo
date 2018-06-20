package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.Encoder.MediaAudioEncoder;
import com.example.asus1.teacherbyvideo.Encoder.MediaEncoder;
import com.example.asus1.teacherbyvideo.Encoder.MediaMuxerWrapper;
import com.example.asus1.teacherbyvideo.Encoder.MediaVideoEncoder;
import com.example.asus1.teacherbyvideo.Encoder.VideoComposer;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Util.DialogBuilder;
import com.example.asus1.teacherbyvideo.views.CameraGLViews.CameraGLView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

public class RecordeActivity extends  BaseActivity implements View.OnClickListener{

    private ImageView mPlayImage;
    private CameraGLView mPlayView;

    private ImageView mPreview;
    private ImageView mBack;
    private ImageView mChangeCamera;
    private SurfaceView mPlaySurface;

    private MediaMuxerWrapper mMuxer;
    private MediaVideoEncoder mVedioEncoder;
    private MediaAudioEncoder mAudiaEncoder;

    private MediaPlayer mPlayer;

    private ImageView mDelete;
    private ImageView mMask;
    private TextView mTime;
    private static final String DIR_NAME = "AVRecReal";
    private static final SimpleDateFormat mDateTimeFormat =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

    private static final SimpleDateFormat mTimeFormat =
            new SimpleDateFormat("mm:ss",Locale.CHINA);

    private String mOutputPath;
    private boolean mRecord = false;

    private static final String TAG = "PlayActivity";

    private long mLimitTime = 5*60*1000;
    private long time = 0;

    private int mSurfaceWidth;
    private int mSurfaceHeight;

    private int camear_id = 1;
    private Object mSyn = new Object();

    private String mPlayPath = "/storage/emulated/0/Tencent/QQfile_recv/miku_mmd.mp4";

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
        CameraGLView.CAMERA_ID = 1;
        mPlayImage.setOnClickListener(this);
        mPreview = (ImageView)findViewById(R.id.iv_preview);
        mPreview.setOnClickListener(this);
        mBack = (ImageView)findViewById(R.id.iv_back);
        mBack.setOnClickListener(this);

        mDelete = (ImageView)findViewById(R.id.iv_delete);
        mDelete.setOnClickListener(this);
        mMask = (ImageView)findViewById(R.id.iv_mask);
        mMask.setOnClickListener(this);
        mTime = (TextView)findViewById(R.id.tv_time) ;
        mChangeCamera = (ImageView)findViewById(R.id.iv_camera);
        mChangeCamera.setOnClickListener(this);

        mPlaySurface = (SurfaceView)findViewById(R.id.surface_play);
        Uri uri = Uri.parse(mPlayPath);
        mPlayer = MediaPlayer.create(RecordeActivity.this,uri);
        mPlayer.setOnCompletionListener(mPlayCompletionListener);
        mPlaySurface.getHolder().addCallback(mPlayCallBack);
        mPlaySurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivbtn_play:
                if(mMuxer != null){
                    stopRecord();
                    mPreview.setVisibility(View.VISIBLE);
                    mBack.setVisibility(View.VISIBLE);
                    mChangeCamera.setVisibility(View.VISIBLE);
                    mDelete.setVisibility(View.VISIBLE);
                    mMask.setVisibility(View.VISIBLE);
                    mRecord = false;
                }else {
                    startRecording();
                    mPreview.setVisibility(View.GONE);
                    mBack.setVisibility(View.GONE);
                    mChangeCamera.setVisibility(View.GONE);
                    mDelete.setVisibility(View.GONE);
                    mMask.setVisibility(View.GONE);
                    mRecord = true;
                }
                break;

            case R.id.iv_camera:
                mPlayView.onPause();
                mPlayer.pause();
                if(camear_id == 1){
                    camear_id = 0;
                    CameraGLView.CAMERA_ID = 0;
                }else {
                    camear_id = 1;
                    CameraGLView.CAMERA_ID = 1;
                }
                while (mPlayView.mHasSurface){
                    try {
                        Thread.sleep(50);
                        Log.d(TAG, "onClick: "+mPlayView.mHasSurface);
                    }catch (InterruptedException e ){

                    }

                }
                mPlayView.onResume();
                mPlayer.start();
                break;
            case R.id.iv_preview:
                preView();
                break;

            case R.id.iv_delete:
                final AlertDialog dialog_delete = DialogBuilder
                        .createMessageDialog(
                                RecordeActivity.this, "",
                                getString(R.string.record_delet), true,
                                new DialogBuilder.DialogCallback() {
                                    @Override
                                    public void certian() {
                                        deleteVideoShort();
                                        mTime.setText("");
                                        time = 0;
                                    }

                                    @Override
                                    public void cancle() {
                                        //dialog.dismiss();

                                    }
                                });
                dialog_delete.show();
                break;
            case R.id.iv_mask:
                break;
            case R.id.iv_back:
                    if(!mRecord)
                     showMesaageDelet();
                break;

        }

    }

    private void showMesaageDelet(){
        if(mVideos.size()<=0){
            finish();
        }else {
            final AlertDialog dialog = DialogBuilder
                    .createMessageDialog(
                            RecordeActivity.this, "",
                            getString(R.string.record_back), true,
                            new DialogBuilder.DialogCallback() {
                                @Override
                                public void certian() {
                                    deleteVideoShort();
                                    finish();
                                }

                                @Override
                                public void cancle() {
                                    //dialog.dismiss();
                                    ComposeVideo();
                                    finish();
                                }
                            });
            dialog.show();
        }

    }

    private void preView(){
        if(mVideos.size()<=0) return;
        AlertDialog dialog = DialogBuilder.createSimpleDialog(this,
                "",getString(R.string.record_composer));
        dialog.show();
        VideoComposer composer = new VideoComposer
                (mVideos, mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES
                        , ".mp4").toString());
        if(ComposeVideo() && mOutputPath!=null){
            Intent intent = new Intent(RecordeActivity.this,PlayVideoActivity.class);
            intent.putExtra("video",mOutputPath);
            deleteVideoShort();
            mVideos.add(mOutputPath);
            startActivity(intent);
        }

        dialog.dismiss();
    }

    private boolean ComposeVideo(){
        if(mVideos.size()<=0) return false;
        VideoComposer composer = new VideoComposer
                (mVideos, mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES
                        , ".mp4").toString());

        return composer.joinVideo();
    }

    @Override
    public void onBackPressed() {
        if(!mRecord){
            showMesaageDelet();
        }

        //super.onBackPressed();
    }

    private void deleteVideoShort(){

        for(int i =0;i<mVideos.size();i++){
            String path = mVideos.get(i);
            File file = new File(path);
            if(file.exists()){
                file.delete();
                Log.d(TAG, "deleteVideoShort: ");
            }
        }
        mVideos.clear();
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
            mHandler.postDelayed(mTimerunnable,100);
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

    private SurfaceHolder.Callback mPlayCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
                holder.setFixedSize(100,150);
            if(mPlayer == null){
                Uri uri = Uri.parse(mPlayPath);
                mPlayer = MediaPlayer.create(RecordeActivity.this,uri);
                mPlayer.setOnCompletionListener(mPlayCompletionListener);
                mPlaySurface.getHolder().addCallback(mPlayCallBack);
                mPlaySurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
           // holder.setFixedSize(150,100);
            mSurfaceHeight = width;
            mSurfaceWidth = height;
            Log.d(TAG, "surfaceChanged: "+width+"   "+height);
            mPlayer.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
            mPlayer.setDisplay(holder);
            mPlayer.start();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
                mPlayer.pause();
        }
    };

    private MediaPlayer.OnCompletionListener mPlayCompletionListener
            = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
                mp.start();
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener =
            new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            changeVideoSize();
        }
    };

    public void changeVideoSize() {
        int videoWidth = mPlayer.getVideoWidth();
        int videoHeight = mPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation==
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth /
                    (float) mSurfaceWidth,(float) videoHeight / (float) mSurfaceHeight);
        } else{
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth/(float)
                    mSurfaceHeight),(float) videoHeight/(float) mSurfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
       RelativeLayout.LayoutParams p =
               new RelativeLayout.LayoutParams(videoWidth, videoHeight);
       p.topMargin = 230;
       p.leftMargin = 50;
        mPlaySurface.setLayoutParams(p);
        Log.d(TAG, "changeVideoSize: "+videoWidth+"---"+videoHeight);

    }

    @Override
    protected void onDestroy() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
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

    private Handler mHandler = new Handler();

    private Runnable mTimerunnable = new Runnable() {
        @Override
        public void run() {
            if (mRecord&&time<mLimitTime){
                time+=100;
                mTime.setText(mTimeFormat.format(new Date(time)));
                mHandler.postDelayed(mTimerunnable,100);
            }

            if(mRecord&&time == mLimitTime){
                mRecord = false;
                stopRecord();
                preView();
            }

        }
    };
}
