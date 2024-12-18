package com.dear.dispatcher.task

import androidx.annotation.IntRange
import android.os.Process
import java.util.concurrent.Executor

interface ITask {

    /**
     * Task主任务执行完成之后需要执行的任务
     *
     * @return
     */
    val tailRunnable: Runnable?

    /**
     * 优先级的范围，可根据Task重要程度及工作量指定；之后根据实际情况决定是否有必要放更大
     *
     * @return Int
     */
    @IntRange(
        from = Process.THREAD_PRIORITY_FOREGROUND.toLong(),
        to = Process.THREAD_PRIORITY_LOWEST.toLong()
    )
    fun priority(): Int

    /**
     * 任务真正执行的地方
     */
    fun run()

    /**
     * Task执行所在的线程池，可指定，一般默认
     * @return Executor?
     */
    fun runOn(): Executor?

    /**
     * 依赖关系
     * @return List<Class<out Task?>?>?
     */
    fun dependsOn(): List<Class<out Task?>?>?

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     * @return Boolean
     */
    fun needWait(): Boolean

    /**
     * 是否在主线程执行
     * @return Boolean
     */
    fun isRunOnMainThread(): Boolean

    /**
     * 只是在主进程中执行
     */
    fun onlyInMainProcess():Boolean

    /**
     * 设置任务回调
     */
    fun setTaskCallback(call: TaskCallback?)

    /**
     * 是否需要回调
     */
    fun needCall(): Boolean

}

interface TaskCallback {
    fun call()
}