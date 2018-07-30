package com.example.vmedvediev.ua21summerdancecamp.mappers

import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem

class ListItemDateMapper : Mapper<ListItem, Date> {
    override fun from(initialObject: ListItem) = Date(initialObject.getDateOfEvent())

    override fun to(initialObject: Date) = Date()
}