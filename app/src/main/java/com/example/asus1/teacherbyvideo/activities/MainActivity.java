package com.example.asus1.teacherbyvideo.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.activities.BaseActivity;
import com.example.asus1.teacherbyvideo.fragments.MineFragment;
import com.example.asus1.teacherbyvideo.fragments.TeachFallFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    private FrameLayout mContanier;
    private BottomNavigationBar mBottomBar;
    private FragmentTransaction mTransaction;
    private FragmentManager mManager;
   private Fragment mFallFragment;
   private Fragment mMineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
       mManager = getSupportFragmentManager();
//       mFallFragment =new TeachFallFragment();
//       mMineFragment = new MineFragment();

       mBottomBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        mBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomBar.setBarBackgroundColor(R.color.bg_color);
        mBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
       mBottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_fall,R.string.teach_fall)
                        .setInActiveColorResource(R.color.divideColor).setActiveColorResource(R.color.mainColor)

                        )
                .addItem(new BottomNavigationItem(R.mipmap.ic_mine,R.string.mime)
                        .setInActiveColorResource(R.color.divideColor).setActiveColorResource(R.color.mainColor)
                ).initialise();


       mBottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
           @Override
           public void onTabSelected(int position) {
               mTransaction = mManager.beginTransaction();
               switch (position){
                   case 0:

                       mTransaction.replace(R.id.fragment_container,new TeachFallFragment());
                       mTransaction.commit();
                       break;
                   case 1:
                       mTransaction.replace(R.id.fragment_container,new MineFragment());
                       mTransaction.commit();
                       break;
               }

           }

           @Override
           public void onTabUnselected(int position) {

           }

           @Override
           public void onTabReselected(int position) {

           }
       });
       mBottomBar.setFirstSelectedPosition(0);
       mBottomBar.setActiveColor(R.color.mainColor);
        mTransaction = mManager.beginTransaction();
        mTransaction.replace(R.id.fragment_container,new TeachFallFragment());
        mTransaction.commit();
    }



}
