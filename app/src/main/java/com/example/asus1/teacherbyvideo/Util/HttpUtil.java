package com.example.asus1.teacherbyvideo.Util;

import com.example.asus1.teacherbyvideo.Models.ComModel;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {

    public static final  String MAIN_URL = "http://ttms.xuejietech.cn";
    public static  Retrofit MAINClient;


    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();


    public static Retrofit getRetrofit(){
       builder.addInterceptor(new Interceptor() {
           @Override
           public okhttp3.Response intercept(Chain chain) throws IOException {
               Request request = chain.request().newBuilder()
                       .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                       .addHeader("Accept-Encoding", "gzip, deflate")
                       .addHeader("Connection", "keep-alive")
                       .addHeader("Accept", "*/*")
                        .build();
               return chain.proceed(request);
           }
       });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MAIN_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static <T> void doRequest(Call<ComModel<T>> call, final ResquestCallBack<T> callBack){
        call.enqueue(new Callback<ComModel<T>>() {
            @Override
            public void onResponse(Call<ComModel<T>> call, Response<ComModel<T>> response) {
                if(response!=null&&response.isSuccessful()){
                    ComModel<T> model = response.body();
                    callBack.onRespone(model);
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
        void onRespone(ComModel<T> response);
        void onError();

    }

}
