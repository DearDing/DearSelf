package com.dear.tools

import android.os.Handler
import android.os.Looper
import com.dear.tools.log.LogUtil
import java.lang.Integer.max
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadExecutors {

    private val threadFactory = ThreadFactory {
        Thread(it).apply {
            priority = android.os.Process.THREAD_PRIORITY_BACKGROUND
            setUncaughtExceptionHandler { t, e ->
                LogUtil.e("Thread<${t.name}> has uncaughtException", e)
            }
        }
    }

    val mainThread:Executor = MainThreadExecutor()

    val cpuIO:Executor = CpuIOThreadExecutor(threadFactory)

    val diskIO:Executor = DiskIOThreadExecutor(threadFactory)

    private class MainThreadExecutor:Executor{

        private val mainHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainHandler.post(command)
        }

        fun delayExecute(command:Runnable,delayMillis:Long){
            mainHandler.postDelayed(command,delayMillis)
        }
    }

    private class DiskIOThreadExecutor(threadFactory: ThreadFactory):Executor{

        private val diskIOPool = Executors.newSingleThreadExecutor()

        override fun execute(command: Runnable) {
            val name = Thread.currentThread().name
            diskIOPool.execute(RunnableWrapper(name,command))
        }

    }

    private class CpuIOThreadExecutor(threadFactory: ThreadFactory):Executor{

        private val cpuIO = ThreadPoolExecutor(
            2,
            max(2,Runtime.getRuntime().availableProcessors()),
            30,
            TimeUnit.SECONDS,
            ArrayBlockingQueue<Runnable>(128),
            threadFactory,
            object :ThreadPoolExecutor.DiscardOldestPolicy(){
                override fun rejectedExecution(r: Runnable?, e: ThreadPoolExecutor?) {
                    super.rejectedExecution(r, e)
                    LogUtil.e("CpuIOThreadExecutor#rejectedExecution => Runnable <$r>")
                }
            }
        )

        override fun execute(command: Runnable) {
            val name = Throwable().stackTrace[1].className
            cpuIO.execute(RunnableWrapper(name,command))
        }

    }

}

private class RunnableWrapper(private val name:String,private val runnable:Runnable):Runnable{
    override fun run() {
        Thread.currentThread().name = name
        runnable.run()
    }

}