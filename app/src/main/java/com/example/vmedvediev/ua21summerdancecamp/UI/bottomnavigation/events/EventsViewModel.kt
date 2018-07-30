package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository

class EventsViewModel(private val repository: Repository) : ViewModel() {

    private val events: MutableLiveData<ArrayList<ListItem>> = MutableLiveData()

    fun getDataBydDate(date: String) {
        repository.getEventsList(date) { eventsList: ArrayList<ListItem> -> onDataLoaded(eventsList)}
    }

    fun getEvents() : MutableLiveData<ArrayList<ListItem>> = events

    fun getEventsList() : ArrayList<ListItem> = events.value!!

    private fun onDataLoaded(eventsList: ArrayList<ListItem>) {
        if (eventsList.isNotEmpty()) {
            events.value = eventsList
        } else {
            ArrayList<ListItem>()
        }
    }
}