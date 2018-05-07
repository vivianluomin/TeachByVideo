package com.example.asus1.teacherbyvideo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.asus1.teacherbyvideo.R;

/**
 * Created by asus1 on 2018/5/7.
 */

public class MineBG extends View {

    private Context mContext;
    private Paint mPaint;
    private Path mPath;
    private int mWdith;
    private int mHeight;

    public MineBG(Context context) {
        this(context,null);
    }

    public MineBG(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MineBG(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wdith = MeasureSpec.getSize(widthMeasureSpec);
        int wdithMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(wdithMode == MeasureSpec.EXACTLY){
            mWdith = wdith;
        }else {
            mWdith = 600;
        }
        if(heightMode == MeasureSpec.EXACTLY){
            mHeight = height;

        }else {
            mHeight = 300;
        }
        setMeasuredDimension(mWdith,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       mPaint.setStyle(Paint.Style.FILL);
       mPaint.setAntiAlias(true);
       mPaint.setColor(Color.parseColor("#ff5722"));
       mPath.moveTo(0,0);
       mPath.lineTo(0,mHeight-120);
       mPath.quadTo(mWdith/2,mHeight,mWdith,mHeight-120);
       mPath.lineTo(mWdith,0);
       canvas.drawPath(mPath,mPaint);
    }
}
