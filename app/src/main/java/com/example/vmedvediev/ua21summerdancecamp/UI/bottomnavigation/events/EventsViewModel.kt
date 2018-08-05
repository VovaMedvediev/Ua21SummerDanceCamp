package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository

class EventsViewModel(private val repository: Repository) : ViewModel() {

    val events: MutableLiveData<ArrayList<ListItem>> = MutableLiveData()

    fun getEventsListByDate(date: String) {
        repository.getEventsList(date) { eventsList: ArrayList<ListItem> -> onDataLoaded(eventsList)}
    }

    private fun onDataLoaded(eventsList: ArrayList<ListItem>) {
        if (eventsList.isNotEmpty()) {
            events.value = eventsList
        } else {
            ArrayList<ListItem>()
        }
    }

    inner class EventsViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventsViewModel(repository) as T
        }
    }
}