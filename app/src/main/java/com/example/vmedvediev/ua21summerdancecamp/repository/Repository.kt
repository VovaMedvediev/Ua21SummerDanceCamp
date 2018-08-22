package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import io.realm.RealmResults

class Repository(private val eventsMapper: RealmEventMapper, private val settingsMapper: RealmSettingsMapper) {

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
            onEventLoaded(eventsMapper.from(realmEvent))
        } else {
            onEventLoaded(Event())
        }
    }

    fun getApplicationSettings(onApplicationSettingsLoaded: (ApplicationSettings) -> Unit) {
        val realmSettings = DatabaseHelper.getApplicationSettings()
        return if (realmSettings != null) {
            onApplicationSettingsLoaded(settingsMapper.from(realmSettings))
        } else {
            onApplicationSettingsLoaded(ApplicationSettings())
        }
    }

    fun deleteNoteFromDatabase(event: Event) {
        DatabaseHelper.deleteNote(eventsMapper.to(event))
    }

    fun saveNoteToDatabase(event: Event) {
        DatabaseHelper.saveNote(eventsMapper.to(event))
    }

    private fun prepareNotesFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<Event> {
        val eventsList = ArrayList<Event>()
        val filteredList = realmEventsList.filter { it.noteText.isNotEmpty() }
        filteredList.forEach { eventsList.add(eventsMapper.from(it)) }
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

    fun getAllEvents() = DatabaseHelper.getEvents()

    fun setupLocalStorage() {
        val realmEventsList = DatabaseHelper.getEvents()
        realmEventsList.forEach {
            EventsCache.eventsList.add(eventsMapper.from(it))
        }
    }

    fun saveEvents(eventsList: ArrayList<Event>) {
        val realmEventsList = ArrayList<RealmEvent>()
        eventsList.forEach {
            realmEventsList.add(eventsMapper.to(it))
        }
        DatabaseHelper.saveEvents(realmEventsList)
    }

    fun saveApplicationSettings(applicationSettings: ApplicationSettings) {
        val realmSettings = settingsMapper.to(applicationSettings)
        DatabaseHelper.saveApplicationSettings(realmSettings)
    }

    private fun getEventsFromLocalStorage(dateOfDay: String): ArrayList<ListItem> {
        val eventsList = ArrayList<ListItem>()
        EventsCache.getEventsByDate(dateOfDay).forEach {
            val date = Date(it.getDateOfEvent())
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
            val date = eventsMapper.from(it)
            if (!eventsList.contains(date)) {
                eventsList.add(date)
            }
            eventsList.add(eventsMapper.from(it))
        }
        return eventsList
    }
}