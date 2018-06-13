package com.example.asus1.teacherbyvideo.Holders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus1.teacherbyvideo.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class CourseHeaderHolder extends CourseDetailHolder{

    private View mView;

    private ImageView mBigCover;
    private Context mContext;

    public CourseHeaderHolder(Context context,View itemView) {
        super(itemView);
        mView = itemView;
        mBigCover = mView.findViewById(R.id.iv_cover_big);
        mContext = context;
        Glide.with(context)
                .load(R.mipmap.bg_video)
                .placeholder(R.mipmap.bg_video)
                .error(R.mipmap.bg_video)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(context,23,4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(mBigCover);


    }


    private void blur(Bitmap bkg, View view) {

        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()),
                (int)(view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(mContext);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(mContext.getResources(), overlay));
        rs.destroy();

    }
}
