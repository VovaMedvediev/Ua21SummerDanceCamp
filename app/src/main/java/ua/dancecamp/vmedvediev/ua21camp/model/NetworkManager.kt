package ua.dancecamp.vmedvediev.ua21camp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun initRetrofit() : WeatherApi {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(WeatherApi::class.java)
    }
}