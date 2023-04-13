package com.winwang.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.winwang.myapplication.view.HorizontalTreeView

class OptionsRateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_rate)
        val rateView = findViewById<OptionRateView>(R.id.rateView)
        val rateView1 = findViewById<OptionRateView>(R.id.rateView1)
        val horizontalTreeView = findViewById<HorizontalTreeView>(R.id.rateView2)
        val horizontalTreeView7 = findViewById<HorizontalTreeView>(R.id.rateView7)
        val horizontalTreeView1 = findViewById<HorizontalTreeView>(R.id.rateView3)
        val horizontalTreeView8 = findViewById<HorizontalTreeView>(R.id.rateView8)
        val horizontalTreeView2 = findViewById<HorizontalTreeView>(R.id.rateView4)
        val horizontalTreeView3 = findViewById<HorizontalTreeView>(R.id.rateView5)
        val horizontalTreeView4 = findViewById<HorizontalTreeView>(R.id.rateView6)
        val horizontalTreeView9 = findViewById<HorizontalTreeView>(R.id.rateView9)
        val horizontalTreeView10 = findViewById<HorizontalTreeView>(R.id.rateView10)
        rateView.setRateRation(50f, 50f)
        rateView1.setRateRation(2f, 50f)
        horizontalTreeView.setTreeViewData(45f, 10f, 45f)
        horizontalTreeView.setCategoryTag("工业硅", "苹果指数", "不锈钢指数", "液化石油气指数")
        horizontalTreeView7.setTreeViewData(20f, 30f, 50f)
        horizontalTreeView1.setTreeViewData(20f, 2f, 30f)
        horizontalTreeView8.setTreeViewData(20f, 2f, 30f)
        horizontalTreeView2.setTreeViewData(30f, 5f, 30f)
        horizontalTreeView3.setTreeViewData(30f, 0f, 60f)
        horizontalTreeView9.setTreeViewData(0f, 0f, 60f)
        horizontalTreeView10.setTreeViewData(60f, 0f, 0f)
        horizontalTreeView4.setTreeViewData(30f, 10f, 60f)



    }

}