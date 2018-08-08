package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent

class RealmEventMapper : Mapper<RealmEvent, Event> {

    override fun from(initialObject: RealmEvent) = Event(initialObject.id, initialObject.name,
            initialObject.date, initialObject.eventTime, initialObject.noteText, initialObject.noteDate,
            initialObject.dateType, initialObject.canHaveNote)

    override fun to(initialObject: Event) = RealmEvent(initialObject.eventId,
            initialObject.eventName, initialObject.eventDate, initialObject.eventTime,
            initialObject.eventNoteText, initialObject.eventNoteDate, initialObject.eventType,
            initialObject.canHaveNote)
}