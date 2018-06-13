package com.example.asus1.teacherbyvideo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.adapters.MyCourseAdapter;

public class MyCourseActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MyCourseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        init();
    }

    private void init(){
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new MyCourseAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }
}
