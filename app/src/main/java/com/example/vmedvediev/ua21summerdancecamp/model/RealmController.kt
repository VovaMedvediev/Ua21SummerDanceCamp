package com.example.vmedvediev.ua21summerdancecamp.model

import io.realm.Realm
import io.realm.RealmResults

object RealmController {

    fun getEventsByDate(date: String) : RealmResults<RealmEvent> {
        return Realm.getDefaultInstance().where(RealmEvent::class.java).equalTo("date", date).findAll()
    }

    fun getAllEvents() : RealmResults<RealmEvent> {
        return Realm.getDefaultInstance().where(RealmEvent::class.java).findAll()
    }
}