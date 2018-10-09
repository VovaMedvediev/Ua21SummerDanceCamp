package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Event
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class SplashViewModel(private val repository: Repository) : ViewModel() {

    val applicationSettings: MutableLiveData<ApplicationSettings> = MutableLiveData()

    fun setupLocalStorage() = repository.setupLocalStorage()

    fun saveEvents(eventsList: ArrayList<Event>) = repository.saveEvents(eventsList)

    fun getEvents() = repository.getAllEvents()

    fun saveApplicationSettigns(applicationSettings: ApplicationSettings) { repository.saveApplicationSettings(applicationSettings)}

    fun getApplicationSettings() {
        repository.getApplicationSettings { applicationSettings: ApplicationSettings -> onApplicationSettingsLoaded(applicationSettings)}
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