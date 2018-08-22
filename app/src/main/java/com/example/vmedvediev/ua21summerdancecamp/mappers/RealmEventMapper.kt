package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent

class RealmEventMapper : Mapper<RealmEvent, Event> {

    override fun from(initialObject: RealmEvent) = Event(initialObject.id, initialObject.name,
            initialObject.date, initialObject.noteText, initialObject.noteDate,
            initialObject.dateType, initialObject.eventTime, initialObject.canHaveNote, initialObject.image)

    override fun to(initialObject: Event) = RealmEvent(initialObject.eventId,
            initialObject.eventName, initialObject.eventDate, initialObject.eventNoteText,
            initialObject.eventNoteDate, initialObject.eventType, initialObject.eventTime,
            initialObject.canHaveNote, initialObject.eventImage)
}