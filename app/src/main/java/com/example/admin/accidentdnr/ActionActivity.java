package com.example.admin.accidentdnr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by admin on 24-10-2017.
 */

public class ActionActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_action);
        initViews();
    }

    private void initViews() {
    }

    @Override
    public void onClick(View view) {
        
    }
}
