package com.winwang.myapplication.bean

import com.winwang.myapplication.bean.interfaces.IChildren

/**
 * Created by WinWang on 2022/8/5
 * Description:
 **/
data class RightImplData(
    override var data1: String = "",
    override var data2: String = "",
    override var data3: String = "",
    override var data4: String = "",
    override var data5: String = "",
    override var data6: String = "",
    override var data7: String = ""
) : IChildren