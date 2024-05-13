package com.dear.http.interceptor

import com.dear.http.config.HttpConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 处理公共参数
 */
class CommonParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url
        val host = requestUrl.host
        if (host != com.dear.http.config.HttpConfig.SERVER_HOST){
            return chain.proceed(request)
        }
        val urlBuilder = requestUrl.newBuilder()
        urlBuilder.addQueryParameter("showapi_appid", com.dear.http.config.HttpConfig.API_APPID)
        urlBuilder.addQueryParameter("showapi_sign", com.dear.http.config.HttpConfig.API_SIGN)
        val newRequest = request.newBuilder().url(urlBuilder.build()).build()
        return chain.proceed(newRequest)
    }

}