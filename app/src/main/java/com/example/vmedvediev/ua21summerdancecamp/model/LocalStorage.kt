package com.example.vmedvediev.ua21summerdancecamp.model

object LocalStorage {

    val eventsList = ArrayList<ListItem>()

    fun getAmountOfEventsByDate(date: String) : Int {
        var amountOfEvents = 0
        eventsList.forEach {
            if (it.getDateOfEvent() == date) {
                amountOfEvents++
            }
        }
        //We increment result in order to add date object.
        return ++amountOfEvents
    }

    fun getEventsByDate(date: String) : ArrayList<ListItem> {
        val sortedByDateList = ArrayList<ListItem>()
        eventsList.forEach {
            if (it.getDateOfEvent() == date) {
                sortedByDateList.add(it)
            }
        }
        return sortedByDateList
    }
}