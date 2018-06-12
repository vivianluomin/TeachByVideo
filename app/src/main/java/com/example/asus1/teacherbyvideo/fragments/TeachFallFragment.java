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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.RecordeActivity;
import com.example.asus1.teacherbyvideo.adapters.TeachFallAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus1 on 2018/5/2.
 */

public class TeachFallFragment extends Fragment implements TeachFallAdapter.VedioPremissionCallBack {


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
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(),2,
                GridLayoutManager.VERTICAL,false));
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void callBack() {
        setPermission();
    }


    private void setPermission(){
        permissions = new String[3];
        mPer = new ArrayList<>();
        permissions[0] = Manifest.permission.RECORD_AUDIO;
        permissions[1] = Manifest.permission.CAMERA;
        permissions[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;


        for(int i = 0;i<permissions.length;i++){
            if(ContextCompat.checkSelfPermission
                    (getContext(),permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                mPer.add(permissions[i]);
            }
        }
        if(mPer.size()>0){
            this.requestPermissions((String[]) mPer.toArray(new String[mPer.size()]),100);
        }else {
            startActivity(new Intent(getContext(), RecordeActivity.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults.length>0){
                for(int i = 0;i<grantResults.length;i++){
                    Log.d(TAG, "onRequestPermissionsResult: ");
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getContext(),"你拒绝了该请求",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startActivity(new Intent(getContext(), RecordeActivity.class));
            }
        }

    }
}
