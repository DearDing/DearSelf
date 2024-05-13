package com.dear.dispatcher.dispatcher

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object ThreadDispatcher {

    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = 2.coerceAtLeast((CPU_COUNT - 1).coerceAtMost(5))
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE

    private const val KEEP_ALIVE_SECONDS = 5L
    private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val sThreadFactory: ThreadFactory = DefaultThreadFactory()
    private val sHandler = RejectedExecutionHandler { r, executor ->
        //饱和/拒绝策略
        Executors.newCachedThreadPool().execute(r)
    }

    /**
     * 获取cpu 线程池
     */
    var cpuExecutor: ThreadPoolExecutor? = null
        private set

    /**
     * 获取IO 线程池
     */
    var ioExecutor: ExecutorService? = null
        private set

    init {
        cpuExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
            sPoolWorkQueue,
            sThreadFactory,
            sHandler
        )
        cpuExecutor?.allowCoreThreadTimeOut(true)
        ioExecutor = Executors.newCachedThreadPool(sThreadFactory)
    }

    private class DefaultThreadFactory : ThreadFactory {

        private val group: ThreadGroup?
        private val threadNumber = AtomicInteger(1)
        private val namePrefix: String

        companion object {
            private val poolNumber = AtomicInteger(1)
        }

        init {
            val s = System.getSecurityManager()
            group = s?.threadGroup ?: Thread.currentThread().threadGroup ?: null
            namePrefix = "TaskDispatcherPool-${poolNumber.getAndIncrement()}-Thread-"
        }

        override fun newThread(r: Runnable?): Thread {
            val t = Thread(
                group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0
            )
            //是否为守护进程线程
            if (t.isDaemon) {
                t.isDaemon = false
            }
            if (t.priority != Thread.NORM_PRIORITY) {
                t.priority = Thread.NORM_PRIORITY
            }
            return t
        }

    }


}