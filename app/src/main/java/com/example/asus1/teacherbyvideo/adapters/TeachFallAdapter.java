package com.example.asus1.teacherbyvideo.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.Holders.VideoHolder;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.RecordeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus1 on 2018/5/7.
 */

public class TeachFallAdapter extends RecyclerView.Adapter<VideoHolder> {
    private Context mContext;

    private String[] permissions;
    private List<String> mPer;

    private VedioPremissionCallBack mCallBack;

    public interface VedioPremissionCallBack{
        void callBack();
    }

    public TeachFallAdapter(Context context,VedioPremissionCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_teachfall
                ,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mCallBack.callBack();
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
