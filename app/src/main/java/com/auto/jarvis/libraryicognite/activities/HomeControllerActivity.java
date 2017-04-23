package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeControllerActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    private String userId;
    boolean  statusUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_controller);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        userId = SaveSharedPreference.getUsername(getBaseContext());
        Log.d("START_ACTIVITY", "HOMECONTROLLER");
        checkStatusBorrower(userId)
                .delay(1, TimeUnit.SECONDS)
                .subscribe(booleanRestService -> {
                    Intent intentController;
                    if (booleanRestService.getData()) {
                        intentController = new Intent(HomeControllerActivity.this, LibraryActivity.class);
                        SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
                        intentController.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentController);
                        finish();
                    } else {
                        intentController = new Intent(HomeControllerActivity.this, BarCodeActivity.class);
                        intentController.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentController);
                        finish();
                    }
                });
    }

    private Observable<RestService<Boolean>> checkStatusBorrower(String userId) {
        return apiInterface.userStatus(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
