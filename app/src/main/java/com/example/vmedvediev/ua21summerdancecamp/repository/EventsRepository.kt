package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmDateMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.ListItemDateMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import io.realm.RealmResults

class EventsRepository(private val eventsMapper: EventsMapper, private val realmDateMapper: RealmDateMapper,
                       private val listItemDateMapper: ListItemDateMapper) {

    fun getEventsList(date: String, onDataLoaded: (ArrayList<ListItem>) -> Unit) {
        if (EventsCache.eventsList.isNotEmpty()) {
            onDataLoaded(getEventsFromLocalStorage(date))
        } else {
            val realmEventsList = EventsDatabaseHelper.getEventsByDate(date)
            if (realmEventsList.isNotEmpty()) {
                onDataLoaded(prepareEventsFromDatabase(realmEventsList))
            } else {
                onDataLoaded(ArrayList())
            }
        }
    }

    private fun getEventsFromLocalStorage(dateOfDay: String): ArrayList<ListItem> {
        val eventsList = ArrayList<ListItem>()
        EventsCache.getEventsByDate(dateOfDay).forEach {
            val date = listItemDateMapper.from(it)
            if (!eventsList.contains(date)) {
                eventsList.add(date)
            }
            eventsList.add(it)
        }
        return eventsList
    }

    private fun prepareEventsFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<ListItem> {
        val eventsList = ArrayList<ListItem>()
        realmEventsList.forEach {
            val date = realmDateMapper.from(it)
            if (!eventsList.contains(date)) {
                eventsList.add(date)
            }
            eventsList.add(eventsMapper.from(it))
        }
        return eventsList
    }
}