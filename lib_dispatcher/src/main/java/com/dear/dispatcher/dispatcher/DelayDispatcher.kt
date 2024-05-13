package com.dear.dispatcher.dispatcher

import android.os.Looper
import android.os.MessageQueue
import com.dear.dispatcher.task.Task
import com.dear.dispatcher.task.TaskRunnable
import java.util.*

/**
 * 延迟初始化
 * 利用IdleHandler的等待主线程空闲特性，在空闲时才去执行任务
 */
class DelayDispatcher {

    private val mDelayTasks: Queue<Task> = LinkedList()

    private val mIdleHandler = MessageQueue.IdleHandler {
        if (mDelayTasks.size > 0) {
            val task = mDelayTasks.poll()
            TaskRunnable(task).run()
        }
        !mDelayTasks.isEmpty()
    }

    fun addTask(task: Task): DelayDispatcher {
        mDelayTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(mIdleHandler)
    }

}