package com.auto.jarvis.libraryicognite.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.activities.GlobalVariable;

/**
 * Created by HaVH on 3/6/17.
 */

public class InternetConnectionReceiver extends BroadcastReceiver {

    public InternetConnectionReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (checkInternet(context)) {
            return;
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean checkInternet(Context ct) {
        return (NetworkUtils.checkConnection(ct));
    }

}
