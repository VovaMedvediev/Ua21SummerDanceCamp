package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.realm.RealmController

class Repository(private val eventsMapper: EventsMapper) {

    fun getEventsList(date: String, onDataLoaded: (ArrayList<Event>) -> Unit, onDataNotLoaded: (ArrayList<Event>) -> Unit) {
        val realmEventsList = RealmController.getEventsForDay(date)
        if (realmEventsList.isNotEmpty()) {
            val eventsList = ArrayList<Event>()
            realmEventsList.forEach {
                eventsList.add(eventsMapper.map(it))
            }
            onDataLoaded(eventsList)
        } else {
            onDataNotLoaded(ArrayList())
        }
    }
}