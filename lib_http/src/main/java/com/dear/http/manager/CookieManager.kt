package com.dear.http.manager

import com.dear.http.config.HttpConfig.HTTP_COOKIES_INFO
import com.dear.tools.log.LogUtil
import com.tencent.mmkv.MMKV

object CookieManager {

    fun saveCookies(cookies: String) {
        val mmkv = MMKV.defaultMMKV()
        mmkv.encode(HTTP_COOKIES_INFO, cookies)
    }

    fun getCookies(): String? {
        val mmkv = MMKV.defaultMMKV()
        return mmkv.getString(HTTP_COOKIES_INFO, "")
    }

    fun clearCookies() {
        saveCookies("")
    }

    fun encodeCookie(cookies: List<String>?): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
            ?.map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            ?.forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        LogUtil.e("cookiesList:$cookies", tag = "smy")
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }


}