package com.dear.dispatcher.dispatcher

import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.annotation.UiThread
import com.dear.dispatcher.task.Task
import com.dear.dispatcher.task.TaskCallback
import com.dear.dispatcher.task.TaskRunnable
import com.dear.dispatcher.task.TaskStat
import com.dear.dispatcher.utils.DispatcherLog
import com.dear.dispatcher.utils.StaterUtil
import com.dear.dispatcher.utils.TaskSortUtil
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class TaskDispatcher {

    private var mStartTime: Long = 0
    private val mFutures: MutableList<Future<*>> = arrayListOf()
    private var mAllTasks: MutableList<Task> = arrayListOf()
    private val mClsAllTasks: MutableList<Class<out Task>> = arrayListOf()

    @Volatile
    private var mMainThreadTasks: MutableList<Task> = arrayListOf()
    private var mCountDownLatch: CountDownLatch? = null

    //需要保存的Wait的Task的数量
    private val mNeedWaitCount = AtomicInteger()

    //调用了await的时候还没结束的且需要等待的Task
    private val mNeedWaitTasks: MutableList<Task> = arrayListOf()

    //已经结束了的Task
    @Volatile
    private var mFinishedTasks: MutableList<Class<out Task>> = ArrayList(100)
    private val mDependedHashMap = HashMap<Class<out Task>, ArrayList<Task>?>()

    //启动器分析的次数，统计下分析的耗时
    private val mAnalyseCount = AtomicInteger()

    companion object {

        private const val WAIT_TIME = 10000

        var context: Application? = null
            private set

        var isMainProcess = false
            private set

        @Volatile
        private var sHasInit = false

        fun init(context: Application?) {
            context?.let {
                Companion.context = it
                sHasInit = true
                isMainProcess = StaterUtil.isMainProcess(it)
            }
        }

        fun createInstance(): TaskDispatcher {
            if (!sHasInit) {
                throw java.lang.RuntimeException("TaskDispatcher: must call TaskDispatcher.init first")
            }
            return TaskDispatcher()
        }
    }

    /**
     * 添加任务Task
     */
    fun addTask(task: Task?): TaskDispatcher {
        task?.let {
            collectDepends(it)
            mAllTasks.add(it)
            mClsAllTasks.add(it.javaClass)
            //非主线程且需要wait的，主线程不需要CountDownLatch也是同步的
            if (ifNeedWait(it)) {
                mNeedWaitTasks.add(it)
                mNeedWaitCount.getAndIncrement()
            }
        }
        return this
    }

    /**
     * 处理任务依赖
     */
    private fun collectDepends(task: Task) {
        task.dependsOn()?.let { list ->
            list.forEach { clz ->
                clz?.let {
                    if (mDependedHashMap[clz] == null) {
                        mDependedHashMap[clz] = arrayListOf()
                    }
                    mDependedHashMap[clz]?.add(task)
                    if (mFinishedTasks.contains(clz)) {
                        task.satisfy()
                    }
                }
            }
        }
    }

    /**
     * 是否需要等待
     */
    private fun ifNeedWait(task: Task): Boolean {
        //task不在主线程，并且需要等待
        return !task.isRunOnMainThread() && task.needWait()
    }

    @UiThread
    fun start() {
        mStartTime = System.currentTimeMillis()
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw java.lang.RuntimeException("TaskDispatcher - start() must be called from UiThread")
        }
        if (!mAllTasks.isNullOrEmpty()) {
            mAnalyseCount.getAndIncrement()
            printDependedMsg(false)
            mAllTasks = TaskSortUtil.getSortResult(mAllTasks, mClsAllTasks).toMutableList()
            mCountDownLatch = CountDownLatch(mNeedWaitCount.get())
            sendAndExecuteAsyncTasks()
            DispatcherLog.i("task analyse cost ${(System.currentTimeMillis() - mStartTime)} begin main ")
            executeTaskMain()
        }
        DispatcherLog.i("task analyse cost startTime cost ${(System.currentTimeMillis() - mStartTime)}")
    }

    @UiThread
    fun await() {
        try {
            if (DispatcherLog.isDebug) {
                DispatcherLog.i("still has ${mNeedWaitCount.get()}")
                for (task in mNeedWaitTasks) {
                    DispatcherLog.i("needWait: ${task.javaClass.simpleName}")
                }
            }
            if (mNeedWaitCount.get() > 0) {
                mCountDownLatch?.await(WAIT_TIME.toLong(), TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 执行主线程任务
     */
    private fun executeTaskMain() {
        mStartTime = System.currentTimeMillis()
        for (task in mMainThreadTasks) {
            val time = System.currentTimeMillis()
            TaskRunnable(task, this).run()
            DispatcherLog.i(
                "real main ${task.javaClass.simpleName} cost ${(System.currentTimeMillis() - time)}"
            )
        }
        DispatcherLog.i("mainTask cost ${(System.currentTimeMillis() - mStartTime)}")
    }

    /**
     * 取消任务执行
     */
    fun cancel() {
        mFutures.forEach {
            it.cancel(true)
        }
    }

    /**
     * 分发任务，并执行异步任务
     */
    private fun sendAndExecuteAsyncTasks() {
        mAllTasks.forEach {
            if (it.onlyInMainProcess() && !isMainProcess) {
                //不在主进程的任务，不执行，直接标记完成
                markTaskDone(it)
            } else {
                //分发任务
                sendTaskReal(it)
            }
            it.isSend = true
        }
    }

    /**
     * 标记 task 完成
     */
    fun markTaskDone(task: Task) {
        if (ifNeedWait(task)) {
            mFinishedTasks.add(task.javaClass)
            mNeedWaitTasks.remove(task)
            mCountDownLatch?.countDown()
            mNeedWaitCount.getAndDecrement()
        }
    }

    /**
     * 分发 task
     */
    private fun sendTaskReal(task: Task) {
        if (task.isRunOnMainThread()) {
            mMainThreadTasks.add(task)
            if (task.needCall()) {
                task.setTaskCallback(object : TaskCallback {
                    override fun call() {
                        TaskStat.markTaskDone()
                        task.isFinished = true
                        satisfyChildren(task)
                        markTaskDone(task)
                        DispatcherLog.i("${task.javaClass.simpleName} finish")
                        Log.i("testLog", "call")
                    }
                })
            }
        } else {
            // 直接发，是否执行取决于具体线程池
            val future = task.runOn()?.submit(TaskRunnable(task, this))
            future?.let {
                mFutures.add(it)
            }
        }
    }

    /**
     * 通知Children一个前置任务已完成
     *
     * @param launchTask
     */
    fun satisfyChildren(launchTask: Task) {
        val arrayList = mDependedHashMap[launchTask.javaClass]
        if (!arrayList.isNullOrEmpty()) {
            for (task in arrayList) {
                task.satisfy()
            }
        }
    }

    fun executeTask(task: Task) {
        if (ifNeedWait(task)) {
            mNeedWaitCount.getAndIncrement()
        }
        task.runOn()?.execute(TaskRunnable(task, this))
    }


    /**
     * 查看被依赖的信息
     */
    private fun printDependedMsg(isPrintAllTask: Boolean) {
        DispatcherLog.i("needWait size : ${mNeedWaitCount.get()}")
        if (isPrintAllTask) {
            for (cls in mDependedHashMap.keys) {
                DispatcherLog.i("cls: ${cls.simpleName} ${mDependedHashMap[cls]?.size}")
                mDependedHashMap[cls]?.let {
                    for (task in it) {
                        DispatcherLog.i("cls:${task.javaClass.simpleName}")
                    }
                }
            }
        }
    }

}