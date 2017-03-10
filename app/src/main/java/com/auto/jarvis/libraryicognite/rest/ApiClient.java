package com.auto.jarvis.libraryicognite.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HaVH on 1/13/17.
 */

public class ApiClient {
    public static String URL = "jwl-api-v0.herokuapp.com";
    public static final String BASE_URL = "http://" + URL + "/";


//    public static final String BASE_URL = "http://10.5.50.25:8080/";
//    public static final String BASE_URL = "http://192.168.43.207:8080/"; //thiendn ip wfchuane

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void setURL(String URL) {
        ApiClient.URL = URL;
    }
}
