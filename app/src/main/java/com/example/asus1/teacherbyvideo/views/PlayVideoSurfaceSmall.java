package com.example.asus1.teacherbyvideo.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.PropertyResourceBundle;

public class PlayVideoSurfaceSmall extends SurfaceView {

    private Context mContext;
    private MediaPlayer mPlayer;
    private String mFilePath;

    public PlayVideoSurfaceSmall(Context context) {
        this(context,null);
    }

    public PlayVideoSurfaceSmall(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlayVideoSurfaceSmall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(350,500);
    }


}
