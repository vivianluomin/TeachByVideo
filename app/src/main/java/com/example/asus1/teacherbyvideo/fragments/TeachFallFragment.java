package com.example.asus1.teacherbyvideo.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.CourseDetailActivity;
import com.example.asus1.teacherbyvideo.activities.RecordeActivity;
import com.example.asus1.teacherbyvideo.adapters.TeachFallAdapter;
import com.example.asus1.teacherbyvideo.adapters.VedioPremissionCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus1 on 2018/5/2.
 */

public class TeachFallFragment extends Fragment implements VedioPremissionCallBack {


    private RecyclerView mRecycler;
    private TeachFallAdapter mAdapter;
    private String[] permissions;
    private List<String> mPer;

    private static final String TAG = "TeachFallFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachfall,container,false);
        mRecycler = (RecyclerView)view.findViewById(R.id.recycler_view);
        mAdapter = new TeachFallAdapter(getContext(),this);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void callBack() {

        //setPermission();
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        startActivity(intent);
    }

}
