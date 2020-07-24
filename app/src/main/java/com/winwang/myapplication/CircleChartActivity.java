package com.winwang.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.winwang.myapplication.bean.LineChartBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CircleChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_chart);
        CircleChart chart = findViewById(R.id.circle_chart);
        List<Float> floats = new ArrayList<>();
        floats.add(100f);
        floats.add(12f);
        floats.add(11f);
        floats.add(7f);
        floats.add(12f);
        floats.add(14f);
        floats.add(18f);
        floats.add(9.5f);
        chart.setDataList(floats);
        /******************************************/
        LineChart lineChart = findViewById(R.id.line_chart);
        ArrayList<LineChartBean> lineChartBeans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            LineChartBean lineChartBean = new LineChartBean();
            lineChartBean.setMinute(i);
            lineChartBean.setValue(new Random().nextInt(100));
            lineChartBeans.add(lineChartBean);
        }
        lineChart.setChartData(lineChartBeans);

    }
}
