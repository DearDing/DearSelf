package com.dear.test.testhttp

import com.dear.http.interceptor.CommonParamsInterceptor
import com.dear.http.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpManager {

    private val mClient: OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        createClient()
    }

    private fun createClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder().apply {
            addInterceptor(CommonParamsInterceptor())
            addInterceptor(logger)
            followSslRedirects(true)
        }.build()
    }

    fun findClient(): OkHttpClient {
        return mClient
    }

}