package com.winwang.myapplication;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.winwang.myapplication.activity.BarLineActivity;
import com.winwang.myapplication.activity.PieChartWithTagActivity;
import com.winwang.myapplication.activity.RadarActivity;
import com.winwang.myapplication.activity.SinViewActivity;
import com.winwang.myapplication.view.PieChartWithTag;

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

    public void toSinviewActivity(View view) {
        startActivity(new Intent(this, SinViewActivity.class));
    }

    public void toRadarActivity(View view) {
        startActivity(new Intent(this, RadarActivity.class));
    }

    public void toCircleRingActivity(View view) {
        startActivity(new Intent(this, PieChartWithTagActivity.class));
    }

    public void toBarLineActivity(View view) {
        startActivity(new Intent(this, BarLineActivity.class));
    }


}