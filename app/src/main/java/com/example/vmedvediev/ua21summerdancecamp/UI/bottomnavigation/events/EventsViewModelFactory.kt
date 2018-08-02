package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.vmedvediev.ua21summerdancecamp.repository.EventsRepository

class EventsViewModelFactory(private val repository: EventsRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventsViewModel(repository) as T
    }
}