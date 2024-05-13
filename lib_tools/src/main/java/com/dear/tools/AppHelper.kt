package com.dear.tools

import android.app.Application

object AppHelper {
    private lateinit var app: Application
    private var isDebug = false

    fun init(context: Application, isDebug: Boolean) {
        app = context
        AppHelper.isDebug = isDebug
    }

    fun getApplication(): Application {
        return app
    }

    fun isDebug(): Boolean {
        return isDebug
    }
}