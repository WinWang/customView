package com.winwang.myapplication.bean

/**
 * Created by WinWang on 2022/8/8
 * Description:标题栏包装类
 * [leftTitle]左边标题栏
 * [rightTitle] 右边滚动的标题栏
 **/
data class TableTitleData(
    var leftTitle: String? = "",
    var rightTitle: List<String>? = null
)
