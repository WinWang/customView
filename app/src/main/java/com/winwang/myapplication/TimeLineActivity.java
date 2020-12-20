package com.winwang.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends AppCompatActivity {


    private List<TimeBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        RecyclerView rv = findViewById(R.id.rv_time);

        for (int i = 0; i < 100; i++) {
            dataList.add(new TimeBean());
        }
        rv.setLayoutManager(new LinearLayoutManager(this));
        final TimeAdpater timeAdpater = new TimeAdpater(R.layout.item_time_line_layout, dataList);
        rv.setAdapter(timeAdpater);
        timeAdpater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TimeBean timeBean = dataList.get(position);
                if (timeBean.isHasExpand()) {
                    timeBean.setHasExpand(false);
                } else {
                    timeBean.setHasExpand(true);
                }
                timeAdpater.notifyItemChanged(position, timeBean);
            }
        });

        startActivity(new Intent(this, StepViewActivity.class));

    }
}
