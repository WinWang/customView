package com.winwang.myapplication.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.*
import kotlinx.android.synthetic.main.activity_scroll_table_layout.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by WinWang on 2022/8/5
 * Description:
 **/
class ScrollTableActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_table_layout)
        initData()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initData() {
        generateData()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateData() {
        val dataList = arrayListOf<CustomTableData>()


        val rightTitleList = arrayListOf<String>()
        rightTitleList.add("标题1")
        rightTitleList.add("标题2")
        rightTitleList.add("标题3")
        rightTitleList.add("标题4")
        rightTitleList.add("标题5")
        rightTitleList.add("标题6")
        rightTitleList.add("标题7")
        rightTitleList.add("标题8")
        rightTitleList.add("标题9")
        rightTitleList.add("标题10")
        rightTitleList.add("标题11")
        rightTitleList.add("标题12")
        rightTitleList.add("标题13")
        rightTitleList.add("标题14")
        rightTitleList.add("标题15")
        rightTitleList.add("标题16")
        rightTitleList.add("标题17")
        rightTitleList.add("标题18")
        rightTitleList.add("标题19")
        rightTitleList.add("标题20")
        rightTitleList.add("标题21")
        rightTitleList.add("标题22")
        var tableTitle = TableTitleData("股票名称", rightTitleList)

        val rightList = arrayListOf<String>()
        rightList.add("第1条数据")
        rightList.add("第2条数据")
        rightList.add("第3条数据")
        rightList.add("第4条数据")
        rightList.add("第5条数据")
        rightList.add("第6条数据")
        rightList.add("第8条数据")
        rightList.add("第9条数据")
        rightList.add("第10条数据")
        rightList.add("第11条数据")
        rightList.add("第12条数据")
        rightList.add("第13条数据")
        rightList.add("第14条数据")
        rightList.add("第15条数据")
        rightList.add("第16条数据")
        rightList.add("第17条数据")
        rightList.add("第18条数据")
        rightList.add("第19条数据")
        rightList.add("第20条数据")
        rightList.add("第21条数据")
        rightList.add("第22条数据")
        rightList.add("第23条数据")
        val customTableData = CustomTableData(
            header = LeftImplData("标题"),
            children = RightImplData(
                "第一条数据",
                "第二条数据",
                "第三条数据",
                "第四条数据",
                "第五条数据",
                "第六条数据",
                "第七条数据"
            ),
            rightData = rightList
        )
        for (i in 0 until 10000) {
            dataList.add(customTableData)
        }
        var tabbleData = TableDataWrapper(tableTitle = tableTitle, dataList)
        scrollable.tableDatas = tabbleData
//        lifecycleScope.launch {
//            while (true) {
//                delay(20)
//                scrollable.tableDatas = tabbleData
//            }
//        }
    }


}