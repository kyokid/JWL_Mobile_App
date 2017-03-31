package com.auto.jarvis.libraryicognite.interfaces;

import com.auto.jarvis.libraryicognite.models.Book;
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
import rx.Observable;

/**
 * Created by HaVH on 1/13/17.
 */

public interface ApiInterface {


    @POST("user/login/")
    Call<RestService<User>> login(@Body User user);

    @POST("user/login")
    Observable<RestService<User>> loginUser(@Body User user);

    @POST("init/borrow")
    Call<RestService<InitBorrow>> initBorrow(@Body InitBorrow initBorrow);

    @POST("init/borrow")
    Observable<RestService<InitBorrow>> initCheckout(@Body InitBorrow initBorrow);

    @POST("checkout/")
    Call<RestService<List<InformationBookBorrowed>>> checkout(@Body InitBorrow checkout);

    @POST("checkout")
    Observable<RestService<List<InformationBookBorrowed>>> checkoutFinish(@Body InitBorrow checkout);

    @POST("getBorrowedBooks/")
    Call<RestService<List<InformationBookBorrowed>>> getBorrowedBook(@Body User userId);

    @GET("users/updateToken")
    Call<RestService<String>> updateGoogleToken(@Query("userId") String userId,
                                                @Query("googleToken") String googleToken);

    @GET("users/{id}/status")
    Call<RestService<Boolean>> inLibrary(@Path("id") String userId);

    @GET("users/{id}")
    Call<RestService<Data>> getProfile(@Path("id") String userId);

    @GET("/users/{id}/requestKey")
    Call<RestService<String>> requestPrivateKey(@Path("id") String userId);

    @GET("/users/{id}/requestKey")
    Observable<RestService<String>> requestNewKey(@Path("id") String userId);

    @GET("/renew/{rfid}")
    Call<RestService<InformationBookBorrowed>> renewBorrowedBook(@Path("rfid") String rfid);

    @GET("/books/search")
    Call<RestService<List<Book>>> search(@Query("search_term") String searchKey,
                                         @Query("user_id") String userId);

    @GET("/books/{id}")
    Call<RestService<List<Book>>> getBookDetail(@Path("id") String id);

    @GET("/wishlist/add")
    Call<RestService<Book>> addToWishList(@Query("user_id") String userId,
                                          @Query("book_id") int bookId);
    @GET("/wishlist/remove")
    Call<RestService<Book>> removeFromWishList(@Query("user_id") String userId,
                                          @Query("book_id") int bookId);

    @GET("users/{id}/status")
    Observable<RestService<Boolean>> userStatus(@Path("id") String userId);

    @GET("getSystemDate")
    Observable<RestService<String>> getCurrentDate();

    @GET("history/borrowed_books/{userId}")
    Observable<RestService<List<InformationBookBorrowed>>> getBorrowedBooksHistory(@Path("userId") String userId);

}
