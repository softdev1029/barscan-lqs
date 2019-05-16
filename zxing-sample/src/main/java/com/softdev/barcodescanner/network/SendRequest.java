package com.softdev.barcodescanner.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.softdev.barcodescanner.adapters.BarcodeAdapter;
import com.softdev.barcodescanner.models.Barcode;
import com.softdev.barcodescanner.models.Store;
import com.softdev.barcodescanner.utils.Constant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import com.lqstc.barscannar.R;

public class SendRequest extends AsyncTask<String, Void, String> {

    private Context mContext;
    private int mAction;
    private Barcode mBag;
    private RecyclerView.Adapter mAdapter;
    private ProgressDialog dialog;

    public SendRequest(Context context, int action, Barcode bag, RecyclerView.Adapter adapter) {
        mContext = context;
        mAction = action;
        mBag = bag;
        mAdapter = adapter;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mContext);
        dialog.setTitle(R.string.title_network);
        dialog.setMessage(mContext.getResources().getString(R.string.msg_network));
        dialog.show();
    }

    protected String doInBackground(String... arg0) {

        HashMap<Integer, Barcode> map = Store.getBarcodeMap(mAction);
        if (mBag != null) {
            map = mBag.getMap();
            sendData(mBag);
        }
        for (HashMap.Entry<Integer, Barcode> entry : map.entrySet()) {
            sendData(entry.getValue());
        }
        return "";
    }

    private String sendData(Barcode barcode) {
        try {

            URL url = new URL(Constant.URL_ADD_CODE);
            JSONObject postDataParams = new JSONObject();

            String id = Constant.GOOGLE_DOC_ID;

            String parentBarcode = "";
            if (barcode.getParentKey() != 0) {
                parentBarcode = Store.getBarcode(barcode.getActionType(), barcode.getParentKey()).getBarcode();
            }

            postDataParams.put(Constant.JSON_KEY, barcode.getKey());
            postDataParams.put(Constant.JSON_CODE, barcode.getBarcode());
            postDataParams.put(Constant.JSON_TIME, barcode.getTime());
            postDataParams.put(Constant.JSON_OBS_POD, barcode.getObsPod());
            postDataParams.put(Constant.JSON_DAMAGE_IMAGE, barcode.getDamageImage());
            postDataParams.put(Constant.JSON_POD_IMAGE, barcode.getPodImage());
            postDataParams.put(Constant.JSON_PARENT, parentBarcode);
            postDataParams.put(Constant.JSON_TYPE, barcode.getActionType());
            postDataParams.put(Constant.JSON_USERID, Store.getUserId());
            postDataParams.put("id", id);


            Log.e("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            } else {
                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
        // clean
        if (mBag == null) {
            Store.deleteBarcode(mAction);
            mAdapter.notifyDataSetChanged();
        } else {
            mBag.deleteBarcode();
            Store.deleteBarcode(mAction, mBag.getKey());
            mAdapter.notifyDataSetChanged();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}