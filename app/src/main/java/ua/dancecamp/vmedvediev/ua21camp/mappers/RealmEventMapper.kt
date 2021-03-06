package ua.dancecamp.vmedvediev.ua21camp.mappers

import ua.dancecamp.vmedvediev.ua21camp.model.Event
import ua.dancecamp.vmedvediev.ua21camp.model.entity.RealmEvent

class RealmEventMapper : Mapper<RealmEvent, Event> {

    override fun from(initialObject: RealmEvent) = Event(initialObject.id, initialObject.name,
            initialObject.date, initialObject.noteText, initialObject.noteDate,
            initialObject.dateType, initialObject.eventTime, initialObject.canHaveNote, initialObject.image)

    override fun to(initialObject: Event) = RealmEvent(initialObject.eventId,
            initialObject.eventName, initialObject.eventDate, initialObject.eventNoteText,
            initialObject.eventNoteDate, initialObject.eventType, initialObject.eventTime,
            initialObject.canHaveNote, initialObject.eventImage)
}