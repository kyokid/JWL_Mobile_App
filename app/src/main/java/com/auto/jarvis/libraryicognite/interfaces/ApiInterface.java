package com.auto.jarvis.libraryicognite.interfaces;

import com.auto.jarvis.libraryicognite.models.input.InitBorrow;
import com.auto.jarvis.libraryicognite.models.output.Data;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.UserProfile;
import com.auto.jarvis.libraryicognite.models.output.UserProfileInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by HaVH on 1/13/17.
 */

public interface ApiInterface {


    @POST("users/login/")
    Call<RestService<User>> login(@Body User user);

    @POST("init/borrow")
    Call<RestService<InitBorrow>> initBorrow(@Body InitBorrow initBorrow);

    @POST("checkout/")
    Call<RestService<List<InformationBookBorrowed>>> checkout(@Body InitBorrow checkout);

    @POST("getBorrowedBooks/")
    Call<RestService<List<InformationBookBorrowed>>> getBorrowedBook(@Body User userId);

    @GET("users/updateToken")
    Call<RestService<String>> updateGoogleToken(@Query("userId") String userId,
                                                @Query("googleToken") String googleToken);

    @GET("users/{id}/status")
    Call<RestService<Boolean>> inLibrary(@Path("id") String userId);

    @GET("users/{id}")
    Call<RestService<Data>> getProfile(@Path("id") String userId);



}
