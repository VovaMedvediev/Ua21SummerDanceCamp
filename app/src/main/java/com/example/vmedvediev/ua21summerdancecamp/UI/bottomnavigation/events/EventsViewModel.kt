package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository

class EventsViewModel(private val repository: Repository) : ViewModel() {

    private val events: MutableLiveData<ArrayList<Event>> = MutableLiveData()

    fun getDataBydDate(date: String) {
        repository.getEventsList(date, {eventsList: ArrayList<Event> -> onDataLoaded(eventsList)},
                {eventsList: ArrayList<Event> -> onDataNotLoaded(eventsList)})
    }

    fun getEventsList() : LiveData<ArrayList<Event>> = events

    private fun onDataLoaded(eventsList: ArrayList<Event>) {
        events.value = eventsList
    }

    private fun onDataNotLoaded(emptyEventsList: ArrayList<Event>) {
        events.value = emptyEventsList
    }
}