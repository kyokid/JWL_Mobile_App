package com.auto.jarvis.libraryicognite.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.auto.jarvis.libraryicognite.Utils.NotificationUtils.sendNewIdToServer;

/**
 * Created by thiendn on 22/02/2017.
 */

public class IntanceNotificationIDService extends FirebaseInstanceIdService{
    ApiInterface apiService;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Intent registrationComplete = new Intent(Constant.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
//        sendNewIdToServer(refreshedToken);
    }


}
