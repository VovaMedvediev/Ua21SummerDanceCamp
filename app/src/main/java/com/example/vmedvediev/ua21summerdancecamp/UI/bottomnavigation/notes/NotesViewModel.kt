package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent
import com.example.vmedvediev.ua21summerdancecamp.repository.NotesRepository

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val events: MutableLiveData<ArrayList<Event>> = MutableLiveData()
    private val event: MutableLiveData<Event> = MutableLiveData()

    fun getEventsFromRepository() {
        repository.getNotes { notesList: ArrayList<Event> -> onEventsLoaded(notesList)}
    }

    fun getEventFromRepository(eventId: String) {
        repository.getEvent(eventId) { event: Event -> onEventLoaded(event)}
    }

    fun getNotes() = events

    fun getEvent() = event

    fun getEventValue() = event.value!!

    fun getEventsList() = events.value!!

    fun deleteNoteFromDatabase(event: Event) {
        repository.deleteNoteFromDatabase(event)
    }

    fun saveNoteToDatabase(realmEvent: RealmEvent) {
        repository.saveNoteToDatabase(realmEvent)
    }



    private fun onEventsLoaded(notesList: ArrayList<Event>) {
        if (notesList.isNotEmpty()) {
            events.value = notesList
        } else {
            ArrayList<Event>()
        }
    }

    private fun onEventLoaded(loadedEvent: Event?) {
        if (loadedEvent != null) {
            event.value = loadedEvent
        } else {
            Event()
        }
    }
}