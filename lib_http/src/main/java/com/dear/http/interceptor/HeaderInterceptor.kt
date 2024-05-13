package com.dear.http.interceptor

import com.dear.http.config.HttpConfig.ARTICLE_WEBSITE
import com.dear.http.config.HttpConfig.COIN_WEBSITE
import com.dear.http.config.HttpConfig.COLLECTION_WEBSITE
import com.dear.http.config.HttpConfig.KEY_COOKIE
import com.dear.http.config.HttpConfig.NOT_COLLECTION_WEBSITE
import com.dear.http.manager.CookieManager
import com.dear.tools.log.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-type","application/json;charset=utf-8")

        val host = request.url.host
        val url = request.url.toString()

        //给有需要的接口添加cookie
        if(!host.isNullOrEmpty()  && (url.contains(COLLECTION_WEBSITE)
                    || url.contains(NOT_COLLECTION_WEBSITE)
                    || url.contains(ARTICLE_WEBSITE)
                    || url.contains(COIN_WEBSITE))) {
            val cookies = CookieManager.getCookies()
            LogUtil.e("HeaderInterceptor:cookies:$cookies", tag = "smy")
            if (!cookies.isNullOrEmpty()) {
                builder.addHeader(KEY_COOKIE, cookies)
            }
        }
        return chain.proceed(builder.build())
    }
}