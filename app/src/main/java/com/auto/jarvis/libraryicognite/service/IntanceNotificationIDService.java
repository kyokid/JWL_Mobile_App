package com.auto.jarvis.libraryicognite.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by thiendn on 22/02/2017.
 */

public class IntanceNotificationIDService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        System.out.println("In InstanceNotificationIDService");
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Intent registrationComplete = new Intent(Constant.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendNewIdToServer(){

    }
}
