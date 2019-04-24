package com.softdev.barcodescanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.softdev.barcodescanner.utils.Constant;
import com.softdev.barcodescanner.utils.FileUtil;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.sample.BaseScannerActivity;

public class ScanActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {

    // views
    private Button mBtnPhoto;
    private Button mBtnScan;
    private Button mBtnDelete;
    private TextView mTitleView;
    private FrameLayout mScannerLayout;
    private ZXingScannerView mScannerView;
    private ImageView mCameraResultView;
    private TextView mCodeLogView;
    private EditText mPodBox;

    // logic
    private int mAction;
    private final String FILE_NAME = "file-name";

    static final int REQUEST_IMAGE_CAPTURE = 1;

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
        mBtnPhoto = findViewById(R.id.button_photo);
        mBtnScan = findViewById(R.id.button_scan);
        mBtnDelete = findViewById(R.id.button_delete);

        mTitleView = findViewById(R.id.tv_title);

        mScannerLayout = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        mScannerLayout.addView(mScannerView);

        mCameraResultView = findViewById(R.id.iv_camera_result);
        mCameraResultView.setVisibility(View.GONE);

        mCodeLogView = findViewById(R.id.tv_code_log);

        mPodBox = findViewById(R.id.et_pod);
        mPodBox.setSelected(false);
    }

    private void initData() {
        Intent i = getIntent();
        mAction = i.getIntExtra(Constant.ACTION_NAME, Constant.DEFAULT_ACTION);

        mTitleView.setText(getResources().getString(Util.getActionTitleId(mAction)) + " (for BAG)");

        if (mAction == Constant.ACTION_DAMAGE) {
            mBtnPhoto.setVisibility(View.VISIBLE);
        } else {
            mBtnPhoto.setVisibility(View.GONE);
        }

        String data = FileUtil.readData(this, FILE_NAME + mAction);
        mCodeLogView.setText(data);
    }

    private void setViewHandler() {
        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerLayout.setVisibility(View.VISIBLE);
                mCameraResultView.setVisibility(View.GONE);
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeLogView.setText("");
                FileUtil.clearData(ScanActivity.this, FILE_NAME + mAction);
            }
        });
        mCodeLogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, ScanArticleActivity.class);
                intent.putExtra(Constant.ACTION_NAME, mAction);
                startActivity(intent);
            }
        });
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        String data = getResources().getString(Util.getActionTitleId(mAction)) + ": " + rawResult.getText() + ", " + rawResult.getBarcodeFormat();
        FileUtil.writeData(this, FILE_NAME + mAction, data);
        String readData = FileUtil.readData(this, FILE_NAME + mAction);
        mCodeLogView.setText(readData);

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mCameraResultView.setImageBitmap(imageBitmap);
            mCameraResultView.setVisibility(View.VISIBLE);
            mScannerLayout.setVisibility(View.GONE);
        }
    }
}
