package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.DownLoadedCourseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DownLoadCourseService {


    @GET("/course /queryUserDownLoadCourseList/{phone}")
    Call<ComModel<List<DownLoadedCourseModel>>>
    getDownloadCourse(@Path("phone") String phone);

    @POST("/course /downLoadCourse")
    @FormUrlEncoded
    Call<ComModel<String>> downLoadCourse(@Field("courseid ") int courseId,
                                          @Field("phone") String phone);
}
