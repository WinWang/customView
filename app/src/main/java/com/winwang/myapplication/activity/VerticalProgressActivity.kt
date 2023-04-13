package com.winwang.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.winwang.myapplication.R
import com.winwang.myapplication.view.HorizontalProgressView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerticalProgressActivity : AppCompatActivity() {

    var progressValue = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_progress)
        val horizontalProgressView = findViewById<HorizontalProgressView>(R.id.progress)
        lifecycleScope.launch {
            while (progressValue <= 100) {
                delay(100)
                horizontalProgressView.progress = progressValue++
            }
        }
    }
}