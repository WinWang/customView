package com.winwang.testlib

import android.content.Context
import android.util.Log
import com.winwang.annotation.AppLifecycle
import com.winwang.api.IApplifecycle

/**
 * Created by WinWang on 2022/9/23
 * Description:优先级properties控制加载的优先级
 **/
@AppLifecycle(properties = 10)
class TestLib : IApplifecycle {

    override fun onCreate(context: Context) {
        Log.d("Applifecycle", "加载testLib")
    }

}