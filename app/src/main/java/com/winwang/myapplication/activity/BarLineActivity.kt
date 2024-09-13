package com.winwang.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.BarLineBean
import com.winwang.myapplication.bean.EvaluteBarchartBean
import com.winwang.myapplication.bean.PieBlockBean
import com.winwang.myapplication.bean.StockSectionBean
import com.winwang.myapplication.view.BarLineChart
import com.winwang.myapplication.view.CirclePieView
import com.winwang.myapplication.view.RoundBarChartView
import com.winwang.myapplication.view.SectionView
import java.util.Random

class BarLineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_line)

        val barLineChart = findViewById<BarLineChart>(R.id.barLineChart)
        val pieChart = findViewById<CirclePieView>(R.id.pieChart)
        val sectionView = findViewById<SectionView>(R.id.sectionView)
        val roundBarChart = findViewById<RoundBarChartView>(R.id.roundBarChart)

        /**
         * 设置柱状图和折线图数据
         */
        val arrayList = ArrayList<BarLineBean>()
        val max = 70
        val min = 20
        val lineMax = 15
        val lineMin = 2
        (20..29).forEach { index: Int ->
            val i = Random().nextInt(max) % (max - min + 1) + min
            val lineRandom = Random().nextInt(lineMax) % (lineMax - lineMin + 1) + lineMin
            val barLineBean = BarLineBean(i.toFloat(), (i - lineRandom).toFloat(), "3-19")
            arrayList.add(barLineBean)
        }
        barLineChart.setBarLineData(arrayList)

        /**
         * 设置板块图形数据
         */
        val list = ArrayList<PieBlockBean>()
        (1..8).forEach { index: Int ->
            val lineRandom = Random().nextInt(lineMax) % (lineMax - lineMin + 1) + lineMin
            var block = PieBlockBean(lineRandom.toFloat(), "")
            list.add(block)
        }
        pieChart.setChartData(list)

        /**
         * 设置分段图标数据
         */
        val stockSectionBean = StockSectionBean(36f, 38f, 66f, "100亿以下", "100亿-500亿", "500亿以上")
        sectionView.setSectionData(stockSectionBean)


        /**
         * 设置圆角柱状图数据
         */

        val barList = ArrayList<EvaluteBarchartBean>()
        (1..5).forEach {
            val evaluteBarchartBean = EvaluteBarchartBean(it + 1, "$it+1")
            barList.add(evaluteBarchartBean)
        }
        roundBarChart.setChartData(barList)


    }
}