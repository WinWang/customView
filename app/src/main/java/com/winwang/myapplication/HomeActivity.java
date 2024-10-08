package com.winwang.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.winwang.myapplication.activity.BarFlingctivity;
import com.winwang.myapplication.activity.BarLineActivity;
import com.winwang.myapplication.activity.BlockAveragectivity;
import com.winwang.myapplication.activity.DialogActivity;
import com.winwang.myapplication.activity.FlowActivity;
import com.winwang.myapplication.activity.ItemDragLayoutActivity;
import com.winwang.myapplication.activity.PieChartWithTagActivity;
import com.winwang.myapplication.activity.RadarActivity;
import com.winwang.myapplication.activity.ScrollTableActivity;
import com.winwang.myapplication.activity.ScrollingActivity;
import com.winwang.myapplication.activity.SinViewActivity;
import com.winwang.myapplication.activity.TreeMapActivity;
import com.winwang.myapplication.activity.VerticalProgressActivity;
import com.winwang.myapplication.activity.WorkManagerActivity;
import com.winwang.myapplication.activity.motionlalyout.MotionActivity1;
import com.winwang.myapplication.activity.motionlalyout.MotionActivity2;
import com.winwang.myapplication.activity.motionlalyout.MotionActivity3;

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

    public void toBlockActivity(View view) {
        startActivity(new Intent(this, BlockAveragectivity.class));
    }

    public void toProgressBarActivity(View view) {
        startActivity(new Intent(this, VerticalProgressActivity.class));
    }

    public void toBarFlingActivity(View view) {
        startActivity(new Intent(this, BarFlingctivity.class));
    }

    public void toPUPU(View view) {
        startActivity(new Intent(this, ScrollingActivity.class));
    }

    public void toScrollTable(View view) {
        startActivity(new Intent(this, ScrollTableActivity.class));
    }

    public void toMotionLayoutFirst(View view) {
        startActivity(new Intent(this, MotionActivity1.class));
    }

    public void toMotionLayoutSecond(View view) {
        startActivity(new Intent(this, MotionActivity2.class));
    }

    public void toMotionLayoutThird(View view) {
        startActivity(new Intent(this, MotionActivity3.class));
    }

    public void toSwipeLayout(View view) {
        startActivity(new Intent(this, ItemDragLayoutActivity.class));
    }

    public void toTreeMapActivity(View view) {
        startActivity(new Intent(this, TreeMapActivity.class));
    }

    public void toFlowActivity(View view) {
        startActivity(new Intent(this, FlowActivity.class));
    }

    public void toCoroutine(View view) {
        startActivity(new Intent(this, DialogActivity.class));
    }

    public void workManager(View view) {
        startActivity(new Intent(this, WorkManagerActivity.class));
    }

}