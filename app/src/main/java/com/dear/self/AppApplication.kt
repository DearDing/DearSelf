package com.dear.self

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.dear.cockroach.CockExceptionHandler
import com.dear.cockroach.Cockroach
import com.dear.cockroach.support.CrashLog
import com.dear.tools.AppHelper
import com.dear.tools.toast.ToastUtil

open class AppApplication : com.dear.base.activity.BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        ToastUtil.init(this)
        AppHelper.init(this, true)
//        CrashHandler.init()
        install()
    }

    private fun install() {
        val sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler()
        Cockroach.install(this, object : CockExceptionHandler() {
            override fun onUncaughtExceptionHappened(thread: Thread, throwable: Throwable?) {
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:$thread<---", throwable)
                CrashLog.saveCrashLog(applicationContext, throwable)
                Handler(Looper.getMainLooper()).post { ToastUtil.show("捕获到导致崩溃的异常") }
            }

            override fun onBandageExceptionHappened(throwable: Throwable) {
                throwable.printStackTrace() //打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
                Handler(Looper.getMainLooper()).post { ToastUtil.show("Cockroach Worked") }
            }

            override fun onEnterSafeMode() {
                Handler(Looper.getMainLooper()).post { ToastUtil.show("有异常发生，要处理") }
            }

            override fun onMayBeBlackScreen(e: Throwable?) {
                //TODO 可能黑屏，可尝试通过关掉栈顶Activity解决
                val thread = Looper.getMainLooper().thread
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:$thread<---", e)
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, RuntimeException("black screen"))
            }
        })
    }
}