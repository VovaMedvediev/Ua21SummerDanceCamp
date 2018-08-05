package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.MapperImpl
import com.example.vmedvediev.ua21summerdancecamp.model.*
import io.realm.RealmResults

class Repository(private val mapper: MapperImpl) {

    fun getNotes(onEventsLoaded: (ArrayList<Event>) -> Unit) {
        val realmEventsList = DatabaseHelper.getEvents()
        if (realmEventsList.isNotEmpty()) {
            onEventsLoaded(prepareNotesFromDatabase(realmEventsList))
        } else {
            onEventsLoaded(ArrayList())
        }
    }

    fun getEvent(eventId: String, onEventLoaded: (Event) -> Unit) {
        val realmEvent = DatabaseHelper.getEventById(eventId)
        if (realmEvent != null) {
            onEventLoaded(mapper.from(realmEvent, true) as Event)
        } else {
            onEventLoaded(Event())
        }
    }

    fun deleteNoteFromDatabase(event: Event) {
        DatabaseHelper.deleteNote(mapper.to(event) as RealmEvent)
    }

    fun saveNoteToDatabase(event: Event) {
        DatabaseHelper.saveNote(mapper.to(event) as RealmEvent)
    }

    private fun prepareNotesFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<Event> {
        val eventsList = ArrayList<Event>()
        val filteredList = realmEventsList.filter { it.noteText.isNotEmpty() }
        filteredList.forEach { eventsList.add(mapper.from(it, true) as Event) }
        return eventsList
    }

    fun getEventsList(date: String, onDataLoaded: (ArrayList<ListItem>) -> Unit) {
        if (EventsCache.eventsList.isNotEmpty()) {
            onDataLoaded(getEventsFromLocalStorage(date))
        } else {
            val realmEventsList = DatabaseHelper.getEventsByDate(date)
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
            val date = mapper.from(it, false)
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
            val date = mapper.from(it, false)
            if (!eventsList.contains(date)) {
                eventsList.add(date)
            }
            eventsList.add(mapper.from(it, true))
        }
        return eventsList
    }
}