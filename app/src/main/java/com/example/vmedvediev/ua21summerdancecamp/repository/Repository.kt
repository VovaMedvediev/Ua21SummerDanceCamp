package com.example.vmedvediev.ua21summerdancecamp.repository

import android.util.Log
import com.example.vmedvediev.ua21summerdancecamp.mappers.DateMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent
import com.example.vmedvediev.ua21summerdancecamp.realm.RealmController

class Repository(private val eventsMapper: EventsMapper, private val dateMapper: DateMapper) {

    fun getEventsList(date: String, onDataLoaded: (ArrayList<ListItem>) -> Unit, onDataNotLoaded: (ArrayList<ListItem>) -> Unit) {
        val realmEventsList = RealmController.getEventsByDate(date)
        if (realmEventsList.isNotEmpty()) {
            val eventsList = ArrayList<ListItem>()
            realmEventsList.forEach {
                val date = dateMapper.from(it)
                if (!eventsList.contains(date)) {
                    eventsList.add(date)
                }
                eventsList.add(eventsMapper.from(it))
            }
            onDataLoaded(eventsList)
        } else {
            onDataNotLoaded(ArrayList())
        }
    }
}