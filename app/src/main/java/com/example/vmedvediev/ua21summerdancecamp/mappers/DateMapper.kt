package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent

class DateMapper : Mapper<RealmEvent, Date> {
    override fun from(initialObject: RealmEvent) = Date(initialObject.date, initialObject.date, 1)

    override fun to(initialObject: Date) = RealmEvent()
}