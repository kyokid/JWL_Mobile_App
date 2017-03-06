package com.auto.jarvis.libraryicognite.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by HaVH on 3/6/17.
 */

public class NetworkUtils {


    public static boolean checkConnection(Context ct) {
        ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nw = cm.getActiveNetworkInfo();
        return (nw!= null && nw.isConnected());

    }
}
