package com.showbuddy4.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.showbuddy4.R;

/**
 * Created by Ashish on 2/8/2018.
 */

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        getSupportActionBar().setTitle("Privacy And Policy");
    }
}
