package com.softdev.barcodescanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.softdev.barcodescanner.network.GetDataTask;

public class MenuActivity
        extends AppCompatActivity {
    private Button mBtnGateway = null;
    private Button mBtnDriver = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();
        initData();
        setViewHandler();
    }

    private void initView() {
        mBtnGateway = findViewById(R.id.button_gateway);
        mBtnDriver = findViewById(R.id.button_driver);
    }

    private void initData() {
        new GetDataTask(this).execute();
    }

    private void setViewHandler() {
        mBtnGateway.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent k = new Intent(MenuActivity.this, GatewayActivity.class);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnDriver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent k = new Intent(MenuActivity.this, DriverActivity.class);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.msg_exit)
                .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.lbl_no, null)
                .show();
    }
}
