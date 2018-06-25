package com.example.asus1.teacherbyvideo.Services;

import com.example.asus1.teacherbyvideo.Models.ComModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SimilarityService {


    @POST("/pic/upLoadPicture")
    @Multipart
    Call<ComModel<Integer>> upLoadImage(@Field("phone") String phone,
                                        @Part("file")RequestBody body);
}
