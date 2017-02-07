package com.auto.jarvis.libraryicognite.activities;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;

/**
 * Created by HaVH on 2/4/17.
 */

public class GlobalVariable extends Application {
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
        EstimoteSDK.initialize(getApplicationContext(), "ibeacondemo-el5", "5111290bf074a3ff553336435d3f91bb");
    }
}
