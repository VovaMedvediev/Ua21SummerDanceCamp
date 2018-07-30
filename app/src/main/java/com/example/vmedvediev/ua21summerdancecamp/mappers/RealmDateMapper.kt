package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent

class RealmDateMapper : Mapper<RealmEvent, Date> {

    override fun from(initialObject: RealmEvent) = Date(initialObject.date)

    override fun to(initialObject: Date) = RealmEvent()
}