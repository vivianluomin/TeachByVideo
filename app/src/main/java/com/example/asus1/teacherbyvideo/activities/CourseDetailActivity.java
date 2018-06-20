package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Util.ActivityManager;
import com.example.asus1.teacherbyvideo.adapters.CourseDetailAdapter;
import com.example.asus1.teacherbyvideo.adapters.VedioPremissionCallBack;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity implements VedioPremissionCallBack{

    private RecyclerView mRecyclerView;
    private CourseDetailAdapter mAdapter;
    private List<String> mVideos = new ArrayList<>();
    private List<String> mComments = new ArrayList<>();
    private String[] permissions;
    private List<String> mPer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ActivityManager.getInstance().addActivity(this);
        Window window = getWindow();
        View decor = window.getDecorView();
        if(Build.VERSION.SDK_INT>=21){
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    ,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        init();
    }


    private void init(){
        mVideos.add("");
        mVideos.add("");
        mVideos.add("");
        mVideos.add("");
        mVideos.add("");
        mComments.add("");
        mComments.add("");
        mComments.add("");
        mComments.add("");
        mComments.add("");
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseDetailAdapter(this,mComments,mVideos,this);
        mRecyclerView.setAdapter(mAdapter);
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
            if(ActivityCompat.checkSelfPermission
                    (this,permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                mPer.add(permissions[i]);
            }
        }
        if(mPer.size()>0){
            this.requestPermissions((String[]) mPer.toArray(new String[mPer.size()]),100);
        }else {
            startActivity(new Intent(this, RecordeActivity.class));
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
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"你拒绝了该请求",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startActivity(new Intent(this, RecordeActivity.class));
            }
        }

    }
}
