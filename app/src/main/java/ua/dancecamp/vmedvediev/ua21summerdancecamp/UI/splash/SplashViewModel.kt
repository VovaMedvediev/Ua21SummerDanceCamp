package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Event
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class SplashViewModel(private val repository: Repository) : ViewModel() {

    fun setupLocalStorage() = repository.setupLocalStorage()

    fun saveEvents(eventsList: ArrayList<Event>) = repository.saveEvents(eventsList)

    fun getEvents() = repository.getAllEvents()

    inner class SplashViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SplashViewModel(repository) as T
        }
    }
}