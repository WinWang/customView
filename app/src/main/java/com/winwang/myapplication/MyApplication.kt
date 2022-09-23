package com.winwang.myapplication

import android.app.Application
import com.winwang.api.AppLifecycleManager

/**
 * Created by WinWang on 2022/9/23
 * Description:
 **/
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //初始化App启动框架
        AppLifecycleManager.init(this)
    }

}