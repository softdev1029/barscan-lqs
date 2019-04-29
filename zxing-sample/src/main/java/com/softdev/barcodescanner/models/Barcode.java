package com.softdev.barcodescanner.models;

import android.text.format.DateFormat;

import com.softdev.barcodescanner.utils.Constant;

import java.util.Date;
import java.util.HashMap;

public class Barcode {
    private String mBarcode = null;
    private String mTime = null;
    private String mObsPod = "";
    private String mDamageImage = "";
    private String mPodImage = "";
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

    public void setMap(HashMap<Integer, Barcode> map) {
        mMap = map;
    }

    public int addBarcode(String barcode, String time, int key) {
        if (barcode == null) {
            return Constant.ERROR;
        }
        Barcode code = new Barcode();
        Date d = new Date();
        if (time == null) {
            time = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime()).toString();
        }
        code.setTime(time);
        if (key == 0){
            key = (int) ((d.getTime() / 1000L) % Integer.MAX_VALUE);
        }
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

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getDamageImage() {
        return mDamageImage;
    }

    public void setDamageImage(String damageImage) {
        mDamageImage = damageImage;
    }

    public String getPodImage() {
        return mPodImage;
    }

    public void setPodImage(String podImage) {
        mPodImage = podImage;
    }

    public String getObsPod() {
        return mObsPod;
    }

    public void setObsPod(String obsPod) {
        mObsPod = obsPod;
    }
}
