package com.example.asus1.teacherbyvideo.Util;

import android.os.Environment;

import java.io.File;

public class Constant {

    public static final String DIR_NAME = "AVRecReal";

    public static final File DIR =new File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            , DIR_NAME);
}
