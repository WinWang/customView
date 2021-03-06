package com.winwang.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PagerHelperActivity extends AppCompatActivity {


    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_helper);
        RecyclerView rv = findViewById(R.id.rv_pager);
        for (int i = 0; i < 45; i++) {
            dataList.add("");
        }
        PagerAdapter pagerAdapter = new PagerAdapter(R.layout.item_icon_layout, dataList);
        rv.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
        rv.setAdapter(pagerAdapter);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(rv);
    }
}
