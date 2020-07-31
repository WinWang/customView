package com.winwang.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        BarChartView chart = (BarChartView) findViewById(R.id.chartBar);

        List<BarChartBean> dataList = new ArrayList<>();
        BarChartBean barChartBean = new BarChartBean();
        barChartBean.setValue(10f);
        barChartBean.setxLableName("测试标题");

        BarChartBean barChartBean1 = new BarChartBean();
        barChartBean1.setValue(2.3f);
        barChartBean1.setxLableName("asdf");

        BarChartBean barChartBean2 = new BarChartBean();
        barChartBean2.setValue(5.5f);
        barChartBean2.setxLableName("asdf");

        BarChartBean barChartBean3 = new BarChartBean();
        barChartBean3.setValue(5f);
        barChartBean3.setxLableName("asdf");

        BarChartBean barChartBean4 = new BarChartBean();
        barChartBean4.setValue(0.1f);
        barChartBean4.setxLableName("asdf");

        dataList.add(barChartBean);
        dataList.add(barChartBean1);
        dataList.add(barChartBean2);
        dataList.add(barChartBean3);
        dataList.add(barChartBean4);
        dataList.add(barChartBean);
        chart.setDataList(dataList);

//        startActivity(new Intent(this,TimeLineActivity.class));
//        startActivity(new Intent(this,SocketActivity.class));

    }
}
