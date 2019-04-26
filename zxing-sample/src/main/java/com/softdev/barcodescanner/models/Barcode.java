package com.softdev.barcodescanner.models;

import com.softdev.barcodescanner.utils.Constant;

import java.util.Date;
import java.util.HashMap;

public class Barcode {
    private String mBarcode = null;
    private int mActionType = Constant.DEFAULT_ACTION;
    private String mAction = null;
    private int mKey = 0;
    private int mParentKey = 0;
    private HashMap<Integer, Barcode> mMap = new HashMap<>();

    public String getBarcode() {
        return mBarcode;
    }

    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }

    public int getActionType() {
        return mActionType;
    }

    public void setActionType(int actionType) {
        mActionType = actionType;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public HashMap<Integer, Barcode> getMap() {
        return mMap;
    }

    public void setmMap(HashMap<Integer, Barcode> map) {
        this.mMap = map;
    }

    public int addBarcode(String barcode) {
        if (barcode == null) {
            return Constant.ERROR;
        }
        Barcode code = new Barcode();
        int key = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        code.setKey(key);
        code.setAction(mAction);
        code.setActionType(mActionType);
        code.setBarcode(barcode);
        code.setParentKey(mKey);
        mMap.put(key, code);
        return key;
    }

    public int deleteBarcode(int key) {
        mMap.remove(key);
        return Constant.SUCCESS;
    }

    public int deleteBarcode() {
        mMap.clear();
        return Constant.SUCCESS;
    }

    public Barcode getBarcode(int key) {
        return mMap.get(key);
    }

    public int getKey() {
        return mKey;
    }

    public void setKey(int key) {
        mKey = key;
    }

    public int getParentKey() {
        return mParentKey;
    }

    public void setParentKey(int parentKey) {
        this.mParentKey = parentKey;
    }
}
