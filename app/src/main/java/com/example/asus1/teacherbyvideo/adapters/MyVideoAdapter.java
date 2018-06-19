package com.example.asus1.teacherbyvideo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.Holders.MyCourseHolder;
import com.example.asus1.teacherbyvideo.R;

public class MyVideoAdapter extends RecyclerView.Adapter<MyCourseHolder> {

    private Context mContext;

    public MyVideoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyCourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_myvideo_item,parent,false);

        return new MyCourseHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCourseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
