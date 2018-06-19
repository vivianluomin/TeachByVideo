package com.example.asus1.teacherbyvideo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.adapters.MyCourseAdapter;
import com.example.asus1.teacherbyvideo.adapters.MyVideoAdapter;

public class MyVideoActivity extends BaseActivity {

    private ImageView mBack;
    private RecyclerView mRcyclerView;
    private MyVideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
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

        mRcyclerView = findViewById(R.id.recycler_view);
        mRcyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,RecyclerView.VERTICAL));
        mAdapter = new MyVideoAdapter(this);
        mRcyclerView.setAdapter(mAdapter);
    }
}
