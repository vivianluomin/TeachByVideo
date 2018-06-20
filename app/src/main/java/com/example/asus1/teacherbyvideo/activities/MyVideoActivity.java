package com.example.asus1.teacherbyvideo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Util.ActivityManager;
import com.example.asus1.teacherbyvideo.Util.Constant;
import com.example.asus1.teacherbyvideo.adapters.MyCourseAdapter;
import com.example.asus1.teacherbyvideo.adapters.MyVideoAdapter;
import com.example.asus1.teacherbyvideo.adapters.VideoCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyVideoActivity extends BaseActivity implements VideoCallBack {

    private ImageView mBack;
    private RecyclerView mRcyclerView;
    private MyVideoAdapter mAdapter;
    private List<File> mVideos = new ArrayList<>();

    private static final String TAG = "MyVideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        ActivityManager.getInstance().addActivity(this);
        init();
    }

    private void  init(){
        mBack = findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scanFold();
        mRcyclerView = findViewById(R.id.recycler_view);
        mRcyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,RecyclerView.VERTICAL));
        mAdapter = new MyVideoAdapter(this,mVideos,this);
        Log.d(TAG, "init: "+mVideos.size());
        mRcyclerView.setAdapter(mAdapter);
    }

    private void scanFold(){
        File dir = Constant.DIR;
        if(dir.exists()){
           File[] files = dir.listFiles();
           mVideos.clear();
            for(int i =0;i<files.length;i++){
                mVideos.add(files[i]);
                Log.d(TAG, "scanFold: "+files[i].toString());
            }
        }
    }

    @Override
    public void callBack() {
        scanFold();
        mAdapter.notifyDataSetChanged();
    }

}
