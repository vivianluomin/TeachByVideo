package com.example.asus1.teacherbyvideo;

import android.hardware.Camera;

public class RecodeVideo {

    private Camera mCamera;

    private void init(){
        mCamera = Camera.open(1);

    }

}
