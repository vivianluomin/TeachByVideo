package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoadService {

    @POST("/usr/login")
    @FormUrlEncoded
    Call<ComModel<UserModel>> getLoadInfo(@Field("phone") String phone,
                                          @Field("password") String password);

    @GET("/usr/check/{phone}")
    Call<ComModel<String>> checkPhone(@Path("phone") String phone);

    @POST("/usr/register/")
    @FormUrlEncoded
    Call<ComModel<String>> registerPhone(@Field("phone") String phone,
                                         @Field("password") String password);


}
