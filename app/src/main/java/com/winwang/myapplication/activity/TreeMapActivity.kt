package com.winwang.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.evgenii.jsevaluator.JsEvaluator
import com.winwang.myapplication.R


class TreeMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_map)
        val jsEvaluator = JsEvaluator(this)
    }
}