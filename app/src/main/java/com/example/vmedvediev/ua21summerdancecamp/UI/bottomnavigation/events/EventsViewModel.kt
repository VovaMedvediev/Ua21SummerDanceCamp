package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository
import java.util.*

class EventsViewModel(private val repository: Repository) : ViewModel() {

    val events: MutableLiveData<LinkedHashSet<ListItem>> = MutableLiveData()

    fun getEventsListByDate(date: String) {
        repository.getEventsList(date) { eventsList: LinkedHashSet<ListItem> -> onDataLoaded(eventsList)}
    }

    fun getEvents() = events.value

    private fun onDataLoaded(eventsList: LinkedHashSet<ListItem>) {
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