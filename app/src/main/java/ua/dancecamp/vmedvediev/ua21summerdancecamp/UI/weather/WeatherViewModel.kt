package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.weather

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.WeatherResponse
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class WeatherViewModel(private val repository: Repository) : ViewModel()  {

    val weatherResponse: MutableLiveData<WeatherResponse> = MutableLiveData()

    fun loadWeatherResponse() {
        repository.getWeatherResponse { weatherResponse: WeatherResponse -> onWeatherResponseLoaded(weatherResponse) }
    }

    private fun onWeatherResponseLoaded(weather: WeatherResponse) {
        weatherResponse.value = weather
    }

    inner class WeatherViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WeatherViewModel(repository) as T
        }
    }
}