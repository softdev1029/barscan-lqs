package com.softdev.barcodescanner.utils;

import com.softdev.barcodescanner.R;

public class Util {
    public static int getActionTitleId(int action) {
        // gateway
        if (action == Constant.ACTION_BYPASS) {
            return R.string.lbl_bypass;
        } else if (action == Constant.ACTION_AIRPORT) {
            return R.string.lbl_airport;
        } else if (action == Constant.ACTION_DEPARTURE) {
            return R.string.lbl_departure;
        } else if (action == Constant.ACTION_ARRIVAL) {
            return R.string.lbl_arrival;
        }
        // driver
        else if (action == Constant.ACTION_DRIVER) {
            return R.string.lbl_driver;
        }
        // delivery
        else if (action == Constant.ACTION_BAD_ADDRESS) {
            return R.string.lbl_bad_address;
        } else if (action == Constant.ACTION_REFUSED) {
            return R.string.lbl_refused;
        } else if (action == Constant.ACTION_CLOSED) {
            return R.string.lbl_closed;
        } else if (action == Constant.ACTION_DAMAGE) {
            return R.string.lbl_damage;
        } else if (action == Constant.ACTION_POD) {
            return R.string.lbl_pod;
        }
        return R.string.lbl_gateway;
    }
}
