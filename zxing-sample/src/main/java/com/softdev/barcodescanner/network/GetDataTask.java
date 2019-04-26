package com.softdev.barcodescanner.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.softdev.barcodescanner.models.Barcode;
import com.softdev.barcodescanner.models.Store;
import com.softdev.barcodescanner.utils.Constant;
import com.softdev.barcodescanner.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softdev.barcodescanner.R;

/**
 * Creating Get Data Task for Getting Data From Web
 */
public class GetDataTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private ProgressDialog dialog;

    public GetDataTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(mContext);
        dialog.setTitle(R.string.title_network);
        dialog.setMessage(mContext.getResources().getString(R.string.msg_network));
        dialog.show();
    }

    @Nullable
    @Override
    protected Void doInBackground(Void... params) {

        /**
         * Getting JSON Object from Web Using okHttp
         */
        JSONObject jsonObject = JSONParser.getDataFromWeb();

        try {
            /**
             * Check Whether Its NULL???
             */
            if (jsonObject != null) {
                /**
                 * Check Length...
                 */
                if (jsonObject.length() > 0) {
                    /**
                     * Getting Array named "contacts" From MAIN Json Object
                     */
                    JSONArray array = jsonObject.getJSONArray(Constant.JSON_BODY);

                    /**
                     * Check Length of Array...
                     */


                    int lenArray = array.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {

                            JSONObject innerObject = array.getJSONObject(jIndex);
                            int key = Integer.valueOf(innerObject.getString(Constant.JSON_KEY));
                            String code = innerObject.getString(Constant.JSON_CODE);
                            String time = innerObject.getString(Constant.JSON_TIME);
                            int parent = Integer.valueOf(innerObject.getString(Constant.JSON_PARENT));
                            int type = Integer.valueOf(innerObject.getString(Constant.JSON_TYPE));
                            String userid = innerObject.getString(Constant.JSON_USERID);
                            if (userid.equals(Store.getUserId())) {
                                if (parent == 0) {
                                    Store.addBarcode(type, Util.getActionTitle(mContext, type), code, time, key);
                                } else {
                                    Barcode parentBarcode = Store.getBarcode(type, parent);
                                    if (parentBarcode != null) {
                                        parentBarcode.addBarcode(code, time, key);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {

            }
        } catch (JSONException je) {
            Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        /**
         * Checking if List size if more than zero then
         * Update ListView
         */
    }
}