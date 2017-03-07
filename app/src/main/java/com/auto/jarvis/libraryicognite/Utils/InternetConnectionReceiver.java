package com.auto.jarvis.libraryicognite.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by HaVH on 3/6/17.
 */

public class InternetConnectionReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public InternetConnectionReceiver() {
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = checkInternet(context);
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }


    public static boolean checkInternet(Context ct) {
        return (NetworkUtils.checkConnection(ct));
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
