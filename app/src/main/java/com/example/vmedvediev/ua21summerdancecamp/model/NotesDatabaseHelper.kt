package com.example.vmedvediev.ua21summerdancecamp.model

import io.realm.Realm

object NotesDatabaseHelper {

    fun getEventById(eventId: String) = Realm.getDefaultInstance()
            .where(RealmEvent::class.java).equalTo("id", eventId).findFirst()

    fun saveNote(realmEvent: RealmEvent) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insertOrUpdate(realmEvent)
            commitTransaction()
        }
    }

    fun deleteNote(realmEvent: RealmEvent) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insertOrUpdate(realmEvent)
            commitTransaction()
        }
    }
}