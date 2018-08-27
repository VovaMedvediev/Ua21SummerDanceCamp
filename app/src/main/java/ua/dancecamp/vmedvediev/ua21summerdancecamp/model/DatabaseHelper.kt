package ua.dancecamp.vmedvediev.ua21summerdancecamp.model

import io.realm.Realm
import io.realm.RealmResults

object DatabaseHelper {

    fun getEventsByDate(date: String) : RealmResults<RealmEvent> =
         Realm.getDefaultInstance().where(RealmEvent::class.java).equalTo("date", date).findAll()


    fun getEvents() : RealmResults<RealmEvent> =
         Realm.getDefaultInstance().where(RealmEvent::class.java).findAll()

    fun getEventById(eventId: String) = Realm.getDefaultInstance()
            .where(RealmEvent::class.java).equalTo("id", eventId).findFirst()

    fun saveNote(realmEvent: RealmEvent) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insertOrUpdate(realmEvent)
            commitTransaction()
        }
    }

    fun getApplicationSettings() = Realm.getDefaultInstance().where(RealmSettings::class.java).findFirst()

    fun saveApplicationSettings(realmSettings: RealmSettings) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insertOrUpdate(realmSettings)
            commitTransaction()
        }
    }

    fun deleteNote(realmEvent: RealmEvent) {
        Realm.getDefaultInstance().use {
            it.executeTransaction {
                val updatingEvent = it.where(RealmEvent::class.java).equalTo("id", realmEvent.id).findFirst()
                updatingEvent?.let {
                    it.noteText = ""
                    it.noteDate = ""
                }
            }
        }
    }

    fun saveEvents(realmEventsList: ArrayList<RealmEvent>) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            delete(RealmEvent::class.java)
            insert(realmEventsList)
            commitTransaction()
        }
    }
}