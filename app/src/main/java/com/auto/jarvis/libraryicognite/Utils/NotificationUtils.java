package com.auto.jarvis.libraryicognite.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.activities.BorrowCartActivity;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.estimote.sdk.EstimoteSDK.getApplicationContext;

/**
 * Created by thiendn on 22/02/2017.
 */

public class NotificationUtils {



    public static void sendNewIdToServer(String userId, String googleToken){
        ApiInterface apiService;
        apiService = ApiClient.getClient().create(ApiInterface.class);
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

    public static void showNotification(Context context, String message, ArrayList<InformationBookBorrowed> recentList) {
        int notificationID = 0;
        Intent resultIntent = new Intent(context, BorrowCartActivity.class);
        //flag = 1: go to recent list
        resultIntent.putExtra("RESULT", true);
        resultIntent.putParcelableArrayListExtra("RECENT_LIST", recentList);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_ver2)
                .setContentTitle("The Library")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, builder.build());
    }
}
