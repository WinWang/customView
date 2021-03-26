package com.winwang.myapplication.bean

/**
 *Created by WinWang on 2021/3/20
 *Description->
 */
data class TreeMapParentBean(
        var children: List<TreeMapChildBean>,
        var value: Float
)

data class TreeMapChildBean(
        var y0: Float,
        var x0: Float,
        var y1: Float,
        var x1: Float,
        var value: Float
)
