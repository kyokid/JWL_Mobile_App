package com.auto.jarvis.libraryicognite.Utils;

import android.util.Log;

import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.estimote.sdk.EstimoteSDK.getApplicationContext;

/**
 * Created by thiendn on 22/02/2017.
 */

public class NotificationUtils {
    public static void sendNewIdToServer(String googleToken){
        ApiInterface apiService;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        String userId = SaveSharedPreference.getUsername(getApplicationContext());
        Call<RestService<String>> updateGoogleToken = apiService.updateGoogleToken(userId, googleToken);
        updateGoogleToken.enqueue(new Callback<RestService<String>>() {
            @Override
            public void onResponse(Call<RestService<String>> call, Response<RestService<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("generation ", response.body().isSucceed() + " ");
                }
            }

            @Override
            public void onFailure(Call<RestService<String>> call, Throwable t) {

            }
        });
    }
}
