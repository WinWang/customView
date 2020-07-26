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
        LineChart lineChartMulty = findViewById(R.id.line_chart_1);
        ArrayList<LineChartBean> lineChartBeans = new ArrayList<>();
        ArrayList<LineChartBean> lineChartBeans1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LineChartBean lineChartBean = new LineChartBean();
            lineChartBean.setMinute(i);
            lineChartBean.setValue(new Random().nextInt(100));
            lineChartBeans.add(lineChartBean);
        }

        for (int i = 0; i < 10; i++) {
            LineChartBean lineChartBean = new LineChartBean();
            lineChartBean.setMinute(i);
            lineChartBean.setValue(new Random().nextInt(100));
            lineChartBeans1.add(lineChartBean);
        }

        ArrayList<List<LineChartBean>> lists = new ArrayList<>();
        lists.add(lineChartBeans);
        lists.add(lineChartBeans1);
        lineChartMulty.setChartData(lineChartBeans);//设置单挑数据
//        lineChartMulty.setMultiChartData(lists); //设置多条数据;

    }
}
