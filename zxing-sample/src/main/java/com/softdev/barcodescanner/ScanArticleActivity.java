package com.softdev.barcodescanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.sample.BaseScannerActivity;

public class ScanArticleActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {

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

        mTitleView.setText(getResources().getString(Util.getActionTitleId(mAction)) + " for ARTICLE");

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
                new AlertDialog.Builder(ScanArticleActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(R.string.msg_delete)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCodeLogView.setText("");
                                FileUtil.clearData(ScanArticleActivity.this, FILE_NAME + mAction);
                            }
                        })
                        .setNegativeButton(R.string.lbl_no, null)
                        .show();
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
                mScannerView.resumeCameraPreview(ScanArticleActivity.this);
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
