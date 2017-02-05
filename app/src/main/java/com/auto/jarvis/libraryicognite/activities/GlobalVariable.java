package com.auto.jarvis.libraryicognite.activities;

import android.app.Application;

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
}
