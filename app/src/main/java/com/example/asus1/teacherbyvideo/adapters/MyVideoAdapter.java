package com.example.asus1.teacherbyvideo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.Holders.MyCourseHolder;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.MyVideoActivity;

import java.io.File;
import java.util.List;

public class MyVideoAdapter extends RecyclerView.Adapter<MyCourseHolder> {

    private Context mContext;
    private List<File> mVideos;
    private VideoCallBack mCallBack;

    public MyVideoAdapter(Context context, List<File> videos, VideoCallBack callback) {
        mContext = context;
        mVideos = videos;
        mCallBack = callback;
    }

    @Override
    public MyCourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_myvideo_item,parent,false);

        return new MyCourseHolder(view,mContext,mCallBack);
    }

    @Override
    public void onBindViewHolder(MyCourseHolder holder, int position) {
        holder.setData(mVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }
}
