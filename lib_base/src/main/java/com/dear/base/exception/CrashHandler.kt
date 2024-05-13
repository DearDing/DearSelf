package com.dear.base.exception

import android.os.Handler
import android.os.Looper
import com.dear.tools.log.LogUtil

object CrashHandler : Thread.UncaughtExceptionHandler {

    fun init() {
        Handler(Looper.getMainLooper()).post {
            LogUtil.e("开始异常拦截。")
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    LogUtil.e("捕获到异常了1。。${e.toString()}")
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        LogUtil.e("捕获到异常了2。。 ${t.name}  ${e.toString()}")
    }
}