package com.softdev.barcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.softdev.barcodescanner.utils.Constant;
import com.softdev.barcodescanner.utils.Util;
import com.softdev.barcodescanner.network.GetDataTask;
import com.lqstc.barscannar.R;
public class ScanIntroActivity
        extends AppCompatActivity {

    // views
    private Button mBtnScan = null;
    private TextView mTitleView = null;

    // logic
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private int mAction;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_intro);

        initView();
        initData();
        setViewHandler();
    }

    private void initView() {
        mBtnScan = findViewById(R.id.button_scan);
        mTitleView = findViewById(R.id.tv_title);
    }

    private void initData() {
        Intent i = getIntent();
        mAction = i.getIntExtra(Constant.ACTION_NAME, Constant.DEFAULT_ACTION);
        mTitleView.setText(Util.getActionTitleId(mAction));
    }

    private void setViewHandler() {
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    launchActivity(ScanActivity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            intent.putExtra(Constant.ACTION_NAME, mAction);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
