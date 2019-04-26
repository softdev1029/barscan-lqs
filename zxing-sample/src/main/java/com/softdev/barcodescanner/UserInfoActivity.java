package com.softdev.barcodescanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.softdev.barcodescanner.models.Store;
import com.softdev.barcodescanner.utils.Constant;

public class UserInfoActivity extends AppCompatActivity {

    // view
    private EditText mUserIdView;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        initView();
        initData();
        setViewHandler();
    }

    private void initView() {
        mUserIdView = findViewById(R.id.tv_userid);
        mBtnStart = findViewById(R.id.button_start);
    }

    private void initData() {
    }

    private void setViewHandler() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Store.setUserId(mUserIdView.getText().toString());
                launchMain();
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

    private void launchMain() {
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        finish();
    }
}
