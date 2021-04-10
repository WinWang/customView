package com.winwang.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.winwang.myapplication.R
import kotlinx.android.synthetic.main.activity_block_averagectivity.*

class BlockAveragectivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_averagectivity)

        val list = ArrayList<Float>()

        list.add(2.1f)
        list.add(1.5f)
        list.add(1.1f)
        list.add(0.9f)
        list.add(0.7f)
        list.add(0.6f)

        view_block.setData(list)

    }
}