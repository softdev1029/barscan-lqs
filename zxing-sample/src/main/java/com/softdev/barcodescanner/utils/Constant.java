package com.softdev.barcodescanner.utils;

public class Constant {
    public static final String ACTION_NAME = "action";
    public static final String BAG_KEY = "bag_key";
    public static final int DEFAULT_ACTION = 0;

    // gateway
    public static final int ACTION_BYPASS = 0;
    public static final int ACTION_AIRPORT = 1;
    public static final int ACTION_DEPARTURE = 2;
    public static final int ACTION_ARRIVAL = 3;

    // driver
    public static final int ACTION_DRIVER_LOAD = 4;

    // delivery
    public static final int ACTION_BAD_ADDRESS = 5;
    public static final int ACTION_REFUSED = 6;
    public static final int ACTION_CLOSED = 7;
    public static final int ACTION_DAMAGE = 8;
    public static final int ACTION_POD = 9;

    public static final int ACTION_COUNT = 10;

    // func error
    public final static int ERROR = -1;
    public final static int SUCCESS = 0;

    // network
    public final static String JSON_BODY = "records";
    public final static String JSON_KEY = "key";
    public final static String JSON_CODE = "barcode";
    public final static String JSON_PARENT = "parent";
    public final static String JSON_TYPE = "type";

    // log
    public final static String TAG = "barscan";
    public final static String TAG_LOGIN = TAG + "login";
}
