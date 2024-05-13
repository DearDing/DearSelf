package com.dear.http.manager

import com.dear.http.config.HttpConfig
import com.dear.http.interceptor.CommonParamsInterceptor
import com.dear.http.interceptor.CookieInterceptor
import com.dear.http.interceptor.HeaderInterceptor
import com.dear.http.interceptor.NetworkInterceptor
import com.dear.http.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpManager {
    private val mRetrofit: Retrofit

    init {
        mRetrofit = Retrofit.Builder()
            .client(initOkHttpClient())
            .baseUrl(com.dear.http.config.HttpConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(clz: Class<T>): T {
        return mRetrofit.create(clz)
    }

    private fun initOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)

        builder.addInterceptor(CookieInterceptor())
        builder.addInterceptor(HeaderInterceptor())

        //日志 拦截器
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        builder.addInterceptor(loggingInterceptor)

        //网络状态 拦截器
        builder.addInterceptor(NetworkInterceptor())

        //公共参数 拦截器
        builder.addInterceptor(CommonParamsInterceptor())

        return builder.build()
    }
}