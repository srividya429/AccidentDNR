package com.cyient.iot.ambulance;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

/**
 * Created by admin on 24-10-2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText mEdtName;
    private EditText mEdtEmailAddress;
    private EditText mEdtMobileNum;
    private EditText mEdtVehicleNum;
    private RadioGroup mRadGroVehicleTypeRg;
    private EditText mEdtServiceProvider;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private Button mBtnRegister;
    private RadioButton mSelectedRadBtn;
    private TextInputLayout mTxtIptLytSrvProvider;


    private CognitoCachingCredentialsProvider credentialsProvider = null;
    public String mName, mEmail, mPassword, mMobile, mVehicleNo, mVehicleType, mServiceProv, mConfirmPassword;
    public static final String MY_PREFS_NAME = "DnrPrefsFile";
    int mVehicleTypeId;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        // MY_PREFS_NAME - a static String variable like:
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        mEdtName = (EditText) findViewById(R.id.mEdtName);
        mEdtEmailAddress = (EditText) findViewById(R.id.mEdtEmail);
        getRegisteredEmail();
        mEdtMobileNum = (EditText) findViewById(R.id.mEdtMobile);
        mEdtVehicleNum = (EditText) findViewById(R.id.mEdtVehicleNum);
        mRadGroVehicleTypeRg = (RadioGroup) findViewById(R.id.mRadGrovehicleType);
        mRadGroVehicleTypeRg.setOnCheckedChangeListener(this);
        mVehicleTypeId = mRadGroVehicleTypeRg.getCheckedRadioButtonId();
        mSelectedRadBtn = (RadioButton) mRadGroVehicleTypeRg.findViewById(mVehicleTypeId);
        mVehicleType = (String) mSelectedRadBtn.getText();
        mEdtServiceProvider = (EditText) findViewById(R.id.mEdtServiceProvider);
        mTxtIptLytSrvProvider = (TextInputLayout) findViewById(R.id.mTxtIptLytServiceProvider);
        mEdtPassword = (EditText) findViewById(R.id.mEdtPassword);
        mEdtConfirmPassword = (EditText) findViewById(R.id.mEdtConfirmPswd);
        mBtnRegister = (Button) findViewById(R.id.mBtnRegister);
        mBtnRegister.setOnClickListener(this);
    }

    private void getRegisteredEmail() {
        String possibleEmail = "";

        try {
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
            Account account;
            if (accounts.length > 0) {
                account = accounts[0];
                Log.d("SignUpActivity", "The primary email id is " + accounts[0].name);
                mEdtEmailAddress.setText(accounts[0].name);
            } else {
                mEdtEmailAddress.setText("srividya.kl@gmail.com");
            }
        } catch (Exception e) {
            Log.i("Exception", "Exception:" + e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnRegister:
                mName = mEdtName.getText().toString();
                mEmail = mEdtEmailAddress.getText().toString();
                Log.d("SignUpActivity", "The Email id is " + mEmail);
                mPassword = mEdtPassword.getText().toString();
                mMobile = mEdtMobileNum.getText().toString();
                mVehicleNo = mEdtVehicleNum.getText().toString();
                mServiceProv = mEdtServiceProvider.getText().toString();
                mConfirmPassword = mEdtConfirmPassword.getText().toString();

                if (mName.isEmpty() || mName.equalsIgnoreCase(" ")) {
                    showToast("Please enter your name");
                } else if (mEmail.isEmpty() || mEmail.equalsIgnoreCase(" ")) {
                    showToast("Please enter your email address");
                } else if (mPassword.isEmpty() || mPassword.equalsIgnoreCase(" ")) {
                    showToast("Please enter a password");
                } else if (mMobile.isEmpty() || mMobile.equalsIgnoreCase(" ")) {
                    showToast("Please enter your mobile number");
                } else if (mVehicleNo.isEmpty() || mVehicleNo.equalsIgnoreCase(" ")) {
                    showToast("Please enter your vehicle number");
                } /*else if (mServiceProv.isEmpty() || mServiceProv.equalsIgnoreCase(" ")) {
                    showToast("Please enter your Service Provider");
                }*/ else if (mConfirmPassword.isEmpty() || mConfirmPassword.equalsIgnoreCase(" ")) {
                    showToast("Please enter confirm password");
                } else if (!mPassword.equalsIgnoreCase(mConfirmPassword)) {
                    showToast("Password and confirm password do not match.");
                } else if (!mVehicleNo.matches("[A-Za-z]{2}[0-9]{2}[A-Za-z]{1,2}[0-9]{4}")) {
                    showToast("Please enter valid vehicle number");
                } else {
                    new UpdateAmbulance().execute();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mRadBtnGovern:
                mTxtIptLytSrvProvider.setVisibility(View.INVISIBLE);
                break;
            case R.id.mRadBtnPrivate:
                mTxtIptLytSrvProvider.setVisibility(View.VISIBLE);
                break;
        }
    }

    private class UpdateAmbulance extends AsyncTask<Void, Integer, Integer> {

        private ProgressBar progressBar = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                ManagerClass managerClass = new ManagerClass();
                credentialsProvider = managerClass.getCredentials(SignUpActivity.this);

                AmbulanceDetails ambulanceDetails = new AmbulanceDetails();
                ambulanceDetails.setVehicle_no(mVehicleNo);
                ambulanceDetails.setName(mName);
                ambulanceDetails.setEmail(mEmail);
                ambulanceDetails.setMobile_no(mMobile);
                ambulanceDetails.setService_provider(mServiceProv);
                ambulanceDetails.setAmbulance_type(mVehicleType);
                ambulanceDetails.setPassword(mPassword);
                ambulanceDetails.setLogged_in(true);

                if (credentialsProvider != null) {
                    DynamoDBMapper dynamoDBMapper = managerClass.initDynamoClient(credentialsProvider);
                    dynamoDBMapper.save(ambulanceDetails);
                } else {
                    return 2;
                }
                return 1;
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
                return 3;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                editor.putString("VEHICLE_NO", mVehicleNo);
                editor.putString("EMAIL", mEmail);
                editor.putString("MOBILE", mMobile);
                editor.putString("SERVICE_PROVIDER", mServiceProv);
                editor.putString("NAME", mName);
                editor.putString("AMB_TYPE", mVehicleType);
                editor.putString("PASSWORD", mPassword);
                editor.apply();
                showToast("Registered Successfully");
                Intent mActionActivity = new Intent(SignUpActivity.this, ActionActivity.class);
                mActionActivity.putExtra("ID", mVehicleNo);
                startActivity(mActionActivity);
                finish();
            } else if (integer == 2) {
                showToast("Insertion failed.");
            } else {
                showToast("Unexpected Issue #:: " + integer);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
