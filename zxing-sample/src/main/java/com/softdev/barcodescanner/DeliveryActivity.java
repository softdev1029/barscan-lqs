package com.softdev.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.softdev.barcodescanner.utils.Constant;
import com.lqstc.barscannar.R;

public class DeliveryActivity
        extends AppCompatActivity {
    private Button mBtnBadAddress;
    private Button mBtnRefused;
    private Button mBtnClosed;
    private Button mBtnDamage;
    private Button mBtnPod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        initView();
        setViewHandler();
    }

    private void initView() {
        mBtnBadAddress = findViewById(R.id.button_bad_address);
        mBtnRefused = findViewById(R.id.button_refused);
        mBtnClosed = findViewById(R.id.button_closed);
        mBtnDamage = findViewById(R.id.button_damage);
        mBtnPod = findViewById(R.id.button_pod);
    }

    private void setViewHandler() {
        mBtnBadAddress.setOnClickListener(new View.OnClickListener() {
            public static final long serialVersionUID = 6461604831051250971L;

            public void onClick(View v) {
                try {
                    Intent k = new Intent(DeliveryActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_BAD_ADDRESS);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnRefused.setOnClickListener(new View.OnClickListener() {
            public static final long serialVersionUID = -2205098781203492940L;

            public void onClick(View v) {
                try {
                    Intent k = new Intent(DeliveryActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_REFUSED);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnClosed.setOnClickListener(new View.OnClickListener() {
            public static final long serialVersionUID = 649990730419735431L;

            public void onClick(View v) {
                try {
                    Intent k = new Intent(DeliveryActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_CLOSED);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnDamage.setOnClickListener(new View.OnClickListener() {
            public static final long serialVersionUID = 6177126237348158715L;

            public void onClick(View v) {
                try {
                    Intent k = new Intent(DeliveryActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_DAMAGE);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnPod.setOnClickListener(new View.OnClickListener() {
            public static final long serialVersionUID = 1621548364977739410L;

            public void onClick(View v) {
                try {
                    Intent k = new Intent(DeliveryActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_POD);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
