package com.winwang.myapplication.bean

/**
 * Created by WinWang on 2022/8/8
 * Description:图表数据包装类
 **/
data class TableDataWrapper(
    //绘制标题栏
    var tableTitle: TableTitleData? = null,
    //绘制数据列表
    var tableDataList: List<CustomTableData>? = null
)