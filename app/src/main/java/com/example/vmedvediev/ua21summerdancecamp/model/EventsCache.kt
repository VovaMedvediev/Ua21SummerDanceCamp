package com.example.vmedvediev.ua21summerdancecamp.model

import java.util.*

object EventsCache {

    val eventsList = ArrayList<ListItem>()

    fun getAmountOfEventsByDate(date: String) : Int {
        var amountOfEvents = 0
        eventsList.forEach {
            if (it.getDateOfEvent() == date) {
                amountOfEvents++
            }
        }
        //We increment result in order to add eventDate object.
        return ++amountOfEvents
    }

    fun getEventsByDate(date: String) : ArrayList<ListItem> {
        val selectedByDateList = ArrayList<ListItem>()
        eventsList.forEach {
            if (it.getDateOfEvent() == date) {
                selectedByDateList.add(it)
            }
        }
        return selectedByDateList
    }
}