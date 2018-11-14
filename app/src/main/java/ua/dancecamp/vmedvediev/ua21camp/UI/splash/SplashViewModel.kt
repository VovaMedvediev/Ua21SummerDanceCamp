package ua.dancecamp.vmedvediev.ua21camp.UI.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21camp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21camp.model.Credentials
import ua.dancecamp.vmedvediev.ua21camp.model.Event
import ua.dancecamp.vmedvediev.ua21camp.repository.Repository
import java.util.*

class SplashViewModel(private val repository: Repository) : ViewModel() {

    val applicationSettings: MutableLiveData<ApplicationSettings> = MutableLiveData()
    val credentials: MutableLiveData<Credentials> = MutableLiveData()

    fun setupLocalStorage() = repository.setupLocalStorage()

    fun saveEvents(eventsList: ArrayList<Event>) = repository.saveEvents(eventsList)

    fun getEvents() = repository.getAllEvents()

    fun saveApplicationSettings(applicationSettings: ApplicationSettings) { repository.saveApplicationSettings(applicationSettings)}

    fun getApplicationSettings() {
        repository.getApplicationSettings { settings: ApplicationSettings -> applicationSettings.value = settings }
    }

    fun loadCredentials() {
        repository.getCredentials { creds: Credentials -> credentials.value = creds }
    }

    inner class SplashViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SplashViewModel(repository) as T
        }
    }
}