package com.softdev.barcodescanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicaltakephoto.MagicalTakePhoto;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.zxing.Result;
import com.softdev.barcodescanner.adapters.BarcodeAdapter;
import com.softdev.barcodescanner.interfaces.ISendCode;
import com.softdev.barcodescanner.models.Barcode;
import com.softdev.barcodescanner.models.Store;
import com.softdev.barcodescanner.network.CheckUser;
import com.softdev.barcodescanner.network.SendRequest;
import com.softdev.barcodescanner.utils.Constant;
import com.softdev.barcodescanner.utils.FileUtil;
import com.softdev.barcodescanner.utils.ImageUtil;
import com.softdev.barcodescanner.utils.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.sample.BaseScannerActivity;
import com.lqstc.barscannar.R;
public class ScanActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler, ISendCode {

    private Context mContext;

    // views
    private Button mBtnScan;
    private Button mBtnSend;
    private TextView mTitleView;
    private FrameLayout mScannerLayout;
    private ZXingScannerView mScannerView;
    private RecyclerView mCodeLogView;

    // adapter
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // logic
    private int mAction;
    private final String FILE_NAME = "file-name";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan);
        setupToolbar();

        initView();
        initData();
        setViewHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    private void initView() {
        mBtnScan = findViewById(R.id.button_scan);
        mBtnSend = findViewById(R.id.button_send);

        mTitleView = findViewById(R.id.tv_title);

        mScannerLayout = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        mScannerLayout.addView(mScannerView);

        mCodeLogView = findViewById(R.id.code_log_view);
    }

    private void initData() {
        mContext = this;

        Intent i = getIntent();
        mAction = i.getIntExtra(Constant.ACTION_NAME, Constant.DEFAULT_ACTION);

        mTitleView.setText(getResources().getString(Util.getActionTitleId(mAction)) + " for BAG");

        String data = FileUtil.readData(this, FILE_NAME + mAction);

        // adapter
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mCodeLogView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mCodeLogView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BarcodeAdapter(this, Store.getBarcodeMap(mAction), true);
        mCodeLogView.setAdapter(mAdapter);
    }

    private void setViewHandler() {
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckUser(mContext).execute();
            }
        });
    }

    public void sendBarcode() {
        new SendRequest(mContext, null, mAction, mAdapter).execute();
    }
    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        String barcode = rawResult.getText();
        String action = Util.getActionTitle(this, mAction);
        int key = Store.addBarcode(mAction, action, barcode, null, 0);
        mAdapter.notifyDataSetChanged();


        String data = action + ": " + rawResult.getText() + ", " + rawResult.getBarcodeFormat();
        FileUtil.writeData(this, FILE_NAME + mAction, data);
        String readData = FileUtil.readData(this, FILE_NAME + mAction);

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 2000);
    }
}
