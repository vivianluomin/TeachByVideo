package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.LoadModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface LoadService {

    @GET("/usr/login")
    Call<ComModel<LoadModel>> getLoadInfo(@Field("phone") String phone,
                                          @Field("password") String password);

}
