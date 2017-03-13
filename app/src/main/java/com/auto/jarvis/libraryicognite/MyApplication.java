package com.auto.jarvis.libraryicognite;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.auto.jarvis.libraryicognite.Utils.InternetConnectionReceiver;
import com.auto.jarvis.libraryicognite.estimote.BeaconID;
import com.auto.jarvis.libraryicognite.estimote.BeaconNotificationManager;
import com.estimote.sdk.EstimoteSDK;

/**
 * Created by HaVH on 2/4/17.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MyApplication instance;

    private boolean beaconNotificationsEnabled = false;
    public static String identifier = "";
    public static String major = "";
    public static String minor = "";
    public static String proximity = "";
    private String username;

    private int resume;
    private int pause;
    private int start;
    private int stop;

    String mode = "";


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }





    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(this);
        EstimoteSDK.initialize(getApplicationContext(), "ibeacondemo-el5", "5111290bf074a3ff553336435d3f91bb");
    }




    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public void enableBeaconNotifications() {
        if (beaconNotificationsEnabled) {
            return;
        }

        BeaconNotificationManager beaconNotificationsManager = new BeaconNotificationManager(this);

        beaconNotificationsManager.addNotification(new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FEED", 1, 1), "Init Checkout", "Success, Thank you");
        beaconNotificationsManager.startMonitoring();
        beaconNotificationsEnabled = true;
    }

    public boolean isBeaconNotificationsEnabled() {
        return beaconNotificationsEnabled;
    }

    public void setConnectivityListener(InternetConnectionReceiver.ConnectivityReceiverListener listener) {
        InternetConnectionReceiver.connectivityReceiverListener = listener;
    }

    public static boolean backgroundMode;

    public static boolean isBackgroundMode() {
        return backgroundMode;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.i("BACKGROUND", "create");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i("BACKGROUND", "started");
        ++start;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i("BACKGROUND", "resumed");
        ++resume;
        checkStatusActivity();
        Log.i("BACKGROUND", "BACKGROUND MODE IS " + backgroundMode);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i("BACKGROUND", "pause");
        ++pause;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i("BACKGROUND", "stopped");
        ++stop;
        checkStatusActivity();
        Log.i("BACKGROUND", "BACKGROUND MODE IS " + backgroundMode);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private void checkStatusActivity() {
        if (start == stop) {
            backgroundMode = true;
        } else if (start > stop) {
            backgroundMode = false;
        }
    }
}
