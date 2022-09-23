package com.winwang.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.winwang.myapplication.R
import kotlinx.coroutines.delay

class VerticalProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_progress)

        lifecycleScope.launchWhenResumed {
            when (true) {
                true -> {
                    delay(5000)
                    Log.e("测试输出", "1111111111")
                }
                else -> {}
            }
        }
    }
}