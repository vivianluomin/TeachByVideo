package com.example.asus1.teacherbyvideo.Holders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Util.DialogBuilder;
import com.example.asus1.teacherbyvideo.activities.MyVideoActivity;
import com.example.asus1.teacherbyvideo.activities.PlayVideoActivity;
import com.example.asus1.teacherbyvideo.adapters.VideoCallBack;

import java.io.File;

public class MyCourseHolder extends RecyclerView.ViewHolder {

    private View mParent;
    private ImageView mCover;
    private ImageView mDelet;
    private Context mContext;
    private File mVideo;
    private TextView mTime;
    private VideoCallBack mCallBack;

    private static final String TAG = "MyCourseHolder";

    public MyCourseHolder(View itemView, Context context, VideoCallBack callBack) {
        super(itemView);
        mParent = itemView;
        mCallBack = callBack;
        mCover = mParent.findViewById(R.id.iv_video_cover);
        mDelet = mParent.findViewById(R.id.iv_delete);
        mTime = mParent.findViewById(R.id.tv_time);
        mDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.createMessageDialog(mContext,""
                        ,mContext.getResources().
                                getString(R.string.myvideo_delete),true,callback).show();
            }
        });

        mCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayVideoActivity.class);
                intent.putExtra("video",mVideo.toString());
                mContext.startActivity(intent);
            }
        });
        mContext = context;
    }

    public void setData(File file){
        mVideo = file;
        //MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

        //metadataRetriever.setDataSource(mVideo.getAbsolutePath());
        Bitmap bitmap = ThumbnailUtils.
                createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        mCover.setImageBitmap(bitmap);

        String path = file.toString();
        Log.d(TAG, "setData: "+path);
        String[] ss =  path.split("/");
        path = ss[ss.length-1];
        Log.d(TAG, "setData: "+path);
        path = path.split(".mp4")[0];
        mTime.setText(path);
    }

    private DialogBuilder.DialogCallback callback = new DialogBuilder.DialogCallback() {
        @Override
        public void certian() {
            mVideo.delete();
            mCallBack.callBack();
        }

        @Override
        public void cancle() {

        }
    };
}
