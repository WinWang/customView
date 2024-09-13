package com.winwang.myapplication.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.winwang.myapplication.R
import kotlinx.coroutines.delay

class WorkManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager)
        WorkManager.getInstance(this)
            .beginWith(OneTimeWorkRequestBuilder<Work1>().build())
            .then(OneTimeWorkRequestBuilder<Work2>().build())
            .then(OneTimeWorkRequestBuilder<Work3>().build())
            .then(OneTimeWorkRequestBuilder<Work4>().build())
            .enqueue()
    }
}

class Work1(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        delay(3000)
        Log.d("Work1", "doWork: 1${System.currentTimeMillis()}")
        return Result.success()
    }
}

class Work2(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        delay(3000)
        Log.d("Work2", "doWork: 2${System.currentTimeMillis()}")
        return Result.success()
    }
}

class Work3(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        delay(3000)
        Log.d("Work3", "doWork: 3${System.currentTimeMillis()}")
        //failure后面的任务不会执行
        return Result.failure()
    }
}

class Work4(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        delay(3000)
        Log.d("Work4", "doWork: 4${System.currentTimeMillis()}")
        return Result.success()
    }
}
