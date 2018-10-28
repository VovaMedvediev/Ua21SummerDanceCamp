package ua.dancecamp.vmedvediev.ua21summerdancecamp.model

import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {

    @GET("find?q=Berdiansk&units=imperial&appid=00480ebfaf1eba8f874e0d118a5f5f9a")
    fun getCurrentWeather() : Call<WeatherResponse>
}