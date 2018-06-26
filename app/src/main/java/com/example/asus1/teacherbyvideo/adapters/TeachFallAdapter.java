package com.example.asus1.teacherbyvideo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.Holders.VideoHolder;
import com.example.asus1.teacherbyvideo.Models.CourseModel;
import com.example.asus1.teacherbyvideo.R;

import java.util.List;

/**
 * Created by asus1 on 2018/5/7.
 */

public class TeachFallAdapter extends RecyclerView.Adapter<VideoHolder> {
    private Context mContext;

    private String[] permissions;
    private List<String> mPer;
    private List<CourseModel> mCourses;

    private VedioPremissionCallBack<CourseModel> mCallBack;


    public TeachFallAdapter(Context context, List<CourseModel> courses, VedioPremissionCallBack<CourseModel> callBack) {
        mContext = context;
        mCallBack = callBack;
        mCourses = courses;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_coursefall_item
                ,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallBack.callBack(mCourses.get(position));
            }
        });

        holder.setData(mCourses.get(position));
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }


}
