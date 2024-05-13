package com.dear.http.interceptor

import com.dear.http.error.ERROR
import com.dear.http.error.NoNetWorkException
import com.dear.tools.AppHelper
import com.dear.tools.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (NetworkUtil.isConnected(AppHelper.getApplication())) {
            val request = chain.request()
            return chain.proceed(request)
        } else {
            throw com.dear.http.error.NoNetWorkException(com.dear.http.error.ERROR.NETWORD_ERROR)
        }
    }
}