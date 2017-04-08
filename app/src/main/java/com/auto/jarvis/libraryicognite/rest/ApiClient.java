package com.auto.jarvis.libraryicognite.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HaVH on 1/13/17.
 */

public class ApiClient {
//    public static String BASE_URL = "https://jwl-api-v0.herokuapp.com/";
//    public static String BASE_URL = "http://172.20.10.5:8080/";

    public static String BASE_URL = "http://192.168.1.156:8080/";
    public static String URL_CONNECTION = "192.168.1.156";
//    public static String BASE_URL = "http://192.168.0.100:8080/"; //thiendn ip wfchuane

    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public ApiClient() {
    }


}
