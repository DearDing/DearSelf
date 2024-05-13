package com.dear.http.interceptor

import com.dear.http.config.HttpConfig.KEY_SAVE_USER_LOGIN
import com.dear.http.config.HttpConfig.KEY_SAVE_USER_REGISTER
import com.dear.http.config.HttpConfig.KEY_SET_COOKIE
import com.dear.http.manager.CookieManager
import com.dear.tools.log.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()

        val response = chain.proceed(newBuilder.build())
        val url = request.url.toString()
        val host = request.url.host

        // set-cookie maybe has multi, login to save cookie
        if ((url.contains(KEY_SAVE_USER_LOGIN) || url.contains(KEY_SAVE_USER_REGISTER))
            && response.headers(KEY_SET_COOKIE).isNotEmpty()
        ) {
            val cookies = response.headers(KEY_SET_COOKIE)
            val cookiesStr = CookieManager.encodeCookie(cookies)
            CookieManager.saveCookies(cookiesStr)
            LogUtil.e("CookiesInterceptor:cookies:$cookies", tag = "smy")
        }
        return response
    }
}