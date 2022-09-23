package com.winwang.testlib2

import android.content.Context
import android.util.Log
import com.winwang.annotation.AppLifecycle
import com.winwang.api.IApplifecycle

/**
 * Created by WinWang on 2022/9/23
 * Description:
 **/
@AppLifecycle(properties = 2)
class TestLib2 : IApplifecycle {
    override fun onCreate(context: Context) {
        Log.d("Applifecycle", "加载TestLib2")
    }
}