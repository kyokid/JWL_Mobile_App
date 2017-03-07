package com.auto.jarvis.libraryicognite.activities;

import android.app.Application;

import com.auto.jarvis.libraryicognite.Utils.InternetConnectionReceiver;
import com.auto.jarvis.libraryicognite.estimote.BeaconID;
import com.auto.jarvis.libraryicognite.estimote.BeaconNotificationManager;
import com.estimote.sdk.EstimoteSDK;

/**
 * Created by HaVH on 2/4/17.
 */

public class GlobalVariable extends Application {

    private static GlobalVariable instance;

    private boolean beaconNotificationsEnabled = false;
    public static String identifier = "";
    public static String major = "";
    public static String minor = "";
    public static String proximity = "";
    private String username;

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
        EstimoteSDK.initialize(getApplicationContext(), "ibeacondemo-el5", "5111290bf074a3ff553336435d3f91bb");
    }

    public static synchronized GlobalVariable getInstance() {
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
}
