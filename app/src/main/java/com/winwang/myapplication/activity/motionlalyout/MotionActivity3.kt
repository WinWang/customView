package com.winwang.myapplication.activity.motionlalyout

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.winwang.myapplication.R
import com.winwang.myapplication.utils.Utils
import com.winwang.myapplication.view.CustomBottomSheetBehavior
import com.winwang.myapplication.view.NestedRecycleView


class MotionActivity3 : AppCompatActivity() {

    private var behavior: CustomBottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion3)
        val testAdapter = TestAdapter()
        val testHeaderAdapter = TestHeaderAdapter()
        val dataList = mutableListOf<String>()
        val headerList = mutableListOf<String>()
        for (i in 0..100) {
            dataList.add("item$i")
        }
        for (i in 0..1) {
            headerList.add("header$i")
        }
        val header = findViewById<NestedRecycleView>(R.id.rvHeader)
        val content = findViewById<RecyclerView>(R.id.rvContent)
        val behaviorContent = findViewById<LinearLayout>(R.id.behaviorLayout)
        testAdapter.setNewInstance(dataList)
        testHeaderAdapter.setNewInstance(headerList)
        header.adapter = testHeaderAdapter
        content.adapter = testAdapter
        behavior = CustomBottomSheetBehavior.from(behaviorContent)
        behavior?.state = CustomBottomSheetBehavior.STATE_COLLAPSED
        behavior?.peekHeight = Utils.dipToPix(this@MotionActivity3, 50f).toInt()
        header.post { header.bindBottomSheetBehavior(behavior!!) }
    }
}

class TestAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_test_layout) {
    override fun convert(holder: BaseViewHolder, item: String) {

    }
}

class TestHeaderAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_header_test_layout) {
    override fun convert(holder: BaseViewHolder, item: String) {

    }
}
