package ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.settings

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21camp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21camp.repository.Repository

class SettingsViewModel(private val repository: Repository) : ViewModel() {

    val applicationSettings: MutableLiveData<ApplicationSettings> = MutableLiveData()

    fun getApplicationSettings() {
        repository.getApplicationSettings { settings: ApplicationSettings -> applicationSettings.value = settings}
    }

    fun saveApplicationSettings(applicationSettings: ApplicationSettings) = repository.saveApplicationSettings(applicationSettings)

    inner class SettingsViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsViewModel(repository) as T
        }
    }
}