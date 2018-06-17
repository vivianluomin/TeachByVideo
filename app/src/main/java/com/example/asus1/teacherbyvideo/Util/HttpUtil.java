package com.example.asus1.teacherbyvideo.Util;

import com.example.asus1.teacherbyvideo.Models.ComModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {

    public static final  String MAIN_URL = "http://ttms.xuejietech.cn";
    public static  Retrofit MAINClient;


    public static Retrofit getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static <T> void doRequest(Call<ComModel<T>> call, final ResquestCallBack<T> callBack){
        call.enqueue(new Callback<ComModel<T>>() {
            @Override
            public void onResponse(Call<ComModel<T>> call, Response<ComModel<T>> response) {
                if(response!=null&&response.isSuccessful()){
                    callBack.onRespone(response.body().getmData());
                }
            }

            @Override
            public void onFailure(Call<ComModel<T>> call, Throwable t) {
                    callBack.onError();
            }
        });
    }

    public static <T>  T getService(Class<T> cls){
        if(MAINClient == null){
            MAINClient = getRetrofit();
        }

        return  MAINClient.create(cls);
    }


    public  interface ResquestCallBack<T>{
        void onRespone(T response);
        void onError();

    }

}
