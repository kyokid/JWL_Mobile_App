package com.auto.jarvis.libraryicognite.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.auto.jarvis.libraryicognite.MyApplication;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.activities.BorrowCartActivity;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HaVH on 3/26/17.
 */

public class BackGroundService extends Service {

    private BeaconManager beaconManager;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private int status = 0;

    public static Context context;
    String userId;

    ApiInterface apiInterface;

    InitBorrow initBorrow;

    String message;
    ArrayList<InformationBookBorrowed> recentList;
    Intent borrowIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("BEACON", "REBIND");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        userId = SaveSharedPreference.getUsername(getBaseContext());
        initBorrow = new InitBorrow(userId, "1");
        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(5000, 5000);
        beaconManager.setRangingListener((region, list) -> {
            Log.d("BEACON", "Found beacons: " + list.size());
            status = SaveSharedPreference.getStatusUser(getApplicationContext());
            Log.d("BEACON", "status of user: " + status);
            for (Beacon beacon : list) {
                if (status == Constant.CHECK_IN && beacon.getMacAddress().toStandardString().equals(Constant.IBEACON_INIT_CHECKOUT_ADDRESS)) {

                    initCheckout();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (status == Constant.INIT_CHECKOUT && beacon.getMacAddress().toStandardString().equals(Constant.IBEACON_CHECKOUT_COMPLETE_ADDRESS)) {
                    status = Constant.LOGIN;
                    SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.LOGIN);
                    checkOut();
                    if (beaconManager != null) {
                        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);

                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (beaconManager != null) {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        }
        try {
            beaconManager.connect(() -> beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initCheckout() {
        Observable<RestService<InitBorrow>> init = apiInterface.initCheckout(initBorrow);
        init.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .subscribe(new Subscriber<RestService<InitBorrow>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        message = e.getMessage();
                        if (message != null)
                        LibraryActivity.getInstance().updateTextView(message);
                    }

                    @Override
                    public void onNext(RestService<InitBorrow> initBorrowRestService) {
                        Log.d("BEACON", "INIT");
                        message = initBorrowRestService.getTextMessage();
                        LibraryActivity.getInstance().updateTextView(message);
                        if (initBorrowRestService.getCode().equals("200")) {
                            status = Constant.INIT_CHECKOUT;
                            SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.INIT_CHECKOUT);
                        }
                    }
                });
    }

    private void checkOut() {
        Observable<RestService<List<InformationBookBorrowed>>> checkoutFinish = apiInterface.checkoutFinish(initBorrow);
        checkoutFinish.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RestService<List<InformationBookBorrowed>>>() {
                    @Override
                    public void onCompleted() {
                        if (borrowIntent != null) {
                            if ((MyApplication.isScreenOn() && !MyApplication.isBackgroundMode())
                                    || (!MyApplication.isScreenOn() && MyApplication.isBackgroundMode())) {
                                startActivity(borrowIntent);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        message = e.getMessage();
                        if (message != null)
                        LibraryActivity.getInstance().updateTextView(message);
                    }

                    @Override
                    public void onNext(RestService<List<InformationBookBorrowed>> listRestService) {
                        Log.d("BEACON", "CHECKOUT");
                        message = listRestService.getTextMessage();
                        LibraryActivity.getInstance().updateTextView(message);
                        recentList = (ArrayList<InformationBookBorrowed>) listRestService.getData();
                        if (recentList != null && recentList.size() > 0) {
                            NotificationUtils.showNotification(getApplicationContext(), message, recentList);

                            borrowIntent = new Intent(LibraryActivity.getInstance(), BorrowCartActivity.class);

                            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.PARTIAL_WAKE_LOCK, "checkout");
                            wl.acquire(3000);
                            wl.release();

                            borrowIntent.putExtra("RESULT", true);
                            borrowIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            borrowIntent.putParcelableArrayListExtra("RECENT_LIST", recentList);

                        }
                    }
                });
    }
}

