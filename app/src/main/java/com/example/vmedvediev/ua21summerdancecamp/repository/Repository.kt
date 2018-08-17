package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import com.example.vmedvediev.ua21summerdancecamp.model.Date
import io.realm.RealmResults
import java.util.*
import kotlin.collections.ArrayList

class Repository(private val mapper: RealmEventMapper) {

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
            onEventLoaded(mapper.from(realmEvent))
        } else {
            onEventLoaded(Event())
        }
    }

    fun deleteNoteFromDatabase(event: Event) {
        DatabaseHelper.deleteNote(mapper.to(event))
    }

    fun saveNoteToDatabase(event: Event) {
        DatabaseHelper.saveNote(mapper.to(event))
    }

    private fun prepareNotesFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<Event> {
        val eventsList = ArrayList<Event>()
        val filteredList = realmEventsList.filter { it.noteText.isNotEmpty() }
        filteredList.forEach { eventsList.add(mapper.from(it)) }
        return eventsList
    }

    fun getEventsList(date: String, onDataLoaded: (LinkedHashSet<ListItem>) -> Unit) {
        if (EventsCache.eventsList.isNotEmpty()) {
            onDataLoaded(getEventsFromLocalStorage(date))
        } else {
            val realmEventsList = DatabaseHelper.getEventsByDate(date)
            if (realmEventsList.isNotEmpty()) {
                onDataLoaded(prepareEventsFromDatabase(realmEventsList))
            } else {
                onDataLoaded(LinkedHashSet())
            }
        }
    }

    fun getAllEvents() = DatabaseHelper.getEvents()

    fun setupLocalStorage() {
        val realmEventsList = DatabaseHelper.getEvents()
        realmEventsList.forEach {
            EventsCache.eventsList.add(mapper.from(it))
        }
    }

    fun saveEvents(eventsList: ArrayList<Event>) {
        val realmEventsList = ArrayList<RealmEvent>()
        eventsList.forEach {
            realmEventsList.add(mapper.to(it))
        }
        DatabaseHelper.saveEvents(realmEventsList)
    }

    private fun getEventsFromLocalStorage(dateOfDay: String): LinkedHashSet<ListItem> {
        val eventsList = LinkedHashSet<ListItem>()
        EventsCache.getEventsByDate(dateOfDay).forEach {
            val date = Date(it.getDateOfEvent())
            eventsList.add(date)
            eventsList.add(it)
        }
        return eventsList
    }

    private fun prepareEventsFromDatabase(realmEventsList: RealmResults<RealmEvent>): LinkedHashSet<ListItem> {
        val eventsList = LinkedHashSet<ListItem>()
        realmEventsList.forEach {
            val date = mapper.from(it)
            eventsList.add(date)
            eventsList.add(mapper.from(it))
        }
        return eventsList
    }
}