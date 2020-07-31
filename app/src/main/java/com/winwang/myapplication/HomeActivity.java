package com.winwang.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void toStepActivity(View view) {
        startActivity(new Intent(this, StepViewActivity.class));
    }

    public void toBarActivity(View view) {
        startActivity(new Intent(this, ChartActivity.class));
    }

    public void toCircleLineActivity(View view) {
        startActivity(new Intent(this, CircleChartActivity.class));
    }

    public void toOptionActivity(View view) {
        startActivity(new Intent(this, OptionsRateActivity.class));
    }


}