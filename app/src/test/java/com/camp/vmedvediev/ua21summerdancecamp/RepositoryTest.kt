package com.camp.vmedvediev.ua21summerdancecamp

import org.junit.Test
import ua.dancecamp.vmedvediev.ua21camp.model.NetworkManager

class RepositoryTest {

    @Test
    fun testGetWeatherResponse() {
        val weatherApi = NetworkManager.initRetrofit()
        val weatherResponse = weatherApi.getCurrentWeather()
        val test = weatherResponse.execute()
        assert(test.body()?.cod == "200")
    }
}