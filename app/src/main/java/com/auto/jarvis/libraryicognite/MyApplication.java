package com.auto.jarvis.libraryicognite;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.estimote.sdk.EstimoteSDK;

import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

/**
 * Created by HaVH on 2/4/17.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {


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
        registerActivityLifecycleCallbacks(this);
        EstimoteSDK.initialize(getApplicationContext(), "ibeacondemo-el5", "5111290bf074a3ff553336435d3f91bb");


    }

    public static boolean backgroundMode;

    public static boolean isBackgroundMode() {
        return backgroundMode;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++start;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resume;
        checkStatusActivity();
        checkMonitor();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++pause;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stop;
        checkStatusActivity();
        checkMonitor();
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

    private boolean checkMonitor() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            screenOn = pm.isInteractive();
        }
        return screenOn;

    }
    private static boolean screenOn;

    public static boolean isScreenOn() {
        return screenOn;
    }
}
