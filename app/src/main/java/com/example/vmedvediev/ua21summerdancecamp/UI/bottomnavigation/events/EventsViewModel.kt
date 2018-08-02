package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.repository.EventsRepository

class EventsViewModel(private val repository: EventsRepository) : ViewModel() {

    private val events: MutableLiveData<ArrayList<ListItem>> = MutableLiveData()

    fun getDataByDate(date: String) {
        repository.getEventsList(date) { eventsList: ArrayList<ListItem> -> onDataLoaded(eventsList)}
    }

    fun getEvents() = events

    fun getEventsList() : ArrayList<ListItem> = events.value!!

    private fun onDataLoaded(eventsList: ArrayList<ListItem>) {
        if (eventsList.isNotEmpty()) {
            events.value = eventsList
        } else {
            ArrayList<ListItem>()
        }
    }
}