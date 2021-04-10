package com.winwang.myapplication.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.winwang.myapplication.R
import kotlinx.android.synthetic.main.layout_block_average.view.*

/**
 *Created by WinWang on 2021/3/29
 *Description->
 */
class BlockAverageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.layout_block_average, this)
    }

    fun setData(list: List<Float>) {

        var sum = 0f

        list.forEach {
            sum += it
        }
        val layout_1_sum = list[0] + list[1]
        val tv_3_sum = list[2]
        val layout_2_sum = list[3] + list[4]
        val tv_6_sum = list[5]

        ll_wrap.weightSum = sum
        ll_1.weightSum = layout_1_sum
        val layoutParams_1 = ll_1.layoutParams as LinearLayout.LayoutParams
        layoutParams_1.weight = layout_1_sum
        ll_1.layoutParams = layoutParams_1


        val layoutParams_tv_1 = tv_1_layout.layoutParams as LinearLayout.LayoutParams
        layoutParams_tv_1.weight = list[0]
        tv_1_layout.layoutParams = layoutParams_tv_1

        val layoutParams_tv_2 = tv_2_layout.layoutParams as LinearLayout.LayoutParams
        layoutParams_tv_2.weight = list[1]
        tv_2_layout.layoutParams = layoutParams_tv_2

        val layoutParams_tv_3 = tv_3_layout.layoutParams as LinearLayout.LayoutParams
        layoutParams_tv_3.weight = list[2]
        tv_3_layout.layoutParams = layoutParams_tv_3

        ll_2.weightSum = layout_2_sum
        val layoutParams_2 = ll_2.layoutParams as LinearLayout.LayoutParams
        layoutParams_2.weight = layout_2_sum
        ll_2.layoutParams = layoutParams_2

        val layoutParams_tv_4 = tv_4_layout.layoutParams as LinearLayout.LayoutParams
        layoutParams_tv_4.weight = list[3]
        tv_4_layout.layoutParams = layoutParams_tv_4

        val layoutParams_tv_5 = tv_5_layout.layoutParams as LinearLayout.LayoutParams
        layoutParams_tv_5.weight = list[4]
        tv_5_layout.layoutParams = layoutParams_tv_5

        val layoutParams_tv_6 = tv_6_layout.layoutParams as LinearLayout.LayoutParams
        layoutParams_tv_6.weight = list[5]
        tv_6_layout.layoutParams = layoutParams_tv_6


    }


}