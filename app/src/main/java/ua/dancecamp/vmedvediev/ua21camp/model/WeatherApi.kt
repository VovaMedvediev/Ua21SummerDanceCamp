package ua.dancecamp.vmedvediev.ua21camp.model

import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {

    @GET("find?q=Berdiansk&units=metric&appid=00480ebfaf1eba8f874e0d118a5f5f9a")
    fun getCurrentWeather() : Call<WeatherResponse>
}