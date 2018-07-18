package com.example.vmedvediev.ua21summerdancecamp.realm

import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent
import io.realm.Realm
import io.realm.RealmResults

object RealmController {

    fun refresh() {
        Realm.getDefaultInstance().refresh()
    }

    fun clearAll() {
        Realm.getDefaultInstance().beginTransaction()
        Realm.getDefaultInstance().delete(RealmEvent::class.java)
        Realm.getDefaultInstance().commitTransaction()
    }

    fun getEventsForDay(date: String) : RealmResults<RealmEvent> {
        return Realm.getDefaultInstance().where(RealmEvent::class.java).equalTo("date", date).findAll()
    }

    fun getEvent(id: String): RealmEvent? {
        return Realm.getDefaultInstance().where(RealmEvent::class.java).equalTo("id", id).findFirst()
    }

    fun queryedEvents() : RealmResults<RealmEvent> {
        return Realm.getDefaultInstance().where(RealmEvent::class.java)
                .contains("name", "event1")
                .or()
                .contains("id", "1")
                .findAll()
    }
}