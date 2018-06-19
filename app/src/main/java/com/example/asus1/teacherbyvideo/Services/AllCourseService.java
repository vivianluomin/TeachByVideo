package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.CourseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AllCourseService {

    @GET("/course/queryAllCourseList")
    Call<ComModel<List<CourseModel>>> getAllCourse();
}
