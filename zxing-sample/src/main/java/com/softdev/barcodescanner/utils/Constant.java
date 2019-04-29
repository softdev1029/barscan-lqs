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
    public final static String JSON_TIME = "time";
    public final static String JSON_OBS_POD = "obs_pod";
    public final static String JSON_DAMAGE_IMAGE = "damage_image";
    public final static String JSON_POD_IMAGE = "pod_image";
    public final static String JSON_PARENT = "parent";
    public final static String JSON_TYPE = "type";
    public final static String JSON_USERID = "userid";

    public final static String URL_GET_CODE   = "https://script.google.com/macros/s/AKfycbyuX2dvx-iTuy9lOpk1XfxiYCoQdcSZcL4OqQ8PRx3BhSU4MUY/exec";
    public final static String URL_ADD_CODE   = "https://script.google.com/macros/s/AKfycby9scXmuEz5rULglulTnI5Ad2gNLUTnphVedOXhbymDMJAigt4k/exec";
    public final static String URL_CHECK_USER = "https://script.google.com/macros/s/AKfycbwM2v5GBU0q-hUyuCCbUUBYMZ1DSoQNwNEvdcVc2yxLHXUgKqPS/exec";
    public final static String GOOGLE_DOC_ID  = "1CMRxs1ptDIfiqcnOxYrK6F9NvKAoRRQILBGHqSXd94Q";//"1l-Akhh1hF6e6-9WmPgGEko9pS6crYyz5X6OJ8mqOO0Y";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    // log
    public final static String TAG = "barscan";
    public final static String TAG_LOGIN = TAG + "login";
}
