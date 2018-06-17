package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.CourseModel;

import retrofit2.Call;

public interface AllCourseService {

    Call<ComModel<CourseModel>> getAllCourse();
}
