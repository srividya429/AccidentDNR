package com.cyient.iot.ambulance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences("DnrPrefsFile", MODE_PRIVATE);
        if (preferences != null) {
            if (preferences.getString("VEHICLE_NO", null) != null) {
                startActivity(new Intent(SplashActivity.this, ActionActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}
