package com.auto.jarvis.libraryicognite.Utils;

/**
 * Created by HaVH on 2/8/17.
 */

public class Constant {
    public static final String IBEACON_INIT_CHECKOUT_ADDRESS = "D8:CF:F3:6B:8E:01";
    public static final String IBEACON_CHECKOUT_COMPLETE_ADDRESS = "CC:82:03:75:2B:1A";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String REFRESH_LIST = "refreshList";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final int LOGIN = 0;
    public static final int CHECK_IN = 1;
    public static final int INIT_CHECKOUT = 2;
    public static final int FINISH_CHECKOUT = 3;
}
