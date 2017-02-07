package com.auto.jarvis.libraryicognite.interfaces;

import com.auto.jarvis.libraryicognite.models.input.InitBorrow;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.models.input.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HaVH on 1/13/17.
 */

public interface ApiInterface {

    @GET("getAll/")
    Call<List<User>> getAllUser();

    @POST("users/login/")
    Call<RestService<User>> login(@Body User user);

    @POST("init/borrow")
    Call<RestService<InitBorrow>> initBorrow(@Body InitBorrow initBorrow);



}
