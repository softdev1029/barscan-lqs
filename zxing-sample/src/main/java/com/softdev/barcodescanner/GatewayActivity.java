package com.softdev.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.softdev.barcodescanner.utils.Constant;

public class GatewayActivity
        extends AppCompatActivity {
    private Button mBtnBypass = null;
    private Button mBtnAirport = null;
    private Button mBtnDeparture = null;
    private Button mBtnArrival = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway);

        initView();
        setViewHandler();
    }

    private void initView() {
        mBtnBypass = findViewById(R.id.button_bypass);
        mBtnAirport = findViewById(R.id.button_airport);
        mBtnDeparture = findViewById(R.id.button_departure);
        mBtnArrival = findViewById(R.id.button_arrival);
    }

    private void setViewHandler() {
        mBtnBypass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent k = new Intent(GatewayActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_BYPASS);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnAirport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent k = new Intent(GatewayActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_AIRPORT);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mBtnDeparture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent k = new Intent(GatewayActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_DEPARTURE);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mBtnArrival.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent k = new Intent(GatewayActivity.this, ScanIntroActivity.class);
                    k.putExtra(Constant.ACTION_NAME, Constant.ACTION_ARRIVAL);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
