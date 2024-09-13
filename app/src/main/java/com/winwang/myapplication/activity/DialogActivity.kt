package com.winwang.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.winwang.myapplication.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class DialogActivity : AppCompatActivity() {

    private val dialogList = mutableListOf<String>("1", "2", "3", "4")

    var job: Job? = null

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
//        lifecycleScope.launch {
//            dialogList.forEach {
//                showDialog(it)
//            }
//        }

    }


    fun testCoroutine(view: View) {
        val tv = findViewById<TextView>(R.id.tv)
        count++
        tv.text = count.toString()
        cancelJob()
        job = MainScope().launch {
            Log.d("DialogActivity", "1>>>$count")
            delay(3000)
            Log.d("DialogActivity", "2>>>$count")
        }
    }

    private fun cancelJob() {
        job?.cancel()
    }


    private suspend fun showDialog(tips: String) {
        Log.d("DialogActivity", "showDialog: $tips")
        suspendCancellableCoroutine { coroutine ->
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(tips)
                .setPositiveButton("确定") { dialog, which ->
                    dialog.dismiss()
                    coroutine.resumeWith(Result.success(true))
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                    coroutine.resumeWith(Result.success(true))
                }
                .create()
                .show()
        }
    }


}