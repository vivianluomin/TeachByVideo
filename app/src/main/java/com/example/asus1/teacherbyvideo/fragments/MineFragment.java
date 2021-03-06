package com.example.asus1.teacherbyvideo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Util.UserManager;
import com.example.asus1.teacherbyvideo.activities.MyCourseActivity;
import com.example.asus1.teacherbyvideo.activities.MyInfoActivity;
import com.example.asus1.teacherbyvideo.activities.MyVideoActivity;

/**
 * Created by asus1 on 2018/5/2.
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    private TextView mMineInfo;
    private TextView mMyCourse;
    private TextView mMyVideo;
    private TextView mMyMarks;

    private TextView mUserName;
    private ImageView mUserIcon;

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

       mUserName = view.findViewById(R.id.tv_user_name);
       mUserIcon = view.findViewById(R.id.iv_usericon);
       initData();
        return view;
    }


    private void  initData(){
        if(UserManager.CURRENT_USER!=null){
            mUserName.setText(UserManager.CURRENT_USER.getmUsername());
            Glide.with(getContext())
                    .load(UserManager.CURRENT_USER.getmIcon())
                    .placeholder(R.mipmap.ic_usericon)
                    .error(R.mipmap.ic_usericon)
                    .into(mUserIcon);
        }

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
                Intent intent2 = new Intent(getContext(),MyVideoActivity.class);
                startActivity(intent2);
                break;

        }
    }
}
