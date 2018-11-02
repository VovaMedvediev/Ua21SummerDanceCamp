package ua.dancecamp.vmedvediev.ua21summerdancecamp.model

import io.realm.Realm
import io.realm.RealmResults
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity.RealmCredentials
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity.RealmEvent
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity.RealmSettings

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

    fun getCredentials() = Realm.getDefaultInstance().where(RealmCredentials::class.java).findFirst()

    fun saveApplicationSettings(realmSettings: RealmSettings) {
        Realm.getDefaultInstance().use {
            it.executeTransaction {
                it.insertOrUpdate(realmSettings)
            }
        }
    }

    fun saveCredentials(realmCredentials: RealmCredentials) {
        Realm.getDefaultInstance().use {
            it.executeTransaction {
                it.insertOrUpdate(realmCredentials)
            }
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
            clearInitialRealmEvents(this)
            insert(realmEventsList)
            commitTransaction()
        }
    }

    private fun clearInitialRealmEvents(realm: Realm) = realm.delete(RealmEvent::class.java)
}