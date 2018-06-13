package com.example.asus1.teacherbyvideo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.MyCourseActivity;
import com.example.asus1.teacherbyvideo.activities.MyInfoActivity;

/**
 * Created by asus1 on 2018/5/2.
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    private TextView mMineInfo;
    private TextView mMyCourse;
    private TextView mMyVideo;
    private TextView mMyMarks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_mine,container,false);
       mMineInfo = (TextView)(view.findViewById(R.id.tv_mineinfo));
       mMineInfo.setOnClickListener(this);
       mMyCourse = (TextView)(view.findViewById(R.id.tv_course));
       mMyCourse.setOnClickListener(this);
       mMyVideo = (TextView)(view.findViewById(R.id.tv_video));
       mMyVideo.setOnClickListener(this);
       mMyMarks = (TextView)(view.findViewById(R.id.tv_marks));
       mMyMarks.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_mineinfo:
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_course:
                Intent intent1 = new Intent(getContext(),MyCourseActivity.class);
                startActivity(intent1);
                break;

            case R.id.tv_video:

                break;

            case R.id.tv_marks:
                break;
        }
    }
}
