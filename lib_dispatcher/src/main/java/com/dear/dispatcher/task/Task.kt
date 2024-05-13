package com.dear.dispatcher.task

import android.content.Context
import android.os.Process
import com.dear.dispatcher.dispatcher.TaskDispatcher
import com.dear.dispatcher.dispatcher.ThreadDispatcher
import com.dear.dispatcher.utils.StaterUtil
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

abstract class Task : ITask {

    protected var mContext: Context? = TaskDispatcher.context

    //是否在主进程中
    protected var mIsMainProcess: Boolean = TaskDispatcher.isMainProcess

    //是否在等待
    @Volatile
    var isWaiting = false

    //是否正在执行
    @Volatile
    var isRunning = false

    //是否执行完成
    @Volatile
    var isFinished = false

    //Task是否被分发
    @Volatile
    var isSend = false

    // 当前Task依赖的Task数量（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
    private val mDepends = CountDownLatch(dependsOn()?.size ?: 0)

    override val tailRunnable: Runnable?
        get() = null

    /**
     * 当前task等待，让依赖的task先执行
     */
    fun waitToSatisfy() {
        try {
            mDepends.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 依赖的Task执行完一个
     */
    fun satisfy() {
        mDepends.countDown()
    }

    /**
     * 是否需要尽快执行，解决特殊场景的问题：一个Task耗时非常多但是优先级却一般，很有可能开始的时间较晚，
     * 导致最后只是在等它，这种可以早开始。
     *
     * @return
     */
    fun needRunAsSoon(): Boolean {
        return false
    }

    /**
     * Task的优先级，运行在主线程则不要去改优先级
     *
     * @return
     */
    override fun priority(): Int {
        return Process.THREAD_PRIORITY_BACKGROUND
    }

    override fun runOn(): ExecutorService? {
        return ThreadDispatcher.ioExecutor
    }

    override fun dependsOn(): List<Class<out Task?>?>? {
        return null
    }

    override fun needWait(): Boolean {
        return false
    }

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun setTaskCallback(call: TaskCallback?) {
    }

    override fun needCall(): Boolean {
        return false
    }

    /**
     * 是否只在主进程，默认是
     */
    override fun onlyInMainProcess(): Boolean {
        return true
    }
}

/**
 * 主线程任务
 */
abstract class MainTask : Task() {
    override fun isRunOnMainThread(): Boolean {
        return true
    }
}