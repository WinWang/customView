package com.winwang.myapplication.bean

import com.winwang.myapplication.bean.interfaces.IHeader

/**
 * Created by WinWang on 2022/8/5
 * Description:
 **/
data class LeftImplData(

    override var leftTitle: String = ""

) : IHeader
