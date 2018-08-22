package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import com.example.vmedvediev.ua21summerdancecamp.model.Date
import io.realm.RealmResults
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

class Repository(private val eventsMapper: RealmEventMapper, private val settingsMapper: RealmSettingsMapper) {

    fun getNotes(onEventsLoaded: (ArrayList<Event>) -> Unit) {
        val realmEventsList = DatabaseHelper.getEvents()
        onEventsLoaded(if (realmEventsList.isNotEmpty()) prepareNotesFromDatabase(realmEventsList) else ArrayList())
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

    fun getEventsList(date: String, onDataLoaded: (LinkedHashSet<ListItem>) -> Unit) {
        val realmEventsList = DatabaseHelper.getEventsByDate(date)
        onDataLoaded(if (EventsCache.eventsList.isNotEmpty()) getEventsFromLocalStorage(date) else if (realmEventsList.isNotEmpty()) prepareEventsFromDatabase(realmEventsList) else LinkedHashSet())
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
            val date = eventsMapper.from(it)
            eventsList.apply {
                add(date)
                add(eventsMapper.from(it))
            }
        }
        return eventsList
    }
}