package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.CommentModel;
import com.example.asus1.teacherbyvideo.Models.CourseBarModel;
import com.example.asus1.teacherbyvideo.Models.CourseDetailModel;
import com.example.asus1.teacherbyvideo.Models.CourseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AllCourseService {

    @GET("/course/queryAllCourseList")
    Call<ComModel<List<CourseModel>>> getAllCourse();


    @GET("/course/queryCurrentCourse")
    Call<ComModel<List<CourseBarModel>>> getCourseBar();

    @GET("/course/queryCurrentCoursePJ")
    Call<ComModel<List<CommentModel>>> getComment();

    @POST("/course/selectCourse")
    @FormUrlEncoded
    Call<ComModel<String>> selectCourse(@Field("courseid ") int courseId,
                                        @Field("phone") String phone);



    @POST("/course/deleteCourse")
    @FormUrlEncoded
    Call<ComModel<String>> DeleteCourse(@Field("courseid ") int courseId,
                                        @Field("phone") String phone);


 }
