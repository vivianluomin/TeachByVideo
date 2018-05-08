package com.example.asus1.teacherbyvideo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.Holders.VideoHolder;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.PlayActivity;

/**
 * Created by asus1 on 2018/5/7.
 */

public class TeachFallAdapter extends RecyclerView.Adapter<VideoHolder> {
    private Context mContext;

    public TeachFallAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_teachfall
                ,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PlayActivity.class));
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
