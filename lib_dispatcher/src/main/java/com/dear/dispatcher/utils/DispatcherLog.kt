package com.dear.dispatcher.utils

import com.dear.dispatcher.BuildConfig
import com.dear.tools.log.LogUtil

object DispatcherLog {

    var isDebug = BuildConfig.DEBUG

    @JvmStatic
    fun i(msg: String?) {
        if (msg == null) {
            return
        }
        LogUtil.i(msg, tag = "StartTask")
    }
}