package com.dear.test.testhttp

import retrofit2.http.GET

interface WeatherServer {

    @GET("/9-2")
    fun getWeather():String

}