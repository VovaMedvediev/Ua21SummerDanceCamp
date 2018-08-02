package com.example.vmedvediev.ua21summerdancecamp.model

import io.realm.Realm
import io.realm.RealmResults

object EventsDatabaseHelper {

    fun getEventsByDate(date: String) : RealmResults<RealmEvent> =
         Realm.getDefaultInstance().where(RealmEvent::class.java).equalTo("date", date).findAll()


    fun getEvents() : RealmResults<RealmEvent> =
         Realm.getDefaultInstance().where(RealmEvent::class.java).findAll()

}