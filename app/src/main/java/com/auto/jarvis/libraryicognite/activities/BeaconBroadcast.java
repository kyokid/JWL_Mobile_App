package com.auto.jarvis.libraryicognite.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.auto.jarvis.libraryicognite.estimote.BeaconID;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by HaVH on 2/27/17.
 */

public class BeaconBroadcast extends BroadcastReceiver{

    public static final String DEFAULT_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FEED";
    public static final String DEFAULT_IDENTIFIER = "rid";
    private static final Region ALL_ESTIMOTE_BEACON_REGION = new Region(DEFAULT_IDENTIFIER,
            UUID.fromString(DEFAULT_UUID), null, null);

    private BeaconManager beaconManager;
    ApiInterface apiService;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BROADTCAST", "running");
//        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                List<BeaconID> beaconIDs = new ArrayList<BeaconID>();
                for (Beacon beacon : list) {
                    beaconIDs.add(BeaconID.fromEstimote(beacon));
                }

//                startCheckout(region, beaconIDs, SaveSharedPreference.getUsername(get.this));
            }
        });
    }
}
