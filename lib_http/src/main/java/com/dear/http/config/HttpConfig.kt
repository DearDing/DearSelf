package com.dear.http.config

object HttpConfig {

    internal const val SERVER_HOST = "route.showapi.com"
    internal const val BASE_URL = "https://${com.dear.http.config.HttpConfig.SERVER_HOST}"
    internal const val API_APPID = "1549074"
    internal const val API_SIGN = "c060208621984211a1e420fcc1ed2053"

    const val COLLECTION_WEBSITE = "lg/collect"
    const val NOT_COLLECTION_WEBSITE = "lg/uncollect"
    const val ARTICLE_WEBSITE = "article"
    const val COIN_WEBSITE = "lg/coin"

    const val HTTP_COOKIES_INFO = "http_cookies_info" //cookies缓存

    const val KEY_TOKEN = "token"
    const val KEY_COOKIE = "Cookie"
    const val KEY_SET_COOKIE = "set-cookie"

    const val KEY_SAVE_USER_LOGIN = "user/login"
    const val KEY_SAVE_USER_REGISTER = "user/register"


}