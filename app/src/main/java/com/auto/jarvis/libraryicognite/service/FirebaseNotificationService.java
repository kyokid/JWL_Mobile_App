package com.auto.jarvis.libraryicognite.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by thiendn on 22/02/2017.
 */

public class FirebaseNotificationService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Toast.makeText(getApplicationContext(), "Receive message: " + remoteMessage.getFrom(), Toast.LENGTH_SHORT).show();
        Log.d("FirebaseNoti: " , remoteMessage.getNotification().getBody());
        Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", remoteMessage.getNotification().getBody());
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }
}
