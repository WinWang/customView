package com.winwang.myapplication

import android.content.Context
import android.util.Log
import com.winwang.annotation.AppLifecycle
import com.winwang.api.IApplifecycle

/**
 * Created by WinWang on 2022/9/23
 * Description:
 **/
@AppLifecycle(properties = 0)
class HostModule : IApplifecycle {
    override fun onCreate(context: Context) {
        Log.d("Applifecycle", "初始化宿主module")
    }
}