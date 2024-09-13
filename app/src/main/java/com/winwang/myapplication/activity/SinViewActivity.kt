package com.winwang.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.winwang.myapplication.R
import com.winwang.myapplication.view.SinView

/**
 *Created by WinWang on 2020/12/20
 *Description->
 */
class SinViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sinview_layout)
        initData()
    }

    private fun initData() {
        val sinview = findViewById<SinView>(R.id.sinview)
        lifecycle.addObserver(sinview)
    }


}