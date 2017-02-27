package com.auto.jarvis.libraryicognite.estimote;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.BorrowCartActivity;
import com.auto.jarvis.libraryicognite.activities.BarCodeActivity;
import com.auto.jarvis.libraryicognite.activities.GlobalVariable;
import com.auto.jarvis.libraryicognite.activities.InsideLibraryActivity;
import com.auto.jarvis.libraryicognite.activities.LibraryActivity;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.InitBorrow;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    private boolean isInit = false;

    public BeaconNotificationManager(final Context context) {
        this.context = context;
        beaconManager = new BeaconManager(context);
        final String username = SaveSharedPreference.getUsername(context);
        beaconManager.setBackgroundScanPeriod(5, 5);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(TAG, "onEnterRegion: " + region.getIdentifier());
                GlobalVariable.identifier = "" + region.getIdentifier();
                GlobalVariable.major = "" + region.getMajor();
                GlobalVariable.minor = "" + region.getMinor();
                GlobalVariable.proximity = "" + region.getProximityUUID();
                String message = enterMessages.get(region.getIdentifier());
                if (message != null) {
                    if (!isInit) {
                        initCheckout(username, message);
                    }

                }
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d(TAG, "onExitedRegion: " + region.getIdentifier());
                String message = exitMessages.get(region.getIdentifier());
                if (message != null) {
                    finishCheckout(username, message);
                    showNotification(message);

                }
            }
        });
    }

    public void addNotification(BeaconID beaconID, String enterMessage, String exitMessage) {
        Region region = beaconID.toBeaconRegion();
        enterMessages.put(region.getIdentifier(), enterMessage);
        exitMessages.put(region.getIdentifier(), exitMessage);
        regionsToMonitor.add(region);
    }

    public void showNotification(String message) {
        Intent resultIntent = new Intent(context, LibraryActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("The Library Checkout")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, builder.build());
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


    private void initCheckout(String username, final String message) {
        InitBorrow initBorrow = new InitBorrow(username, "1");
        Call<RestService<InitBorrow>> callCheckoutInit = apiService.initBorrow(initBorrow);
        callCheckoutInit.enqueue(new Callback<RestService<InitBorrow>>() {
            @Override
            public void onResponse(Call<RestService<InitBorrow>> call, Response<RestService<InitBorrow>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        showNotification(message);
//                        tvLocation.setText("You are in Library");
//                        Toast.makeText(InsideLibraryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        isInit = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<InitBorrow>> call, Throwable t) {
                Toast.makeText(context, "API FAIL WHEN INIT", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void finishCheckout(String username, final String message) {
        InitBorrow initBorrow = new InitBorrow(username, "1");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<List<InformationBookBorrowed>>> callCheckoutInit = apiService.checkout(initBorrow);
        callCheckoutInit.enqueue(new Callback<RestService<List<InformationBookBorrowed>>>() {
            @Override
            public void onResponse(Call<RestService<List<InformationBookBorrowed>>> call, Response<RestService<List<InformationBookBorrowed>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        ArrayList<InformationBookBorrowed> borroweds = (ArrayList<InformationBookBorrowed>) response.body().getData();
                        showNotification(message);
//                        Toast.makeText(InsideLibraryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<List<InformationBookBorrowed>>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
