package com.dear.http.manager

import com.dear.http.api.ApiService

object ApiManager {

    val api by lazy {
        HttpManager.create(com.dear.http.api.ApiService::class.java)
    }
}