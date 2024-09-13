package com.winwang.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.winwang.myapplication.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FlowActivity : AppCompatActivity() {
    private val taskManager = TaskManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout)
        lifecycleScope.launch {
//            taskManager.executeTasks(getTaskList()).collect { taskId ->
//                Log.d("TaskManager", "Task executed: $taskId")
//            }

            val taskFlow = executeTasksSequentially()

            taskFlow.collect { result ->
                println(result)
            }

        }
    }

    // 按顺序执行任务 A、B 和 C
    private fun executeTasksSequentially(): Flow<String> = flow {
        val resultA = taskA()
        emit(resultA)

        val resultB = taskB()
        emit(resultB)

        val resultC = taskC()
        emit(resultC)
    }

    private suspend fun taskA(): String {
        delay(1000) // 模拟耗时操作
        return "Task A completed"
    }

    // 模拟异步任务 B
    private suspend fun taskB(): String {
        delay(2000) // 模拟耗时操作
        return "Task B completed"
    }

    // 模拟异步任务 C
    private suspend fun taskC(): String {
        delay(1500) // 模拟耗时操作
        return "Task C completed"
    }


    private fun getTaskList(): List<Pair<Int, suspend () -> Unit>> {
        return listOf(
            Pair(1) { delay(500) },
            Pair(2) { delay(1000) },
            Pair(3) { delay(200) },
            Pair(4) { delay(1500) },
            Pair(5) { delay(300) }
        )
    }
}

class TaskManager {
    @OptIn(FlowPreview::class)
    suspend fun executeTasks(tasks: List<Pair<Int, suspend () -> Unit>>): Flow<Int> = flow {
        tasks.forEach { (id, task) ->
            task()
            emit(id)
        }
    }.flatMapConcat { id ->
        flow {
            delay(100) // 模拟延时操作
            emit(id)
        }
    }
}