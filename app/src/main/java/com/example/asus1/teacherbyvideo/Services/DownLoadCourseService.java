package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.DownLoadCourseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DownLoadCourseService {


    @GET("/course /queryUserDownLoadCourseList/{phone}")
    Call<ComModel<List<DownLoadCourseModel>>> getDownloadCourse(@Path("phone") String phone);
}
