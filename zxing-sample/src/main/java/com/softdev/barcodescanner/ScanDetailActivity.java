package com.softdev.barcodescanner;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.sample.BaseScannerActivity;

import com.lqstc.barscannar.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ScanDetailActivity extends AppCompatActivity {

    private Context mContext;

    // views
    private Button mBtnPhoto;
    private Button mBtnOk;
    private TextView mTitleView;
    private ImageView mCameraResultView;
    private SignaturePad mSignaturePad;
    private EditText mPodBox;

    // logic
    private int mAction;
    private boolean mIsSignature = false;
    private Uri mImageFileUri;

    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.CAMERA"};

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan_detail);

        initView();
        initData();
        setViewHandler();

        initPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView() {
        mBtnPhoto = findViewById(R.id.button_photo);
        mBtnOk = findViewById(R.id.button_ok);

        mTitleView = findViewById(R.id.tv_title);

        mCameraResultView = findViewById(R.id.iv_camera_result);
        mCameraResultView.setVisibility(View.INVISIBLE);

        mPodBox = findViewById(R.id.et_pod);
        mPodBox.setSelected(false);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
    }

    private void initData() {
        mContext = this;

        Intent i = getIntent();
        mAction = i.getIntExtra(Constant.ACTION_NAME, Constant.DEFAULT_ACTION);

        mTitleView.setText(getResources().getString(Util.getActionTitleId(mAction)) + " for Detail");

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
    }

    private void setViewHandler() {
        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Barcode code = Store.getCurrentBarcode();

                code.setObsPod(mPodBox.getText().toString());
                if (mIsSignature) {
                    code.setPodImage(ImageUtil.getStringImage(mSignaturePad.getSignatureBitmap()));
                }

                mSignaturePad.clear();
                mCameraResultView.setImageResource(0);

                onBackPressed();
            }
        });
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                mIsSignature = true;
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

//        mCameraResultView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mCameraResultView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                mCameraResultView.getHeight(); //height is ready
//            }
//        });
    }

    private void initPermission() {
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            Barcode code = Store.getCurrentBarcode();
            String imageFileName = "damage_" + code.getKey() + ".png";
            File outputImageFile = new File(getExternalCacheDir(), imageFileName);
            if (outputImageFile.exists()) {
                outputImageFile.delete();
            }
            outputImageFile.createNewFile();
            mImageFileUri = ImageUtil.getImageFileUriByOsVersion(this, outputImageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageFileUri);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_IMAGE_CAPTURE
                && resultCode == RESULT_OK) {
            Barcode code = Store.getCurrentBarcode();
            Bitmap imageBitmap = null;
            if (data != null) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
            }
            if (imageBitmap == null) {

                ContentResolver contentResolver = getContentResolver();

                InputStream inputStream = null;
                try {
                    inputStream = contentResolver.openInputStream(mImageFileUri);
                    imageBitmap = BitmapFactory.decodeStream(inputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            code.setDamageImage(ImageUtil.getStringImage(imageBitmap));

            // Get the dimensions of the View
            mCameraResultView.setVisibility(View.VISIBLE);
            int targetW = mCameraResultView.getWidth();
            int targetH = mCameraResultView.getHeight();
            imageBitmap = ImageUtil.getResizedBitmap(imageBitmap, targetW, targetH);
            mCameraResultView.setImageBitmap(imageBitmap);
        }
    }
}
