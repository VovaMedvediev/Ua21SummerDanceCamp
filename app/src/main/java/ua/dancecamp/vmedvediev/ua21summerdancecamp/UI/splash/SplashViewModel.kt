package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Credentials
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Event
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository
import java.util.*

class SplashViewModel(private val repository: Repository) : ViewModel() {

    val applicationSettings: MutableLiveData<ApplicationSettings> = MutableLiveData()
    val credentials: MutableLiveData<Credentials> = MutableLiveData()

    fun setupLocalStorage() = repository.setupLocalStorage()

    fun saveEvents(eventsList: ArrayList<Event>) = repository.saveEvents(eventsList)

    fun getEvents() = repository.getAllEvents()

    fun saveApplicationSettings(applicationSettings: ApplicationSettings) { repository.saveApplicationSettings(applicationSettings)}

    fun getApplicationSettings() {
        repository.getApplicationSettings { applicationSettings: ApplicationSettings -> onApplicationSettingsLoaded(applicationSettings)}
    }

    fun getCredentials() {
        repository.getCredentials { credentials: Credentials -> onCredentialsLoaded(credentials)}
    }

    private fun onCredentialsLoaded(creds: Credentials) {
        credentials.value = creds
    }

    private fun onApplicationSettingsLoaded(settings: ApplicationSettings) {
        applicationSettings.value = settings
    }

    inner class SplashViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SplashViewModel(repository) as T
        }
    }
}