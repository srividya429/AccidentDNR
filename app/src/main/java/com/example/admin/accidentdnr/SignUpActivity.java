package com.example.admin.accidentdnr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by admin on 24-10-2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mName;
    EditText mEmailAddress;
    EditText mMobileNum;
    EditText mvehicleNum;
    RadioButton mGovernment;
    RadioButton mPrivate;
    EditText mServiceProvider;
    EditText mPassword;
    EditText mConfirmPassword;
    Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        initViews();
    }

    private void initViews() {
        mvehicleNum = (EditText) findViewById(R.id.mEdtName);
        mEmailAddress = (EditText) findViewById(R.id.mEdtEmail);
        mMobileNum = (EditText) findViewById(R.id.mEdtMobile);
        mvehicleNum = (EditText) findViewById(R.id.mEdtVehicleNum);
        mGovernment = (RadioButton) findViewById(R.id.mRadBtnGovern);
        mPrivate = (RadioButton) findViewById(R.id.mRadBtnPrivate);
        mServiceProvider = (EditText) findViewById(R.id.mEdtServiceProvider);
        mPassword = (EditText) findViewById(R.id.mEdtPassword);
        mConfirmPassword = (EditText) findViewById(R.id.mEdtConfirmPswd);
        mRegister = (Button) findViewById(R.id.mBtnRegister);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnRegister:
                Intent mActionActivity= new Intent(this,ActionActivity.class);
                startActivity(mActionActivity);
                break;
        }
    }
}
