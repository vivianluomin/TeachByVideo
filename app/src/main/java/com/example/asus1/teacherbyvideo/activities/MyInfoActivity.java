package com.example.asus1.teacherbyvideo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus1.teacherbyvideo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener{

    private CircleImageView mUserIcon;
    private TextView mSpotsTimes;
    private TextView mSpotsTime;
    private TextView mMarks;
    private TextView mChangePassword;
    private TextView mUserName;
    private TextView mQuit;
    private FrameLayout mFrageUserName;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        init();
    }

    private void init(){
        mUserIcon = (CircleImageView)findViewById(R.id.iv_usericon);
        mUserIcon.setOnClickListener(this);

        mSpotsTimes = findViewById(R.id.tv_spots_times);
        mSpotsTime = findViewById(R.id.tv_spots_time);
        mMarks = findViewById(R.id.tv_spots_mark);

        mChangePassword = findViewById(R.id.tv_changepassword);
        mChangePassword.setOnClickListener(this);

        mUserName = findViewById(R.id.tv_user_name);
        //mUserName.setOnClickListener(this);
        mFrageUserName = findViewById(R.id.frag_user_name);
        mFrageUserName.setOnClickListener(this);

        mQuit = findViewById(R.id.tv_quit);
        mQuit.setOnClickListener(this);

        mBack = findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_usericon:
                break;
            case R.id.tv_changepassword:
                break;
            case R.id.frag_user_name:
                break;
            case R.id.tv_quit:

                break;
        }
    }
}
