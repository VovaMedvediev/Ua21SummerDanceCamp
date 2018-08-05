package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent

class MapperImpl : Mapper<ListItem, ListItem> {

    override fun from(initialObject: ListItem, isEvent: Boolean): ListItem {
        return when {
            (initialObject is RealmEvent) && isEvent -> Event(initialObject.id, initialObject.name,
                    initialObject.date, initialObject.noteText, initialObject.noteDate)
            initialObject is RealmEvent -> Date(initialObject.date)
            else -> Date(initialObject.getDateOfEvent())
        }
    }

    override fun to(initialObject: ListItem): ListItem {
        return when (initialObject) {
            is Event -> RealmEvent(initialObject.eventId, initialObject.eventName,
                    initialObject.eventDate, initialObject.eventNoteText, initialObject.eventNoteDate)
            else -> RealmEvent()
        }
    }
}