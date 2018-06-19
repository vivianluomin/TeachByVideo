package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.LoadModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoadService {

    @POST("/usr/login")
    @FormUrlEncoded
    Call<ComModel<LoadModel>> getLoadInfo(@Field("phone") String phone,
                                          @Field("password") String password);

}
