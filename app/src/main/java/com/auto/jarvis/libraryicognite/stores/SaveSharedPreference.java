package com.auto.jarvis.libraryicognite.stores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HaVH on 2/4/17.
 */

public class SaveSharedPreference {

    static final String USERNAME = "username";
    static final String STATUS = "status";
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

    public static void clearAll(Context ct) {
        SharedPreferences.Editor editor = getSharedPreference(ct).edit().clear();
        editor.apply();
    }

    /*
    Status of user after doing something
    default  = 0
     1 = login
     2 = check in
     3 = init
      */
    public static void setStatusUser(Context ct, int status) {
        SharedPreferences.Editor editor = getSharedPreference(ct).edit();
        editor.putInt(STATUS, status);
        editor.apply();
    }

    public static int getStatusUser(Context ct) {
        return getSharedPreference(ct).getInt(STATUS, 0);
    }
}
