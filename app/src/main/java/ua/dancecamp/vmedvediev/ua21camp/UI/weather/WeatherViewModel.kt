package ua.dancecamp.vmedvediev.ua21camp.UI.weather

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21camp.model.WeatherResponse
import ua.dancecamp.vmedvediev.ua21camp.repository.Repository

class WeatherViewModel(private val repository: Repository) : ViewModel()  {

    val weatherResponse: MutableLiveData<WeatherResponse> = MutableLiveData()

    fun loadWeatherResponse() {
        repository.getWeatherResponse { weather: WeatherResponse -> weatherResponse.value = weather }
    }

    inner class WeatherViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WeatherViewModel(repository) as T
        }
    }
}