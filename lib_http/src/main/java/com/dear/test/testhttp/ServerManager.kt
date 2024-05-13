package com.dear.test.testhttp

class ServerManager {

    fun getInstance(): ServerManager {
        return INSTANCE.instance
    }

    private object INSTANCE{
        val instance = ServerManager()
    }

    fun getWeather(){
        val manager = RetrofitManager()
        manager.createService<WeatherServer>().getWeather()
    }

}