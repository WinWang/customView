package com.winwang.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OptionsRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_rate);
        OptionRateView rateView = findViewById(R.id.rateView);
        OptionRateView rateView1 = findViewById(R.id.rateView1);
        rateView.setRateRation(50f, 50f);
        rateView1.setRateRation(10f, 50f);
    }
}