package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.auto.jarvis.libraryicognite.LoginActivity;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ApiInterface apiService;
    boolean inLibrary = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String userId = SaveSharedPreference.getUsername(this);
        if (userId.length() == 0 || userId.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<RestService<Boolean>> result = apiService.inLibrary(userId);
            result.enqueue(new Callback<RestService<Boolean>>() {
                @Override
                public void onResponse(Call<RestService<Boolean>> call, Response<RestService<Boolean>> response) {
                    inLibrary = response.body().getData();
                    if (inLibrary) {
                        Intent intentLibrary = new Intent(HomeActivity.this, LibraryActivity.class);
                        intentLibrary.putExtra("IN_LIBRARY", true);
                        startActivity(intentLibrary);
                    }
                }


                @Override
                public void onFailure(Call<RestService<Boolean>> call, Throwable t) {

                }
            });
            Log.d("LIBRARY", " " + inLibrary);
            if (!inLibrary) {
                Intent barCodeIntent = new Intent(this, BarCodeActivity.class);
                barCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(barCodeIntent);
            }

        }
    }
}
