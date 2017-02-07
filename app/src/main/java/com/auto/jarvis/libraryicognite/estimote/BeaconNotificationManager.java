package com.auto.jarvis.libraryicognite.estimote;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by HaVH on 1/31/17.
 */

public class BeaconNotificationManager {
    private static final String TAG = "BeaconNotification";

    private BeaconManager beaconManager;

    private List<Region> regionsToMonitor = new ArrayList<>();
    private HashMap<String, String> enterMessages = new HashMap<>();
    private HashMap<String, String> exitMessages = new HashMap<>();
    private Context context;
    private int notificationID = 0;

    public BeaconNotificationManager(final Context context) {
        this.context = context;
        beaconManager = new BeaconManager(context);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(TAG, "onEnterRegion: " + region.getIdentifier());
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d(TAG, "onExitedRegion: " + region.getIdentifier());
            }
        });
    }



    public void startMonitoring(){
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for (Region region: regionsToMonitor) {
                    beaconManager.startMonitoring(region);
                }
            }
        });
    }

}
