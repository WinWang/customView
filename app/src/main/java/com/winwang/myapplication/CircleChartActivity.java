package com.winwang.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
        ArrayList<LineChartBean> lineChartBeans2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LineChartBean lineChartBean = new LineChartBean();
            lineChartBean.setMinute(i);
            float v = new Random().nextFloat();
            if (i < 5) {
                lineChartBean.setValue(-v);
            }else{
                lineChartBean.setValue(v);
            }
            lineChartBean.setColor("EA4C43"); //红色
            lineChartBeans.add(lineChartBean);
        }

        for (int i = 0; i < 10; i++) {
            LineChartBean lineChartBean = new LineChartBean();
            lineChartBean.setMinute(i);
            lineChartBean.setColor("FFC554"); //黄色
            float v = new Random().nextFloat();
            if (i < 5) {
                lineChartBean.setValue(-v);
            }else{
                lineChartBean.setValue(v);
            }
            lineChartBeans1.add(lineChartBean);
        }

        for (int i = 0; i < 10; i++) {
            LineChartBean lineChartBean = new LineChartBean();
            lineChartBean.setMinute(i);
            lineChartBean.setColor("4B87FF"); //蓝色
            float v = new Random().nextFloat();
            if (i < 5) {
                lineChartBean.setValue(-v);
            }else{
                lineChartBean.setValue(v);
            }
            lineChartBeans2.add(lineChartBean);
        }


        ArrayList<List<LineChartBean>> lists = new ArrayList<>();
        lists.add(lineChartBeans);
        lists.add(lineChartBeans1);
        lists.add(lineChartBeans2);
        lineChartMulty.setChartData(lineChartBeans);//设置单挑数据
        lineChart.setMultiChartData(lists); //设置多条数据;

    }
}
