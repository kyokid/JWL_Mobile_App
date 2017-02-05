package com.auto.jarvis.libraryicognite.stores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HaVH on 2/4/17.
 */

public class SaveSharedPreference {

    static final String USERNAME = "username";
    static SharedPreferences getSharedPreference(Context ct) {
        return PreferenceManager.getDefaultSharedPreferences(ct);
    }

    public static void setUsername(Context ct, String usernamne) {
        SharedPreferences.Editor editor = getSharedPreference(ct).edit();
        editor.putString(USERNAME, usernamne);
        editor.apply();
    }
    public static String getUsername(Context ct) {
        return getSharedPreference(ct).getString(USERNAME, "");
    }
}
