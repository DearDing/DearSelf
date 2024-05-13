package com.dear.http.api

import com.dear.http.bean.BannerBean
import com.dear.http.bean.BaseResponse
import retrofit2.http.GET

interface ApiService {

    /**
     * 首页轮播图
     */
    @GET("/banner/json")
    suspend fun getHomeBanner(): com.dear.http.bean.BaseResponse<MutableList<com.dear.http.bean.BannerBean>>?

}