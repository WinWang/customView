package com.winwang.myapplication.activity.motionlalyout

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import com.winwang.myapplication.R

class MotionActivity2 : AppCompatActivity() {

    var images = intArrayOf(
        R.mipmap.banner1,
        R.mipmap.banner2,
        R.mipmap.banner3,
        R.mipmap.banner4,
        R.mipmap.banner5
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion2)
        val carousel = findViewById<Carousel>(R.id.carousel)
        carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return 5
            }

            override fun populate(view: View?, index: Int) {
                println(">>>>>>>$index")
                if (view is ImageView) {
                    view.setImageResource(images[index])
                }
            }

            override fun onNewItem(index: Int) {

            }
        })
        reflectTest()
        instanceTest()
    }

    private fun instanceTest() {
        val currentTimeMillis = System.currentTimeMillis()
        Log.d("TimeSpend", "$currentTimeMillis")
        for (index in 0..1000) {
            val testReflectBean = TestReflectBean()
        }
        val currentTimeMillis1 = System.currentTimeMillis()
        Log.d("TimeSpend>>>>", "${currentTimeMillis1 - currentTimeMillis}")
    }

    private fun reflectTest() {
        val currentTimeMillis = System.currentTimeMillis()
        Log.d("TimeSpend", "$currentTimeMillis")
        for (index in 0..1000) {
            try {
                val forName = Class.forName("com.winwang.myapplication.activity.motionlalyout.MotionActivity2.TestReflectBean")
                val newInstance = forName.newInstance()
            } catch (e: java.lang.Exception) {
                Log.d("errorReflect>>>>", e.toString())
            }

        }
        val currentTimeMillis1 = System.currentTimeMillis()
        Log.d("TimeSpend>>>>", "${currentTimeMillis1 - currentTimeMillis}")
    }


    class TestReflectBean {

    }


}