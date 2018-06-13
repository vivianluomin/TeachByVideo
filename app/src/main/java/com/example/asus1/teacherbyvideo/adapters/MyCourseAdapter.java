package com.example.asus1.teacherbyvideo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.Holders.VideoHolder;
import com.example.asus1.teacherbyvideo.R;

import java.util.ConcurrentModificationException;

public class MyCourseAdapter extends RecyclerView.Adapter<VideoHolder>{

    private Context mContext;

    public MyCourseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_mycourse_item,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
