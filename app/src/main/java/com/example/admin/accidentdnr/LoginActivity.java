package com.example.admin.accidentdnr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mVehicleNumber;
    private EditText mPassword;
    private CheckBox mAlwaysLogin;
    private Button mSignUp;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        mVehicleNumber = (EditText) findViewById(R.id.mEdtVehicleNumber);
        mPassword = (EditText) findViewById(R.id.mEdtPassword);
        mAlwaysLogin = (CheckBox) findViewById(R.id.mChkAlwaysLogin);
        mSignUp = (Button) findViewById(R.id.mBtnSignUp);
        mSignUp.setOnClickListener(this);
        mLogin = (Button) findViewById(R.id.mBtnLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnSignUp:
                Intent mSignUpActivity = new Intent(this, SignUpActivity.class);
                startActivity(mSignUpActivity);
                break;
        }
    }
}
