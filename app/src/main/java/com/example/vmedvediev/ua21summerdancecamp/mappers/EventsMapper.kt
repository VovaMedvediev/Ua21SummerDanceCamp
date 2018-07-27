package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent

class EventsMapper : Mapper<RealmEvent, Event> {

    override fun to(initialObject: Event) = RealmEvent(initialObject.id, initialObject.name,
            initialObject.date, initialObject.getType())

    override fun from(initialObject: RealmEvent) = Event("", initialObject.name, initialObject.date)

}