package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.settings

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class SettingsViewModel(private val repository: Repository) : ViewModel() {

    val applicationSettings: MutableLiveData<ApplicationSettings> = MutableLiveData()

    fun getApplicationSettings() {
        repository.getApplicationSettings { applicationSettings: ApplicationSettings -> onApplicationSettingsLoaded(applicationSettings)}
    }

    fun saveApplicationSettings(applicationSettings: ApplicationSettings) = repository.saveApplicationSettings(applicationSettings)

    private fun onApplicationSettingsLoaded(settings: ApplicationSettings) {
        applicationSettings.value = settings
    }

    inner class SettingsViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsViewModel(repository) as T
        }
    }
}