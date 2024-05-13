package com.dear.dispatcher.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * 饿汉式单例
 * 在类加载式就实例化该对象，可能会引起资源浪费；
 * JVM保证在被第一次调用时，完成对象的初始化，保证了线程的安全性
 */
object StaterUtil {

    private var sCurProcessName: String? = null

    private val currentProcessNameFromProcess: String?
        get() {
            return readCurrentProcessNameFromFile()
        }

    /**
     * 是否是主进程
     */
    fun isMainProcess(context: Context): Boolean {
        val processName = getCurrentProcessNameFromSystem(context)
        return if (processName != null && processName.contains(":")) {
            false
        } else {
            processName != null && processName == context.packageName
        }
    }

    /**
     * 从文件中获取当前进程的名字
     */
    private fun readCurrentProcessNameFromFile(): String? {
        var cmdLineReader: BufferedReader? = null
        try {
            cmdLineReader = BufferedReader(
                InputStreamReader(
                    FileInputStream("/proc/${Process.myPid()}/cmdline"),
                    "iso-8859-1"
                )
            )
            var c: Int
            val processName = StringBuilder()
            while (cmdLineReader.read().also { c = it } > 0) {
                processName.append(c.toChar())
            }
            return processName.toString()
        } catch (t: Throwable) {

        } finally {
            if (cmdLineReader != null) {
                try {
                    cmdLineReader.close()
                } catch (e: Exception) {

                }
            }
        }
        return null
    }


    /**
     * 获取当前进程名
     */
    fun getCurrentProcessNameFromSystem(context: Context): String? {
        val processName = sCurProcessName
        if (!TextUtils.isEmpty(processName)) {
            return processName
        }
        try {
            val pid = Process.myPid()
            val mActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    sCurProcessName = appProcess.processName
                    return sCurProcessName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sCurProcessName = currentProcessNameFromProcess
        return sCurProcessName
    }
}