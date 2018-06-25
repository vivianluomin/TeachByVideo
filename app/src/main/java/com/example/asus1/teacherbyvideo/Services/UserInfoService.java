package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.UserInfoModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserInfoService {

    @POST("/usr/queryUserDesc")
    @FormUrlEncoded
    Call<ComModel<UserInfoModel>> getUserInfo(@Field("phone") String phone);

    @POST("/usr/updateUserDesc")
    @FormUrlEncoded
    Call<ComModel<String>> updateUserInfo(@Field("sportcount") int sportCount,
                                          @Field("usersporttime") String sportTime,
                                          @Field("userscore") int score);
}
