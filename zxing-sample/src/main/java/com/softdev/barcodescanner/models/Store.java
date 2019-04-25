package com.softdev.barcodescanner.models;

import com.softdev.barcodescanner.utils.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Store {

    private static ArrayList<HashMap<Integer, Barcode>> mCodeList = new ArrayList<>();

    public static void configure() {
        for (int i = 0; i < Constant.ACTION_COUNT; i++) {
            HashMap<Integer, Barcode> map = new HashMap<>();
            mCodeList.add(map);
        }
    }

    public static int addBarcode(int actionType, String action, String barcode, int key) {
        if (actionType < 0 || actionType >= Constant.ACTION_COUNT) {
            return Constant.ERROR;
        }
        if (action == null || barcode == null) {
            return Constant.ERROR;
        }
        Barcode code = new Barcode();
        if (key == 0) {
            key = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        }
        code.setKey(key);
        code.setAction(action);
        code.setActionType(actionType);
        code.setBarcode(barcode);
        code.setParentKey(0);
        mCodeList.get(actionType).put(key, code);
        return key;
    }

    public static int deleteBarcode(int actionType, int key) {
        if (actionType < 0 || actionType >= Constant.ACTION_COUNT) {
            return Constant.ERROR;
        }
        mCodeList.get(actionType).remove(key);
        return Constant.SUCCESS;
    }

    public static Barcode getBarcode(int actionType, int key) {
        if (actionType < 0 || actionType >= Constant.ACTION_COUNT) {
            return null;
        }
        return mCodeList.get(actionType).get(key);
    }

    public static Set<Integer> getBarcodeKeys(int actionType) {
        if (actionType < 0 || actionType >= Constant.ACTION_COUNT) {
            return null;
        }
        return mCodeList.get(actionType).keySet();
    }

    public static HashMap<Integer, Barcode> getBarcodeMap(int actionType) {
        if (actionType < 0 || actionType >= Constant.ACTION_COUNT) {
            return null;
        }
        return mCodeList.get(actionType);
    }
}
