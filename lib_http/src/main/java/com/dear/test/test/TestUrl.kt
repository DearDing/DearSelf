package com.dear.test.test

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun main(){
    val url = "https://twitter.com/search?q=cute%20%23puppies&f=images#12"
//    val url = "https://twitter.com/search?a=1"
    val httpUrl = url.toHttpUrl()
    println(httpUrl.toString())
    val newUrl = httpUrl.newBuilder()
        .addQueryParameter("city", "上海")
        .addQueryParameter("contry", "zh")
        .addQueryParameter("number", "20")
        .build()
    println(newUrl.toString())
}