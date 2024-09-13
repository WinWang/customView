package com.winwang.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.winwang.myapplication.R
import com.winwang.myapplication.view.BarChartFlingView
import java.util.*
import kotlin.collections.ArrayList

class BarFlingctivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_flingctivity)
        val barchartFling = findViewById<BarChartFlingView>(R.id.barchartFling)
        barchartFling.setBarInfoList(createBarInfo())
    }


    private fun createBarInfo(): List<BarChartFlingView.BarInfo>? {
        val data: MutableList<BarChartFlingView.BarInfo> = ArrayList()
        for (i in 1..50) {
            data.add(BarChartFlingView.BarInfo(i.toString() + "日", Random().nextFloat().toDouble()))
        }
        return data
    }
}