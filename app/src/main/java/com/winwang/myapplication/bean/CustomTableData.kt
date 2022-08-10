package com.winwang.myapplication.bean

import com.winwang.myapplication.bean.interfaces.IChildren
import com.winwang.myapplication.bean.interfaces.IHeader

/**
 * Created by WinWang on 2022/8/4
 * Description:
 **/
data class CustomTableData(
    var header: IHeader? = null,
    var children: IChildren? = null,
    var rightData: List<String>? = null
)
