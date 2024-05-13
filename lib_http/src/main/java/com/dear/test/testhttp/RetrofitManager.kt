package com.dear.test.testhttp

import com.dear.http.config.HttpConfig
import retrofit2.Retrofit

class RetrofitManager {

    private val mRetrofit: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        createRetrofit()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(com.dear.http.config.HttpConfig.BASE_URL)
            client(OkHttpManager.findClient())
        }.build()
    }

    inline fun <reified T> createService(): T {
        return findRetrofit().create(T::class.java)
    }

    fun findRetrofit(): Retrofit {
        return mRetrofit
    }

}