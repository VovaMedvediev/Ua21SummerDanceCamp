package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import io.realm.RealmResults

class NotesRepository(private val eventsMapper: EventsMapper) {

    fun getNotes(onEventsLoaded: (ArrayList<Event>) -> Unit) {
        val realmEventsList = EventsDatabaseHelper.getEvents()
        if (realmEventsList.isNotEmpty()) {
            onEventsLoaded(prepareNotesFromDatabase(realmEventsList))
        } else {
            onEventsLoaded(ArrayList())
        }
    }

    fun getEvent(eventId: String, onEventLoaded: (Event) -> Unit) {
        val realmEvent = NotesDatabaseHelper.getEventById(eventId)
        if (realmEvent != null) {
            onEventLoaded(eventsMapper.from(realmEvent))
        } else {
            onEventLoaded(Event())
        }
    }

    fun deleteNoteFromDatabase(event: Event) {
        NotesDatabaseHelper.deleteNote(prepareNoteForDeletion(event))
    }

    fun saveNoteToDatabase(realmEvent: RealmEvent) {
        NotesDatabaseHelper.saveNote(realmEvent)
    }

    private fun prepareNoteForDeletion(event: Event) : RealmEvent {
        event.noteText = ""
        event.noteDateChanged = ""
        return eventsMapper.to(event)
    }

    private fun prepareNotesFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<Event> {
        val eventsList = ArrayList<Event>()
        realmEventsList.forEach {
            if (it.noteText.isNotEmpty()) {
                eventsList.add(eventsMapper.from(it))
            }
        }
        return eventsList
    }
}