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
import com.softdev.barcodescanner.models.Barcode;
import com.softdev.barcodescanner.models.Store;
import com.softdev.barcodescanner.network.SendRequest;
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
import java.util.HashMap;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.sample.BaseScannerActivity;

public class ScanActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {

    private Context mContext;

    // views
    private Button mBtnPhoto;
    private Button mBtnScan;
    private Button mBtnSend;
    private TextView mTitleView;
    private FrameLayout mScannerLayout;
    private ZXingScannerView mScannerView;
    private ImageView mCameraResultView;
    private RecyclerView mCodeLogView;
    private SignaturePad mSignaturePad;
    private EditText mPodBox;

    // adapter
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // logic
    private int mAction;
    private final String FILE_NAME = "file-name";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private MagicalTakePhoto mMagicalTakePhoto;

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
        mBtnSend = findViewById(R.id.button_send);

        mTitleView = findViewById(R.id.tv_title);

        mScannerLayout = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        mScannerLayout.addView(mScannerView);

        mCameraResultView = findViewById(R.id.iv_camera_result);
        mCameraResultView.setVisibility(View.GONE);

        mCodeLogView = findViewById(R.id.code_log_view);

        mPodBox = findViewById(R.id.et_pod);
        mPodBox.setSelected(false);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
    }

    private void initData() {
        mContext = this;

        Intent i = getIntent();
        mAction = i.getIntExtra(Constant.ACTION_NAME, Constant.DEFAULT_ACTION);

        mTitleView.setText(getResources().getString(Util.getActionTitleId(mAction)) + " for BAG");

        if (mAction == Constant.ACTION_DAMAGE) {
            mBtnPhoto.setVisibility(View.VISIBLE);
        } else {
            mBtnPhoto.setVisibility(View.GONE);
        }

        String data = FileUtil.readData(this, FILE_NAME + mAction);

        mMagicalTakePhoto = new MagicalTakePhoto(this);

        if (mAction == Constant.ACTION_POD) {
            mSignaturePad.setVisibility(View.VISIBLE);
        } else {
            mSignaturePad.setVisibility(View.GONE);
        }

        if (mAction == Constant.ACTION_BAD_ADDRESS ||
                mAction == Constant.ACTION_REFUSED ||
                mAction == Constant.ACTION_CLOSED ||
                mAction == Constant.ACTION_DAMAGE) {
            mPodBox.setText(R.string.lbl_obs);
        } else {
            mPodBox.setText(R.string.lbl_pod_desc);
        }

        if (mAction == Constant.ACTION_BYPASS ||
                mAction == Constant.ACTION_AIRPORT ||
                mAction == Constant.ACTION_DEPARTURE ||
                mAction == Constant.ACTION_ARRIVAL ||
                mAction == Constant.ACTION_DRIVER_LOAD) {
            mPodBox.setVisibility(View.GONE);
        } else {
            mPodBox.setVisibility(View.VISIBLE);
        }

        // adapter
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mCodeLogView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mCodeLogView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BarcodeAdapter(this, Store.getBarcodeMap(mAction));
        mCodeLogView.setAdapter(mAdapter);
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
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRequest(mContext, null, mAction).execute();

                // clean
                Store.deleteBarcode(mAction);
            }
        });
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        String barcode = rawResult.getText();
        String action = Util.getActionTitle(this, mAction);
        int key = Store.addBarcode(mAction, action, barcode, 0);
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

    private void dispatchTakePictureIntent() {
//        mMagicalTakePhoto.takePhoto("my_photo_name");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
            File newdir = new File(dir);
            newdir.mkdirs();
            String file = dir + "1.jpg";
            File newfile = new File(file);

            try {
                newfile.createNewFile();
            } catch (IOException e) {
            }

            Uri outputFileUri = Uri.fromFile(newfile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMagicalTakePhoto.resultPhoto(requestCode, resultCode, data);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        String file = dir + "1.jpg";
        File imgFile = new File(file);

        if (imgFile.exists()) {

            Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mCameraResultView.setImageBitmap(bm);
        }


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
