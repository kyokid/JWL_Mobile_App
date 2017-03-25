package com.auto.jarvis.libraryicognite.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.activities.BorrowCartActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * Created by thiendn on 22/02/2017.
 */

public class FirebaseNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Toast.makeText(getApplicationContext(), "Receive message: " + remoteMessage.getFrom(), Toast.LENGTH_SHORT).show();
//        Log.d("FirebaseNoti: ", remoteMessage.getNotification().getBody());
        String title = remoteMessage.getData().get("title");
        if (title != null && title.equals("Remaining day")) {
            try {
                sendNotification(remoteMessage);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (remoteMessage.getNotification() != null) {
            Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);

            pushNotification.putExtra("message", remoteMessage.getNotification().getBody());
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        }
    }

    private void sendNotification(RemoteMessage remoteMessage) throws UnsupportedEncodingException {
        int notiID = 0;
        String message = URLDecoder.decode(remoteMessage.getData().get("body"), "UTF-8");
        Intent intent = new Intent(this, BorrowCartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_ver2)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(message);
        notificationBuilder.setStyle(bigTextStyle);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notiID++ /* ID of notification */, notificationBuilder.build());

    }
}
